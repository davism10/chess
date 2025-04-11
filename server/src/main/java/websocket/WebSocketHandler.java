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
                    makeMove(newAction.getAuthToken(), newAction.getGameID(), newAction.getMove(), session);
                }
                case LEAVE -> leave(action.getAuthToken(), action.getGameID(), session);
                case RESIGN -> resign(action.getAuthToken(), action.getGameID(), session);
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
            connections.broadcast(visitorName, notification, session);
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

    private void makeMove(String authToken, int gameId, ChessMove move, Session session) throws ResponseException {
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
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var gameNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameService.getGame(gameId));
            connections.broadcastAll(gameNotification, session);
            connections.broadcast(visitorName, notification, session);

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

    private void leave(String authToken, int gameId, Session session) throws ResponseException {
        try {
            String visitorName = userService.getUser(authToken);
            connections.remove(visitorName);
            var message = String.format("%s left the game", visitorName);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(visitorName, notification, session);
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

    private void resign(String authToken, int gameId, Session session) throws ResponseException {
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

            connections.broadcastAll(notification, session);
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
