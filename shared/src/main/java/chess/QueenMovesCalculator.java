package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    QueenMovesCalculator(){}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessMove> rookMoves = new RookMovesCalculator().calculateMoves(board, myPosition);
        Collection<ChessMove> bishopMoves = new BishopMovesCalculator().calculateMoves(board, myPosition);
        moves.addAll(bishopMoves);
        moves.addAll(rookMoves);
        return moves;
    }
}
