package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    BishopMovesCalculator(){}

    private boolean inBounds(ChessPosition myPosition){
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition newPosition;

//        First search the upper right diagonal
        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() + 1);

            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search the upper left diagonal
        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() - 1, newPosition.getColumn() + 1);
            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search the lower right diagonal
        newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() + 1, newPosition.getColumn() - 1);
            }
            capturePiece(board, myPosition, moves, newPosition);
        }


//        Next we search the lower left diagonal
        newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (inBounds(newPosition)) {
            while (inBounds(newPosition) && board.getPiece(newPosition) == null) {
                ChessMove finalMove = new ChessMove(myPosition, newPosition, null);
                moves.add(finalMove);
                newPosition = new ChessPosition(newPosition.getRow() - 1, newPosition.getColumn() - 1);
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
