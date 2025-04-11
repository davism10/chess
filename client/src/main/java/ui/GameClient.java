package ui;
import chess.*;
import exception.ResponseException;
import model.*;
import net.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import java.util.*;

public class GameClient implements ClientObject {
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
    ChessGame.TeamColor color = null;
    public GameData gameData = null;

    public GameClient(String serverUrl, NotificationHandler notificationHandler, ServerFacade serverFacade){
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
        if (observe) {
            return """
                    redraw - move the game board down, so it is easily viewed
                    leave - when you are done watching the game
                    highlight <SQUARE> - will highlight valid moves for the square that you have been given
                     (square must be give in the form <number><letter>)
                    help - with possible commands
                    """;
        }
        return """
                    redraw - move the game board down, so it is easily viewed
                    leave - when you are done playing the game
                    move <START SQUARE> <END SQUARE> - when you are ready to make a valid move 
                    (square must be give in the form <number><letter>)
                    resign - admitting defeat ~ will end the game
                    highlight <SQUARE> - will highlight valid moves for the square that you have been given 
                    (square must be give in the form <number><letter>)
                    help - with possible commands
                    """;

    }
    public String eval(String input) {
        pre = false;
        post = false;
        game = false;
        try {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
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
            return " ";
        }
        throw new ResponseException(400, "Expected no user input");
    }

    public String leave(String... params) throws ResponseException {
        if (params.length == 0) {
            ws.leave(authToken, gameData.gameID());
            this.post = true;
            return String.format("Goodbye, Thanks for playing!");
        }
        throw new ResponseException(400, "Expected: no user input");
    }

    public String move(String... params) throws ResponseException {
        if (params.length == 2) {
            char colLetter = params[0].charAt(0);
            int row = Integer.parseInt(params[0].substring(1));
            int col;
            if (color == ChessGame.TeamColor.WHITE) {
                col = colLetter - 'a' + 1;
            }
            else {
                col = 9 - (colLetter - 'a' + 1);
            }
            ChessPosition start = new ChessPosition(row, col);

            char colLetterEnd = params[1].charAt(0);
            int rowEnd = Integer.parseInt(params[1].substring(1));
            int colEnd;
            if (color == ChessGame.TeamColor.WHITE) {
                colEnd = colLetterEnd - 'a' + 1;
            }
            else {
                colEnd = 9 - (colLetterEnd - 'a' + 1);
            }
            ChessPosition end = new ChessPosition(rowEnd, colEnd);

            ChessPiece myPiece = gameData.game().getBoard().getPiece(start);
            Collection<ChessMove> posMoves = myPiece.pieceMoves(gameData.game().getBoard(), start);

            boolean valid = false;
            for (ChessMove posMove: posMoves){
                if (posMove.getEndPosition().equals(end)){
                    valid = true;
                }
            }
            if (!valid){
                throw new ResponseException(500, "Invalid move");
            }
            ChessPiece.PieceType promotion = null;
            if (myPiece.getPieceType() == ChessPiece.PieceType.PAWN && (row == 1
                    || row == 8)){
                promotion = getPromotionPieceType();
            }
            ChessMove chessMove = new ChessMove(start, end, promotion);
            ws.makeMove(authToken, gameData.gameID(), chessMove);
            return String.format("Move pending");
        }
        throw new ResponseException(400, "Expected: <START SQUARE> <END SQUARE>");
    }

    private ChessPiece.PieceType getPromotionPieceType() {
        notificationHandler.printPrompt();
        System.out.println("congratulations, your pawn is being promoted!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("please type (queen|rook|bishop|knight) to indicate desired promotion :)");
            String line = scanner.nextLine();
            if (line.equals("queen")) {
                return ChessPiece.PieceType.QUEEN;
            } else if (line.equals("rook")) {
                return ChessPiece.PieceType.ROOK;
            } else if (line.equals("bishop")) {
                return ChessPiece.PieceType.BISHOP;
            } else if (line.equals("knight")) {
                return ChessPiece.PieceType.KNIGHT;
            } else {
                System.out.println("Invalid piece type, try again");
            }
        }
    }

    public boolean resignGood(){
        Scanner scanner = new Scanner(System.in);
        notificationHandler.printPrompt();
        while (true){
            System.out.println("Do you want to resign? Type yes or no");
            String line = scanner.nextLine();
            if (line.equals("yes")){
                return true;
            }
            else if(line.equals("no")){
                return false;
            }
        }
    }

    public String resign(String... params) throws ResponseException {
        if (params.length == 0) {
            if (resignGood()) {
                ws.resign(authToken, gameData.gameID());
                return String.format("Thanks for playing, you will get them next time!");
            }
        }
        throw new ResponseException(400, "Expected no parameters");
    }

    public String highlight(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                char colLetter = params[0].charAt(0);
                int row = Integer.parseInt(params[0].substring(1));
                int col;
                if (color == ChessGame.TeamColor.WHITE) {
                    col = colLetter - 'a' + 1;
                }
                else {
                    col = 9 - (colLetter - 'a' + 1);
                }
                ChessPosition start = new ChessPosition(row, col);
                ChessPiece myPiece = gameData.game().getBoard().getPiece(start);
                Collection<ChessMove> posMoves = myPiece.pieceMoves(gameData.game().getBoard(), start);
                ui.ChessBoard draw = new ui.ChessBoard();
                if (color == ChessGame.TeamColor.WHITE) {
                    draw.drawWhite(gameData.game().getBoard(), posMoves);
                } else {
                    draw.drawBlack(gameData.game().getBoard(), posMoves);
                }
                return " ";
            } catch (Exception e) {
                throw new ResponseException(400, "Incorrect square notation, try again");
            }
        }
        throw new ResponseException(400, "Expected <Square>");
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
