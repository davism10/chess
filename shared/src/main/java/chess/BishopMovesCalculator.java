package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    BishopMovesCalculator(){}

    private boolean in_bounds(ChessPosition myPosition){
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition new_position;

//        First search the upper right diagonal
        new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (in_bounds(new_position)) {
            while (in_bounds(new_position) && board.getPiece(new_position) == null) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() + 1);

            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the upper left diagonal
        new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (in_bounds(new_position)) {
            while (in_bounds(new_position) && board.getPiece(new_position) == null) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() + 1);
            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the lower right diagonal
        new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (in_bounds(new_position)) {
            while (in_bounds(new_position) && board.getPiece(new_position) == null) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() - 1);
            }
            capture_piece(board, myPosition, moves, new_position);
        }


//        Next we search the lower left diagonal
        new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (in_bounds(new_position)) {
            while (in_bounds(new_position) && board.getPiece(new_position) == null) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
                new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() - 1);
            }
            capture_piece(board, myPosition, moves, new_position);
        }


        return moves;
    }

    private void capture_piece(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition new_position) {
        if (in_bounds(new_position) && board.getPiece(new_position) != null) {
            if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }
    }
}
