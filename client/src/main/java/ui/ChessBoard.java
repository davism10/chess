package ui;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ui.EscapeSequences.*;


public class ChessBoard {
    private static final String EMPTY = "   ";
    private static final String whiteKing = WHITE_KING;
    private static final String whiteQueen = WHITE_QUEEN;
    private static final String whiteRook = WHITE_ROOK;
    private static final String whiteBishop = WHITE_BISHOP;
    private static final String whiteKnight = WHITE_KNIGHT;
    private static final String whitePawn = WHITE_PAWN;

    private static final String blackKing = BLACK_KING;
    private static final String blackQueen = BLACK_QUEEN;
    private static final String blackRook = BLACK_ROOK;
    private static final String blackBishop = BLACK_BISHOP;
    private static final String blackKnight = BLACK_KNIGHT;
    private static final String blackPawn = BLACK_PAWN;

    // Maps for White & Black pieces
    private static final Map<ChessPiece.PieceType, String> whiteMap = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> blackMap = new HashMap<>();

    static {
        // Populate White pieces map
        whiteMap.put(ChessPiece.PieceType.KING, whiteKing);
        whiteMap.put(ChessPiece.PieceType.QUEEN, whiteQueen);
        whiteMap.put(ChessPiece.PieceType.ROOK, whiteRook);
        whiteMap.put(ChessPiece.PieceType.BISHOP, whiteBishop);
        whiteMap.put(ChessPiece.PieceType.KNIGHT, whiteKnight);
        whiteMap.put(ChessPiece.PieceType.PAWN, whitePawn);
        whiteMap.put(null, EMPTY);

        // Populate Black pieces map
        blackMap.put(ChessPiece.PieceType.KING, blackKing);
        blackMap.put(ChessPiece.PieceType.QUEEN, blackQueen);
        blackMap.put(ChessPiece.PieceType.ROOK, blackRook);
        blackMap.put(ChessPiece.PieceType.BISHOP, blackBishop);
        blackMap.put(ChessPiece.PieceType.KNIGHT, blackKnight);
        blackMap.put(ChessPiece.PieceType.PAWN, blackPawn);
        blackMap.put(null, EMPTY);
    }

    private static final String[] headers = { " ", "a", "b", "c", "d", "e", "f", "g", "h", " " };
    private static final String[] headersReversed = { " ", "h", "g", "f", "e", "d", "c", "b", "a", " " };



    public static void drawWhite(chess.ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, headers);

        drawChessBoard(out, board);
    }

    public static void drawBlack(chess.ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, headersReversed);

        drawChessBoardReverse(out, board);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {

        for (int boardCol = 0; boardCol < 10; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }

        out.println();
    }


    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_PINK);

        out.print(player);
    }

    private static void setColor(PrintStream out, Boolean whatTurn){
        if (whatTurn){
            out.print(SET_BG_COLOR_PINK);
        }
        else {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
    }

    private static boolean switchTurn(boolean whatTurn){
        return !whatTurn;
    }

    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
        ChessPiece current;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareRow = 0; squareRow < 10; ++squareRow){
                if (squareRow == 0 || squareRow == 9){
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_LIGHT_PINK);
                    out.print(8 - boardRow);
                }
                else {
                    setColor(out, turn);
                    turn = switchTurn(turn);
                    current = chessBoard.getPiece(new ChessPosition(boardRow, squareRow));
                    if (current.getTeamColor() == ChessGame.TeamColor.WHITE){
                        out.print(whiteMap.get(current.getPieceType()));
                    }
                    else {
                        out.print(blackMap.get(current.getPieceType()));
                    }
                }
            }
        }
    }

    private static void drawChessBoardReversed(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
        ChessPiece current;
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            for (int squareRow = 9; squareRow > -1; --squareRow){
                if (squareRow == 0 || squareRow == 9){
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_LIGHT_PINK);
                    out.print(8 - boardRow);
                }
                else {
                    setColor(out, turn);
                    turn = switchTurn(turn);
                    current = chessBoard.getPiece(new ChessPosition(boardRow, squareRow));
                    if (current.getTeamColor() == ChessGame.TeamColor.WHITE){
                        out.print(whiteMap.get(current.getPieceType()));
                    }
                    else {
                        out.print(blackMap.get(current.getPieceType()));
                    }
                }
            }
        }
    }

}
