package ui;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoard {
    private static final String EMPTY = "   ";
    private static final String WHITE_KING = EscapeSequences.WHITE_KING;
    private static final String WHITE_QUEEN = EscapeSequences.WHITE_QUEEN;
    private static final String WHITE_ROOK = EscapeSequences.WHITE_ROOK;
    private static final String WHITE_BISHOP = EscapeSequences.WHITE_BISHOP;
    private static final String WHITE_KNIGHT = EscapeSequences.WHITE_KNIGHT;
    private static final String WHITE_PAWN = EscapeSequences.WHITE_PAWN;

    private static final String BLACK_KING = EscapeSequences.BLACK_KING;
    private static final String BLACK_QUEEN = EscapeSequences.BLACK_QUEEN;
    private static final String BLACK_ROOK = EscapeSequences.BLACK_ROOK;
    private static final String BLACK_BISHOP = EscapeSequences.BLACK_BISHOP;
    private static final String BLACK_KNIGHT = EscapeSequences.BLACK_KNIGHT;
    private static final String BLACK_PAWN = EscapeSequences.BLACK_PAWN;

    // Maps for White & Black pieces
    private static final Map<ChessPiece.PieceType, String> WHITE_MAP = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> BLACK_MAP = new HashMap<>();

    static {
        // Populate White pieces map
        WHITE_MAP.put(ChessPiece.PieceType.KING, WHITE_KING);
        WHITE_MAP.put(ChessPiece.PieceType.QUEEN, WHITE_QUEEN);
        WHITE_MAP.put(ChessPiece.PieceType.ROOK, WHITE_ROOK);
        WHITE_MAP.put(ChessPiece.PieceType.BISHOP, WHITE_BISHOP);
        WHITE_MAP.put(ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT);
        WHITE_MAP.put(ChessPiece.PieceType.PAWN, WHITE_PAWN);
        WHITE_MAP.put(null, EMPTY);

        // Populate Black pieces map
        BLACK_MAP.put(ChessPiece.PieceType.KING, BLACK_KING);
        BLACK_MAP.put(ChessPiece.PieceType.QUEEN, BLACK_QUEEN);
        BLACK_MAP.put(ChessPiece.PieceType.ROOK, BLACK_ROOK);
        BLACK_MAP.put(ChessPiece.PieceType.BISHOP, BLACK_BISHOP);
        BLACK_MAP.put(ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT);
        BLACK_MAP.put(ChessPiece.PieceType.PAWN, BLACK_PAWN);
        BLACK_MAP.put(null, EMPTY);
    }

    private static final String[] HEADERS = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    private static final String[] HEADERS_REVERSED = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};


    public static void drawWhite(chess.ChessBoard board, Collection<ChessMove> moves) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.println();

        drawHeaders(out, HEADERS);
        resetColor(out);

        drawChessBoard(out, board, moves);

        drawHeaders(out, HEADERS);
        resetColor(out);
        out.println();
    }

    public static void drawBlack(chess.ChessBoard board, Collection<ChessMove> moves) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.println();

        drawHeaders(out, HEADERS_REVERSED);
        resetColor(out);

        drawChessBoardReversed(out, board, moves);

        drawHeaders(out, HEADERS_REVERSED);
        resetColor(out);
        out.println();
    }

    private static void resetColor(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out, String[] headers) {

        for (int boardCol = 0; boardCol < 10; ++boardCol) {
            printHeaderText(out, headers[boardCol]);
        }
        resetColor(out);

        out.println();
    }


    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_PINK);

        out.print(player);
    }

    private static void setColor(PrintStream out, Integer whatTurn) {
        if (whatTurn == 0) {
//            out.print(SET_BG_COLOR_ROSE);
            out.print(SET_BG_COLOR_LIGHT_PINK);
        }
        else if (whatTurn == 1){
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
        else if (whatTurn == 2){
            out.print(SET_BG_COLOR_LAVENDER);
        }
    }

    private static boolean switchTurn(boolean whatTurn) {
        return !whatTurn;
    }

    private static boolean inMoves(ChessPosition myPosition, Collection<ChessMove> moves){
        if (moves == null){
            return false;
        }
        for (ChessMove move: moves){
            if (myPosition.equals(move.getEndPosition())){
                return true;
            }
        }
        return false;
    }


    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard, Collection<ChessMove> moves) {
        boolean turn = true;
//        ChessPiece current;
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            for (int squareRow = 0; squareRow < 10; ++squareRow) {
                turn = isTurn(out, chessBoard, turn, boardRow, squareRow, moves);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

    private static boolean isTurn(PrintStream out, chess.ChessBoard chessBoard, boolean turn, int boardRow, int squareRow, Collection<ChessMove> moves) {
        ChessPiece current;
        if (squareRow == 0 || squareRow == 9) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_LIGHT_PINK);
            out.print(" " + (boardRow + 1) + " ");
        } else {
            if (inMoves(new ChessPosition(boardRow + 1, squareRow), moves)){
                setColor(out, 2);
            }
            else {
                if (turn){
                    setColor(out, 0);
                }
                else {
                    setColor(out, 1);
                }
            }
            turn = switchTurn(turn);
            current = chessBoard.getPiece(new ChessPosition(boardRow + 1, squareRow));
            if (current == null) {
                out.print(EMPTY);
            } else if (current.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(WHITE_MAP.get(current.getPieceType()));
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(BLACK_MAP.get(current.getPieceType()));
            }
        }
        resetColor(out);
        return turn;
    }

    private static void drawChessBoardReversed(PrintStream out, chess.ChessBoard chessBoard, Collection<ChessMove> moves) {
        boolean turn = true;
//        ChessPiece current;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareRow = 9; squareRow > -1; --squareRow) {
                turn = isTurn(out, chessBoard, turn, boardRow, squareRow, moves);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

}
