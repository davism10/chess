package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    BishopMovesCalculator(){}

    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();

//        First search the upper right diagonal
        ChessPosition new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        while (board.getPiece(new_position) != null && new_position.getRow() >= 8 && new_position.getColumn() >= 8){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
            new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() + 1);
        }
        if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
        }


//        Next we search the upper left diagonal
        new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        while (board.getPiece(new_position) != null && new_position.getRow() >= 8 && new_position.getColumn() >= 8){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
            new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() + 1);
        }
        if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
        }


//        Next we search the lower right diagonal
        new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        while (board.getPiece(new_position) != null && new_position.getRow() >= 8 && new_position.getColumn() >= 8){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
            new_position = new ChessPosition(new_position.getRow() + 1, new_position.getColumn() - 1);
        }
        if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
        }


//        Next we search the lower left diagonal
        new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        while (board.getPiece(new_position) != null && new_position.getRow() >= 8 && new_position.getColumn() >= 8){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
            new_position = new ChessPosition(new_position.getRow() - 1, new_position.getColumn() - 1);
        }
        if (board.getPiece(new_position).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ChessMove final_move = new ChessMove(myPosition, new_position, null);
            moves.add(final_move);
        }


        return moves;
    }
}
