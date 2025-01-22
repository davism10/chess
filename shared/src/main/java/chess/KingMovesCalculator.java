package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    KingMovesCalculator() {}


    private boolean can_add(int row, int column){
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    private boolean blocked(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition){
        if (board.getPiece(newPosition) != null){
            return board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor();
        }
        else{
            return true;
        }
    }

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition new_position;

//        check left
        if (can_add(myPosition.getRow(), myPosition.getColumn()-1)){
            new_position = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        upper left diag
        if (can_add(myPosition.getRow()+1, myPosition.getColumn()-1)){
            new_position = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        up
        if (can_add(myPosition.getRow()+1, myPosition.getColumn())){
            new_position = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        upper right
        if (can_add(myPosition.getRow()+1, myPosition.getColumn()+1)){
            new_position = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        right
        if (can_add(myPosition.getRow(), myPosition.getColumn()+1)){
            new_position = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        lower right diag
        if (can_add(myPosition.getRow()-1, myPosition.getColumn()+1)){
            new_position = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        down
        if (can_add(myPosition.getRow()-1, myPosition.getColumn())){
            new_position = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

//        lower left diag
        if (can_add(myPosition.getRow()-1, myPosition.getColumn()-1)){
            new_position = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            if (blocked(board, myPosition, new_position)) {
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }

        return moves;
    }
}
