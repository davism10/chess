package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    default boolean inBounds(ChessPosition myPosition){
            return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }
    default void searchMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowAdd, int colAdd){
        ChessPosition newPosition;

        newPosition = new ChessPosition(myPosition.getRow() + rowAdd, myPosition.getColumn() + colAdd);
        if (inBounds(newPosition) && board.getPiece(newPosition) == null){
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
        else if (inBounds(newPosition) && board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }
    Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition);
}
