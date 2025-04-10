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
                case MAKE_MOVE -> makeMove(action.getAuthToken(), action.getGameID(), action.getMove());
                case LEAVE -> leave(action.getAuthToken());
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
            connections.add(visitorName, session);
            String teamColor;
            if (gameService.getGame(gameId).whiteUsername() == visitorName){
                teamColor = "white";
            }
            else if (gameService.getGame(gameId).blackUsername() == visitorName){
                teamColor = "black";
            }
            else {
                teamColor = "an observer";
            }

            var message = String.format("%s joined the game as %s", visitorName, teamColor);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var gameNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameService.getGame(gameId));
            connections.broadcast(visitorName, notification);
            session.getRemote().sendString(new Gson().toJson(gameNotification));

        }
        catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void makeMove(String authToken, int gameId, ChessMove move) throws ResponseException {

        try {
            String visitorName = userService.getUser(authToken);
            ChessPiece piece = gameService.getGame(gameId).game().getBoard().getPiece(move.getStartPosition());
            var message = String.format("%s moved %s from %s to %s", visitorName, piece.getPieceType(),
                    move.getStartPosition().toString(), move.getEndPosition().toString());
            gameService.getGame(gameId).game().makeMove(move);
            gameService.updategame(gameId, gameService.getGame(gameId));
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            var gameNotification = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameService.getGame(gameId));
            connections.broadcast(visitorName, notification);
            connections.broadcast(visitorName, gameNotification);

        } catch (Exception e) {
            throw new ResponseException(500,e.getMessage());
        }
    }

    private void leave(String authToken) throws ResponseException {
        try {
            String visitorName = userService.getUser(authToken);
            connections.remove(visitorName);
            var message = String.format("%s left the game", visitorName);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(visitorName, notification);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void resign(String authToken, int gameId) throws ResponseException {
        try {
            String visitorName = userService.getUser(authToken);
            GameData oldGame = gameService.getGame(gameId);
            GameData newGame;
            ChessGame newGameInfo;
            String otherUser;

            String teamColor;
            if (gameService.getGame(gameId).whiteUsername() == visitorName) {
                teamColor = "white";
                newGameInfo = oldGame.game();
                newGameInfo.setTeamTurn(null);
                otherUser = oldGame.blackUsername();
                newGame = new GameData(gameId, null, oldGame.blackUsername(), oldGame.gameName(), newGameInfo);

            } else if (gameService.getGame(gameId).blackUsername() == visitorName) {
                teamColor = "black";
                newGameInfo = oldGame.game();
                newGameInfo.setTeamTurn(null);
                otherUser = oldGame.whiteUsername();
                newGame = new GameData(gameId, oldGame.whiteUsername(), null, oldGame.gameName(), newGameInfo);
            } else {
                teamColor = "an observer";
                throw new ResponseException(500, "Error: an observer cannot resign");
            }

            var message = String.format("%s(%s) resigned. %s has won the game.", visitorName, teamColor, otherUser);
            var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(visitorName, notification);
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

}
