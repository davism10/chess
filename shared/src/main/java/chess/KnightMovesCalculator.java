package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        searchMoves(board, myPosition, moves, 2, 1);
        searchMoves(board, myPosition, moves, 1, 2);
        searchMoves(board, myPosition, moves, -2, 1);
        searchMoves(board, myPosition, moves, -1, 2);

        searchMoves(board, myPosition, moves, 2, -1);
        searchMoves(board, myPosition, moves, 1, -2);
        searchMoves(board, myPosition, moves, -2, -1);
        searchMoves(board, myPosition, moves, -1, -2);

        return moves;
    }
}

