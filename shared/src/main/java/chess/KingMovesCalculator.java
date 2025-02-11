package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{

    public Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

        searchMoves(board, myPosition, moves, 1, 1);
        searchMoves(board, myPosition, moves, -1, 1);
        searchMoves(board, myPosition, moves, -1, -1);
        searchMoves(board, myPosition, moves, 1, -1);

        searchMoves(board, myPosition, moves, 1, 0);
        searchMoves(board, myPosition, moves, -1, 0);
        searchMoves(board, myPosition, moves, 0, -1);
        searchMoves(board, myPosition, moves, 0, 1);

        return moves;
    }
}

