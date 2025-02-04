package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    public boolean inBounds(ChessPosition myPosition){
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }

    public void searchMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowAdd, int colAdd){
    }

    public Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessMove> bishopMoves = new BishopMovesCalculator().generate(board, myPosition);
        Collection<ChessMove> rookMoves = new RookMovesCalculator().generate(board, myPosition);

        moves.addAll(bishopMoves);
        moves.addAll(rookMoves);

        return moves;
    }
}

