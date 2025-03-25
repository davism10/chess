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
    private static final String WhiteKing = WHITE_KING;
    private static final String WhiteQueen = WHITE_QUEEN;
    private static final String WhiteRook = WHITE_ROOK;
    private static final String WhiteBishop = WHITE_BISHOP;
    private static final String WhiteKnight = WHITE_KNIGHT;
    private static final String WhitePawn = WHITE_PAWN;

    private static final String BlackKing = BLACK_KING;
    private static final String BlackQueen = BLACK_QUEEN;
    private static final String BlackRook = BLACK_ROOK;
    private static final String BlackBishop = BLACK_BISHOP;
    private static final String BlackKnight = BLACK_KNIGHT;
    private static final String BlackPawn = BLACK_PAWN;

    // Maps for White & Black pieces
    private static final Map<ChessPiece.PieceType, String> WhiteMap = new HashMap<>();
    private static final Map<ChessPiece.PieceType, String> BlackMap = new HashMap<>();

    static {
        // Populate White pieces map
        WhiteMap.put(ChessPiece.PieceType.KING, WhiteKing);
        WhiteMap.put(ChessPiece.PieceType.QUEEN, WhiteQueen);
        WhiteMap.put(ChessPiece.PieceType.ROOK, WhiteRook);
        WhiteMap.put(ChessPiece.PieceType.BISHOP, WhiteBishop);
        WhiteMap.put(ChessPiece.PieceType.KNIGHT, WhiteKnight);
        WhiteMap.put(ChessPiece.PieceType.PAWN, WhitePawn);
        WhiteMap.put(null, EMPTY);

        // Populate Black pieces map
        BlackMap.put(ChessPiece.PieceType.KING, BlackKing);
        BlackMap.put(ChessPiece.PieceType.QUEEN, BlackQueen);
        BlackMap.put(ChessPiece.PieceType.ROOK, BlackRook);
        BlackMap.put(ChessPiece.PieceType.BISHOP, BlackBishop);
        BlackMap.put(ChessPiece.PieceType.KNIGHT, BlackKnight);
        BlackMap.put(ChessPiece.PieceType.PAWN, BlackPawn);
        BlackMap.put(null, EMPTY);
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
        ChessPiece current;
        for (int boardRow = 7; boardRow > -1; --boardRow) {
            for (int squareRow = 9; squareRow > -1; --squareRow) {
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
                        out.print(WhiteMap.get(current.getPieceType()));
                    } else {
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.print(BlackMap.get(current.getPieceType()));
                    }
                }
                resetColor(out);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

    private static void drawChessBoardReversed(PrintStream out, chess.ChessBoard chessBoard) {
        boolean turn = true;
        ChessPiece current;
        for (int boardRow = 0; boardRow < 8; ++boardRow) {
            for (int squareRow = 0; squareRow < 10; ++squareRow) {
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
                    }
                    else if (current.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(SET_TEXT_COLOR_WHITE);
                        out.print(WhiteMap.get(current.getPieceType()));
                    } else {
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.print(BlackMap.get(current.getPieceType()));
                    }
                }
                resetColor(out);
            }
            out.println();
            turn = switchTurn(turn);
        }
    }

}
