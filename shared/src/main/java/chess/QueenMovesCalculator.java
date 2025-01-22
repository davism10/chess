package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    QueenMovesCalculator(){}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        Collection<ChessMove> rook_moves = new RookMovesCalculator().calculateMoves(board, myPosition);
        Collection<ChessMove> bishop_moves = new BishopMovesCalculator().calculateMoves(board, myPosition);
        moves.addAll(bishop_moves);
        moves.addAll(rook_moves);
        return moves;
    }
}
