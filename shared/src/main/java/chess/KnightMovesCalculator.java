package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    KnightMovesCalculator() {}

    private boolean can_add(int row, int column){
        System.out.println("row " + row + "\n column " + column);
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

    private void move(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int row_change, int column_change){
        if (can_add(myPosition.getRow() + row_change, myPosition.getColumn() + column_change)){
            ChessPosition new_position = new ChessPosition(myPosition.getRow() + row_change, myPosition.getColumn() + column_change);
            if (blocked(board, myPosition, new_position)){
                ChessMove final_move = new ChessMove(myPosition, new_position, null);
                moves.add(final_move);
            }
        }
    }

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();


//        up 2 left 1
        move(board, myPosition, moves, 2, -1);

//        up 1 left 2
        move(board, myPosition, moves, 1, -2);

//        up 2 right 1
        move(board, myPosition, moves, 2, 1);

//        up 1 right 2
        move(board, myPosition, moves, 1, 2);

//        down 2 left 1
        move(board, myPosition, moves, -2, -1);

//        down 1 left 2
        move(board, myPosition, moves, -1, -2);

//        down 2 right 1
        move(board, myPosition, moves, -2, 1);

//        down 1 right 2
        move(board, myPosition, moves, -1, 2);

        return moves;
    }
}
