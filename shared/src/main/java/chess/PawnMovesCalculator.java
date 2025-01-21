package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    PawnMovesCalculator() {}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }
}
