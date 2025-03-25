package ui;
import exception.ResponseException;
import model.LoginRequest;
import model.RegisterRequest;
import net.ClientCommunicator;
import net.ServerFacade;

import java.util.Arrays;

public class PreLoginClient implements ClientObject {
    private final ServerFacade server;
    private final String serverUrl;
    private String authToken = null;
    private final ClientCommunicator notificationHandler;
    boolean pre;
    boolean post;
    boolean game;

    public PreLoginClient(String serverUrl, ClientCommunicator notificationHandler, ServerFacade serverFacade){
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

    public String help() {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - Create an account with given username, password, and email
                    login <USERNAME> <PASSWORD> - Login using existing username and corresponding password
                    quit - leave chess
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
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unkown request, type 'help' to see valid requests";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            server.register(new RegisterRequest(params[0], params[1], params[2]));
            post = true;
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            server.login(new LoginRequest(params[0], params[1]));
            post = true;
            return String.format("You logged in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");

    }

    public boolean getPost() {
        return this.post;
    }

    public boolean getPre() {
        return this.pre;
    }

    public boolean getGame() {
        return this.game;
    }
}
