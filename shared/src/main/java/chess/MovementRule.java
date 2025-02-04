package chess;

import java.util.Collection;

public interface MovementRule {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition);
}
