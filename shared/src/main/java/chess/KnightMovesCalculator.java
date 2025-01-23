package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    KnightMovesCalculator() {}

    private boolean canAdd(int row, int column){
        return row >= 1 && row <= 8 && column >= 1 && column <= 8;
    }

    private boolean blocked(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition){
        if (board.getPiece(newPosition) != null){
            return board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor();
        }
        else{
            return true;
        }
    }

    private void move(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowChange, int columnChange){
        if (canAdd(myPosition.getRow() + rowChange, myPosition.getColumn() + columnChange)){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + rowChange, myPosition.getColumn() + columnChange);
            if (blocked(board, myPosition, newPosition)){
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
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
