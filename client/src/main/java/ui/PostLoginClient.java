package ui;
import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PostLoginClient implements ClientObject {
    private String gameID = null;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final ClientCommunicator notificationHandler;
    boolean pre;
    boolean post;
    boolean game;
    private Map<Integer, Integer> IDs = null;

    public PostLoginClient(String serverUrl, ClientCommunicator notificationHandler, ServerFacade serverFacade){
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
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
    }
    public String eval(String input) {
        pre = false;
        post = false;
        game = false;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length >= 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list(String... params) throws ResponseException {
        ListGamesResult listGamesResult = server.listGames(new ListGamesRequest(this.authToken));
        Map<Integer, Integer> myMap = new HashMap<>();
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var out = new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8);
        out.println("  Game Name        Player Names");
        out.println("-------------------------------------------------");
        int i = 1;
        for (GameData game: listGamesResult.games()){
//            out.println();
            out.print(i);
            out.print("    ");
            out.print(game.gameName());
            out.print("   ");
            if (game.whiteUsername() != null) {
                out.print(game.whiteUsername());
                out.print(",  ");
            }
            if (game.blackUsername() != null) {
                out.print(game.blackUsername());
            }
            i += 1;
            myMap.put(i, game.gameID());
        }
        this.IDs = myMap;
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }
    public String join(String... params) throws ResponseException {
        if (params.length == 2 && (params[1].equals("white") || params[1].equals("black"))){
            ChessBoard board = new ChessBoard();
            ui.ChessBoard draw = new ui.ChessBoard();
            board.resetBoard();
            if (params[1] == "WHITE") {
                System.out.print(authToken);
                server.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, IDs.get(params[0]), authToken));
                draw.drawWhite(board);
            }
            else {
                server.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, IDs.get(params[0]), authToken));
                draw.drawBlack(board);
            }
            return String.format("You joined the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }
    public String observe(String... params) throws ResponseException {
        if (params.length == 1){
            ChessBoard board = new ChessBoard();
            ui.ChessBoard draw = new ui.ChessBoard();
            board.resetBoard();
            draw.drawWhite(board);
            return String.format("You are observing the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <ID>");
        }

    public String logout(String... params) throws ResponseException {
        server.logout();
        pre = true;
        return "You have logged out.";
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

