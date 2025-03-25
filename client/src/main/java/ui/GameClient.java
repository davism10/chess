package ui;
import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameClient implements ClientObject {
    private String gameID = null;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;
    boolean pre;
    boolean post;
    boolean game;

    public GameClient(String serverUrl, ClientCommunicator notificationHandler, ServerFacade serverFacade){
        server = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        this.pre = false;
        this.post = false;
        this.game = false;
    }

    public void connectAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String help(){
        return "";
    }
    public String eval(String line){
        return "";
    }
    public boolean getPost(){
        return post;
    }
    public boolean getPre(){
        return pre;
    }
    public boolean getGame(){
        return game;
    }
}
