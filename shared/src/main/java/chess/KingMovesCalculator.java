package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    KingMovesCalculator() {}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }
}
