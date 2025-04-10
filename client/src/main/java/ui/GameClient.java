package ui;
import chess.*;
import chess.ChessBoard;
import exception.ResponseException;
import model.*;
import net.ClientCommunicator;
import net.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
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
    ChessGame.TeamColor color = null;
    public GameData gameData = null;

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
        if (params.length == 0) {
            ui.ChessBoard draw = new ui.ChessBoard();
            if (color == ChessGame.TeamColor.WHITE) {
                draw.drawWhite(gameData.game().getBoard(), null);
            } else {
                draw.drawBlack(gameData.game().getBoard(), null);
            }
            return null;
        }
        throw new ResponseException(400, "Expected no user input");
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
            try {
                char colLetter = params[0].charAt(0);
                int row = Integer.parseInt(params[0]);
                int col = colLetter - 'a' + 1;
                ChessPosition start = new ChessPosition(row, col);
                Collection<ChessMove> posMoves = ChessPiece.pieceMoves(gameData.game().getBoard(), start);
                ui.ChessBoard draw = new ui.ChessBoard();
                if (color == ChessGame.TeamColor.WHITE) {
                    draw.drawWhite(gameData.game().getBoard(), posMoves);
                } else {
                    draw.drawBlack(gameData.game().getBoard(), posMoves);
                }
                return null;
            } catch (Exception e) {
                throw new ResponseException(400, "Incorrect square notation, try again");
            }
        }
        throw new ResponseException(400, "Expected no parameters");
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
