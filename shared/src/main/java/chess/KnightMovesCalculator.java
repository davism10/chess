package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public boolean inBounds(ChessPosition myPosition){
        return myPosition.getRow() >= 1 && myPosition.getRow() <= 8 && myPosition.getColumn() >= 1 && myPosition.getColumn() <= 8;
    }

    public void searchMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowAdd, int colAdd){
        ChessPosition newPosition;

        newPosition = new ChessPosition(myPosition.getRow() + rowAdd, myPosition.getColumn() + colAdd);
        if (inBounds(newPosition) && board.getPiece(newPosition) == null){
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
        else if (inBounds(newPosition) && board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            moves.add(new ChessMove(myPosition, newPosition, null));
        }
    }

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

