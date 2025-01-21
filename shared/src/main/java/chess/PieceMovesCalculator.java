package chess;

import java.util.Collection;

/**
 * Helper interface for ChessPiece
 * Includes a subclass for each type of chess piece
 */
public interface PieceMovesCalculator {

    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition);

}