package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator{
    public void pawnAdd(ChessPosition myPosition, ChessPosition newPosition, Collection<ChessMove> moves, int end){
        if (newPosition.getRow() == end){
            List<ChessPiece.PieceType> possible;
            possible = Arrays.asList(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN);
            for (ChessPiece.PieceType pieceType : possible) {
                moves.add(new ChessMove(myPosition, newPosition, pieceType));
            }
        }
        else {
            moves.add(new ChessMove(myPosition, newPosition, null));
        }

    }

    @Override
    public void searchMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, int rowAdd, int colAdd) {
        ChessPosition newPosition;
        int end;

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            end = 8;
        } else {
            end = 1;
        }

        newPosition = new ChessPosition(myPosition.getRow() + rowAdd, myPosition.getColumn() + colAdd);
        if (inBounds(newPosition) && board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                pawnAdd(myPosition, newPosition, moves, end);
            }
        }
    }

    public Collection<ChessMove> generate(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition newPosition;
        int rowAdd;
        int start;
        int end;

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            rowAdd = 1;
            start = 2;
            end = 8;
        }
        else {
            rowAdd = -1;
            start = 7;
            end = 1;
        }

        newPosition = new ChessPosition(myPosition.getRow() + 2*rowAdd, myPosition.getColumn());
        if (myPosition.getRow() == start && board.getPiece(newPosition) == null) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + rowAdd, myPosition.getColumn())) == null) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        newPosition = new ChessPosition(myPosition.getRow() + rowAdd, myPosition.getColumn());
        if (inBounds(newPosition) && board.getPiece(newPosition) == null){
            pawnAdd(myPosition, newPosition, moves, end);
        }

        searchMoves(board, myPosition, moves, rowAdd, 1);
        searchMoves(board, myPosition, moves, rowAdd, -1);


        return moves;
    }
}

