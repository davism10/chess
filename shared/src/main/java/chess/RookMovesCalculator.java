package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    RookMovesCalculator() {}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }
}
