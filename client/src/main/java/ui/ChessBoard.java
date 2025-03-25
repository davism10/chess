package ui;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessBoard {
    private static final String EMPTY = "   ";
    private static final String WHITEKING = WHITE_KING;
    private static final String WHITEQUEEN = WHITE_QUEEN;
    private static final String WHITEROOK = WHITE_ROOK;
    private static final String WHITEBISHOP = WHITE_BISHOP;
    private static final String WHITEKNIGHT = WHITE_KNIGHT;
    private static final String WHITEPAWN = WHITE_PAWN;

    private static final String BLACKKING = BLACK_KING;
    private static final String BLACKQUEEN = BLACK_QUEEN;
    private static final String BLACKROOK = BLACK_ROOK;
    private static final String BLACKBISHOP = BLACK_BISHOP;
    private static final String BLACKKNIGHT = BLACK_KNIGHT;
    private static final String BLACKPAWN = BLACK_PAWN;

    // Maps for White & Black pieces
    private static final Map<ChessPiece.PieceType, String> WHITEMAP = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> BLACKMAP = new HashMap<>();

    static {
        // Populate White pieces map
        WHITEMAP.put(ChessPiece.PieceType.KING, WHITEKING);
        WHITEMAP.put(ChessPiece.PieceType.QUEEN, WHITEQUEEN);
        WHITEMAP.put(ChessPiece.PieceType.ROOK, WHITEROOK);
        WHITEMAP.put(ChessPiece.PieceType.BISHOP, WHITEBISHOP);
        WHITEMAP.put(ChessPiece.PieceType.KNIGHT, WHITEKNIGHT);
        WHITEMAP.put(ChessPiece.PieceType.PAWN, WHITEPAWN);
        WHITEMAP.put(null, EMPTY);

        // Populate Black pieces map
        BLACKMAP.put(ChessPiece.PieceType.KING, BLACKKING);
        BLACKMAP.put(ChessPiece.PieceType.QUEEN, BLACKQUEEN);
        BLACKMAP.put(ChessPiece.PieceType.ROOK, BLACKROOK);
        BLACKMAP.put(ChessPiece.PieceType.BISHOP, BLACKBISHOP);
        BLACKMAP.put(ChessPiece.PieceType.KNIGHT, BLACKKNIGHT);
        BLACKMAP.put(ChessPiece.PieceType.PAWN, BLACKPAWN);
        BLACKMAP.put(null, EMPTY);
    }

    private static final String[] Headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    private static final String[] HeadersReversed = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};


    public static void drawWhite(chess.ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, Headers);
        resetColor(out);

        drawChessBoard(out, board);

        drawHeaders(out, Headers);
        resetColor(out);
    }

    public static void drawBlack(chess.ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, HeadersReversed);
        resetColor(out);

        drawChessBoardReversed(out, board);

        drawHeaders(out, HeadersReversed);
        resetColor(out);
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

    private static void setColor(PrintStream out, Boolean whatTurn) {
        if (whatTurn) {
            out.print(SET_BG_COLOR_ROSE);
        } else {
            out.print(SET_BG_COLOR_DARK_GREEN);
        }
    }

    private static boolean switchTurn(boolean whatTurn) {
        return !whatTurn;
    }


    private static void drawChessBoard(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
//        ChessPiece current;
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            for (int squareRow = 9; squareRow > -1; --squareRow) {
                turn = isTurn(out, chessBoard, turn, boardRow, squareRow);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

    private static boolean isTurn(PrintStream out, chess.ChessBoard chessBoard, boolean turn, int boardRow, int squareRow) {
        ChessPiece current;
        if (squareRow == 0 || squareRow == 9) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_LIGHT_PINK);
            out.print(" " + (8 - boardRow) + " ");
        } else {
            setColor(out, turn);
            turn = switchTurn(turn);
            current = chessBoard.getPiece(new ChessPosition(boardRow + 1, squareRow));
            if (current == null) {
                out.print(EMPTY);
            } else if (current.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_WHITE);
                out.print(WHITEMAP.get(current.getPieceType()));
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(BLACKMAP.get(current.getPieceType()));
            }
        }
        resetColor(out);
        return turn;
    }

    private static void drawChessBoardReversed(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
//        ChessPiece current;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareRow = 0; squareRow < 10; ++squareRow) {
                turn = isTurn(out, chessBoard, turn, boardRow, squareRow);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

}
