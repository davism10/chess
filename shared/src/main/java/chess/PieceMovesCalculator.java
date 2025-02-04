package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    boolean inBounds(ChessPosition myPosition);
    void searchMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowAdd, int colAdd);
    Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition);
}
