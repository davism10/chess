package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessMove> bishopMoves = new BishopMovesCalculator().generate(board, myPosition);
        Collection<ChessMove> rookMoves = new RookMovesCalculator().generate(board, myPosition);

        moves.addAll(bishopMoves);
        moves.addAll(rookMoves);

        return moves;
    }
}

