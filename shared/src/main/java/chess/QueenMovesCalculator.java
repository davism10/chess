package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    QueenMovesCalculator(){}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }
}
