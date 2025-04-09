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
    boolean observe = false;

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

    public void setObserve(Boolean observe) {
        this.observe = observe;
    }

    public boolean isObserved(){
        return false;
    }

    public String help(){
        return """
                redraw - move the game board down, so it is easily viewed
                leave - when you are done playing the game
                move <START SQUARE> [END SQUARE] - when you are ready to make a valid move (square must be give in the form <number><letter>)
                resign <ID> - admitting defeat ~ will end the game
                highlight <SQUARE> - will highlight valid moves for the square that you have been given (square must be give in the form <number><letter>)
                help - with possible commands
                """;
    }
    public String eval(String input) {
        pre = false;
        post = false;
        game = false;
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "highlight" -> highlight(params);
                case "help" -> help();
                default -> "Unkown request, type 'help' to see valid requests";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String leave(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String move(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String resign(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String highlight(String... params) throws ResponseException {
        if (params.length == 0) {

        }
        throw new ResponseException(400, "Expected no paramters");
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
