package ui;
import exception.ResponseException;
import net.ClientCommunicator;
import net.ServerFacade;
import net.WebSocketFacade;

import java.util.Arrays;

public class PreLoginClient implements ClientObject {
    private String visitorName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;
    private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
    boolean pre;
    boolean post;
    boolean game;

    public PreLoginClient(String serverUrl, ClientCommunicator notificationHandler){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        this.pre = false;
        this.post = false;
        this.game = false;
    }

    public String help() {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quite - playing chess
                    help - with possible commands
                    """;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
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
