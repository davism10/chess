package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator{
    PawnMovesCalculator() {}

    private boolean can_add(int row, int column){
        return row >= 1 && row <= 8 && column >= 1 && column <= 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition new_position;

//        white
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
//            first move
            if (myPosition.getRow() == 2) {
                if (can_add(myPosition.getRow() + 2, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    ChessPosition MiddlePosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(new_position) == null && board.getPiece(MiddlePosition) == null){
                        moves.add(new ChessMove(myPosition, new_position, null));
                    }
                }
            }

//            basic move
            if (can_add(myPosition.getRow() + 1, myPosition.getColumn())) {
                new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(new_position) == null){
                    if_promote_white(board, myPosition, moves, new_position);}
            }

//            try to capture
            if (can_add(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
                new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if_promote_white(board, myPosition, moves, new_position);
                    }
                }
            }

            if (can_add(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {
                new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if_promote_white(board, myPosition, moves, new_position);
                    }
                }
            }
        }

//        black
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
//            first move
            if (myPosition.getRow() == 7) {
                    new_position = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    ChessPosition MiddlePosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(new_position) == null && board.getPiece(MiddlePosition) == null){
                        moves.add(new ChessMove(myPosition, new_position, null));}
            }

//            basic move
                if (can_add(myPosition.getRow() - 1, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(new_position) == null){
                        if_promote_black(board, myPosition, moves, new_position);}
                }
            }

//            try and capture
            if (can_add(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {
                new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if_promote_black(board, myPosition, moves, new_position);
                    }
                }
            }

            if (can_add(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
                new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(new_position) != null) {
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if_promote_black(board, myPosition, moves, new_position);
                    }
                }
            }

        return moves;
        }

    private void if_promote_black(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition new_position) {
                if (myPosition.getRow() == 2){
                    List<ChessPiece.PieceType> possible_pieces = List.of(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN);
                    for (ChessPiece.PieceType possible_piece : possible_pieces){
                        moves.add(new ChessMove(myPosition, new_position, possible_piece));
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new_position, null));
                }


    }

    private void if_promote_white(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition new_position) {
                if (myPosition.getRow() == 7){
                    List<ChessPiece.PieceType> possible_pieces = List.of(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN);
                    for (ChessPiece.PieceType possible_piece : possible_pieces){
                        moves.add(new ChessMove(myPosition, new_position, possible_piece));
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, new_position, null));
                }
    }

}

