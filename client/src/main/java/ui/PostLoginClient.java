package ui;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

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
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    boolean pre;
    boolean post;
    boolean game;
    boolean observe = false;
    GameData gameData = null;
    private Map<Integer, GameData> iDs = null;
    ChessGame.TeamColor color = null;

    public PostLoginClient(String serverUrl, NotificationHandler notificationHandler, ServerFacade serverFacade){
        server = serverFacade;
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        this.pre = false;
        this.post = false;
        this.game = false;
    }

    public void setObserve(Boolean observe) {
    }

    public boolean isObserved(){
        return observe;
    }

    public void connectAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public GameData getGameInfo(){
        return this.gameData;
    }

    public void attatchGameInfo(GameData gameData){
        this.gameData = gameData;
    }

    public ChessGame.TeamColor getColor(){
        return this.color;
    }

    public void attatchColor(ChessGame.TeamColor color){
        this.color = color;
    }


    public String help() {
        return """
                    create <NAME> - create a game with NAME as game name
                    list - list all game ids, game names, and players
                    join <ID> [WHITE|BLACK] - must run list before joining a game with game id
                    observe <ID> - must run list before observing a game id
                    logout - when you are done
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
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> "Unkown request, type 'help' to see valid requests";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResult gameResult = server.createGame(new CreateGameRequest(this.authToken, params[0]));
            return String.format("You created the game %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list(String... params) throws ResponseException {
        ListGamesResult listGamesResult = server.listGames(new ListGamesRequest(this.authToken));
        Map<Integer, GameData> myMap = new HashMap<>();
        var byteArrayOutputStream = new ByteArrayOutputStream();
        var out = new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8);
        out.printf("%-5s %-20s %-20s %-20s%n", "ID", "Game Name", "White Player Name", "Black Player Name");
        out.println("----------------------------------------------------------------------");

        int i = 1;
        for (GameData game : listGamesResult.games()) {
            out.printf("%-5d %-20s %-20s %-20s%n",
                    i,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "-",
                    game.blackUsername() != null ? game.blackUsername() : "-"
            );
            myMap.put(i, game);
            i += 1;
        }
        this.iDs = myMap;
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }
    public String join(String... params) throws ResponseException {
        if (params.length == 2 && (params[1].equals("WHITE") || params[1].equals("BLACK"))){
            ui.ChessBoard draw = new ui.ChessBoard();
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            try {
                this.gameData = iDs.get(Integer.parseInt(params[0]));
                if (params[1].equals("WHITE")) {
                    this.color = ChessGame.TeamColor.WHITE;
                    server.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, iDs.get(Integer.parseInt(params[0])).gameID(), authToken));
                    ws.connect(authToken, gameData.gameID());
                } else {
                    this.color = ChessGame.TeamColor.BLACK;
                    server.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, iDs.get(Integer.parseInt(params[0])).gameID(), authToken));
                    ws.connect(authToken, gameData.gameID());
                }
                this.game = true;

                return String.format("You joined the game %s.", params[0]);
            } catch (Exception e) {
                throw new ResponseException(400, "Game ID does not exist");
            }
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        if (params.length == 1){
            try {
                ws = new WebSocketFacade(serverUrl, notificationHandler);
                ui.ChessBoard draw = new ui.ChessBoard();
                this.observe = true;
                this.gameData = iDs.get(Integer.parseInt(params[0]));
                this.game = true;
                this.color = ChessGame.TeamColor.WHITE;
                ws.connect(authToken, gameData.gameID());
                return String.format("You are observing the game %s.", params[0]);
            }
            catch(Exception e) {
                throw new ResponseException(400, "Game ID not valid");
            }
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

