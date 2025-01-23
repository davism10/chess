package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    KingMovesCalculator() {}


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

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition newPosition;

//        check left
        if (canAdd(myPosition.getRow(), myPosition.getColumn()-1)){
            newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        upper left diag
        if (canAdd(myPosition.getRow()+1, myPosition.getColumn()-1)){
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        up
        if (canAdd(myPosition.getRow()+1, myPosition.getColumn())){
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        upper right
        if (canAdd(myPosition.getRow()+1, myPosition.getColumn()+1)){
            newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        right
        if (canAdd(myPosition.getRow(), myPosition.getColumn()+1)){
            newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        lower right diag
        if (canAdd(myPosition.getRow()-1, myPosition.getColumn()+1)){
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        down
        if (canAdd(myPosition.getRow()-1, myPosition.getColumn())){
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

//        lower left diag
        if (canAdd(myPosition.getRow()-1, myPosition.getColumn()-1)){
            newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            if (blocked(board, myPosition, newPosition)) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }

        return moves;
    }
}
