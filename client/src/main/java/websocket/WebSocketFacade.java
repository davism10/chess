package websocket;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.lang.module.ResolutionException;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    NotificationHandler notificationHandler;
    Session session;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notifyError(errorMessage);
                    }
                    else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notificationMessage);
                    }
                    else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notifyLoadGame(loadGameMessage);

                    }
                }
            });
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leave(String authToken, int gameId) throws ResponseException{
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    public void resign(String authToken, int gameId) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameId, ChessMove move) throws ResponseException{
        try {
            var action = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void connect(String authToken, int gameId) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}


