package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    RookMovesCalculator() {}

    private boolean inBounds(ChessPosition myPosition){
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition newPosition;

//        First search the right
        newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn() + 1);

            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search the left
        newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn() - 1);
            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search up
        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn());
            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search down
        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() - 1, newPosition.getColumn());
            }
            capturePiece(board, myPosition, moves, newPosition);
        }


        return moves;
    }

    private void capturePiece(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition newPosition) {
        if (inBounds(newPosition) && board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
            }
        }
    }
}