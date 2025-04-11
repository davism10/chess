package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import service.GameService;


import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private GameService gameService;
    private UserService userService;

    public WebSocketHandler(GameService gameService, UserService userService){
        this.gameService = gameService;
        this.userService = userService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), session);
                case MAKE_MOVE -> {
                    MakeMoveCommand newAction = new Gson().fromJson(message, MakeMoveCommand.class);
                    makeMove(newAction.getAuthToken(), newAction.getGameID(), newAction.getMove());
                }
                case LEAVE -> leave(action.getAuthToken(), action.getGameID());
                case RESIGN -> resign(action.getAuthToken(), action.getGameID());
            }
        }
        catch (Exception e) {
            var errorMessage = String.format("Error: message is not valid because %s", e);
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            session.getRemote().sendString(new Gson().toJson(notification));
        }
    }


    private void connect(String authToken, Integer gameId, Session session) throws ResponseException {
        try {
            String visitorName = userService.getUser(authToken);
            connections.add(visitorName, gameId, session);
            String teamColor = getTeamColor(gameId, visitorName);

            var message = String.format("%s joined the game as %s", visitorName, teamColor);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var gameNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameService.getGame(gameId));
            connections.broadcast(visitorName, notification, gameId);
            session.getRemote().sendString(new Gson().toJson(gameNotification));

        }
        catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private String getTeamColor(Integer gameId, String visitorName) throws ResponseException {
        String teamColor;
        if (gameService.getGame(gameId).whiteUsername().equals(visitorName)){
            teamColor = "white";
        }
        else if (gameService.getGame(gameId).blackUsername().equals(visitorName)){
            teamColor = "black";
        }
        else {
            teamColor = "an observer";
        }
        return teamColor;
    }

    private ChessGame.TeamColor getTeamColorType(Integer gameId, String visitorName) throws ResponseException {
        ChessGame.TeamColor teamColor;
        if (gameService.getGame(gameId).whiteUsername().equals(visitorName)){
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (gameService.getGame(gameId).blackUsername().equals(visitorName)){
            teamColor = ChessGame.TeamColor.BLACK;
        }
        else {
            teamColor = null;
        }
        return teamColor;
    }

    public String otherTeam(String team){
        if (team.equals("white")){
            return "black";
        }
        return "white";
    }

    private void makeMove(String authToken, int gameId, ChessMove move) throws ResponseException {
        String visitorName = userService.getUser(authToken);
        if (connections.isResign(gameId)){
            throw new ResponseException(500, "game is over");
        }
        if (getTeamColorType(gameId, visitorName) != gameService.getGame(gameId).game().getTeamTurn()){
            throw new ResponseException(500, "Not your turn!");
        }
        try {
            ChessPiece piece = gameService.getGame(gameId).game().getBoard().getPiece(move.getStartPosition());
            var message = String.format("%s moved %s from %s to %s", visitorName, piece.getPieceType(),
                    move.getStartPosition().toString(), move.getEndPosition().toString());
            ChessGame oldGame = gameService.getGame(gameId).game();
            oldGame.makeMove(move);
            GameData newGameData = new GameData(gameId, gameService.getGame(gameId).whiteUsername(),
                    gameService.getGame(gameId).blackUsername(), gameService.getGame(gameId).gameName(), oldGame);
            gameService.updategame(gameId, newGameData);

            boolean checkmate = gameService.getGame(gameId).game().isInCheckmate(ChessGame.notColor(getTeamColorType(gameId, visitorName)));
            boolean check = gameService.getGame(gameId).game().isInCheck(ChessGame.notColor(getTeamColorType(gameId, visitorName)));
            boolean stalemate = gameService.getGame(gameId).game().isInStalemate(ChessGame.notColor(getTeamColorType(gameId, visitorName)));

            if (checkmate) {
                var mateMessage = String.format("%s is in checkmate by %s", otherTeam(getTeamColor(gameId, visitorName)));
                var mateNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, mateMessage);
                connections.broadcastAll(mateNotification, gameId);
            }
            else if (check) {
                var checkMessage = String.format("%s is in check", otherTeam(getTeamColor(gameId, visitorName)));
                var checkNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, checkMessage);
                connections.broadcastAll(checkNotification, gameId);
            }
            if (stalemate) {
                var staleMessage = String.format("%s is in stalemate", otherTeam(getTeamColor(gameId, visitorName)));
                var staleNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, staleMessage);
                connections.broadcastAll(staleNotification, gameId);
            }

            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var gameNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameService.getGame(gameId));
            connections.broadcastAll(gameNotification, gameId);
            connections.broadcast(visitorName, notification, gameId);

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

    private void leave(String authToken, int gameId) throws ResponseException {
        try {
            String visitorName = userService.getUser(authToken);
            connections.remove(visitorName);
            var message = String.format("%s left the game", visitorName);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(visitorName, notification, gameId);
            GameData newGame;
            GameData oldGame = gameService.getGame(gameId);
            if (getTeamColor(gameId, visitorName).equals("white")){
                newGame = new GameData(gameId, null, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
                gameService.updategame(gameId, newGame);
                }
            else if (getTeamColor(gameId, visitorName).equals("black")){
                newGame = new GameData(gameId, oldGame.whiteUsername(), null, oldGame.gameName(), oldGame.game());
                gameService.updategame(gameId, newGame);
            }
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void resign(String authToken, int gameId) throws ResponseException {
        if (connections.isResign(gameId)){
            throw new ResponseException(500, "game is over");
        }
        try {
            String visitorName = userService.getUser(authToken);
            GameData oldGame = gameService.getGame(gameId);
            String otherUser;

            String teamColor;
            if (gameService.getGame(gameId).whiteUsername().equals(visitorName)) {
                teamColor = "white";
                otherUser = oldGame.blackUsername();

            } else if (gameService.getGame(gameId).blackUsername().equals(visitorName)) {
                teamColor = "black";
                otherUser = oldGame.whiteUsername();
            } else {
                teamColor = "an observer";
                throw new ResponseException(500, "Error: an observer cannot resign");
            }

            var message = String.format("%s(%s) resigned. %s has won the game.", visitorName, teamColor, otherUser);
            connections.resigned(gameId);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            connections.broadcastAll(notification, gameId);
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
