package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    BishopMovesCalculator(){}

    private boolean in_bounds(ChessPosition myPosition){
        return myPosition.getRow() >= 0 && myPosition.getRow() < 8 && myPosition.getColumn() >= 0 && myPosition.getColumn() < 8;
    }

    private boolean can_add(int row, int column){
        System.out.println("row " + row + "\n column " + column);
        return row > 0 && row < 8 && column > 0 && column < 8;
    }

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition new_position;

//        First search the upper right diagonal
        System.out.println("upper right");
        if (can_add(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
            new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            while (board.getPiece(new_position) == null && in_bounds(new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                System.out.println(final_move);
                if (can_add(new_position.getRow() + 1, new_position.getColumn() + 1)) {
                    new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() + 1);
                }
                else {
                    break;
                }
            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the upper left diagonal
        System.out.println("upper left");
        if (can_add(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {
            new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            while (board.getPiece(new_position) == null && in_bounds(new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                System.out.println(final_move);
                if (can_add(new_position.getRow() - 1, new_position.getColumn() + 1)) {
                    new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() + 1);
                }
                else {
                    break;
                }
            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the lower right diagonal
        System.out.println("lower right");
        if (can_add(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {
            new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            while (board.getPiece(new_position) == null && in_bounds(new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                System.out.println(final_move);
                if (can_add(new_position.getRow() + 1, new_position.getColumn() - 1)) {
                    new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() - 1);
                }
                else {
                    break;
                }
            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the lower left diagonal
        System.out.println("lower left");
        if (can_add(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
            new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            while (board.getPiece(new_position) == null && in_bounds(new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                System.out.println(final_move);
                if (can_add(new_position.getRow() - 1, new_position.getColumn() - 1)) {
                    new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() - 1);
                }
                else {
                    break;
                }
            }
            capture_piece(board, myPosition, moves, new_position);
        }


        return moves;
    }

    private void capture_piece(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition new_position) {
        if (board.getPiece(new_position) != null) {
            System.out.println("capture piece");
            System.out.println(board.getPiece(new_position));
            if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                System.out.println(final_move);
            }
        }
        else {
            System.out.println("PROBLEM");
        }
    }
}
