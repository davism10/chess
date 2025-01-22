package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    PawnMovesCalculator() {}

    private boolean can_add(int row, int column){
        System.out.println("row " + row + "\n column " + column);
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition new_position;

//        if first move
        if (myPosition.getRow() == 1 || myPosition.getRow() == 6) {
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
                if (can_add(myPosition.getRow() + 2, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    if (board.getPiece(new_position) == null){
                        moves.add(new ChessMove(myPosition, new_position, null));}
                }
            }
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
                if (can_add(myPosition.getRow() - 2, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    if (board.getPiece(new_position) == null){
                        moves.add(new ChessMove(myPosition, new_position, null));}
                }
            }
        }

//        basic move
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
                if (can_add(myPosition.getRow() + 1, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(new_position) == null){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 7){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));}
                }
            }
            if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
                if (can_add(myPosition.getRow() - 1, myPosition.getColumn())) {
                    new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(new_position) == null){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 0){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));}
                }
                }

//        check if we can capture a piece
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if (can_add(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
                new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(new_position) != null){
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 7){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));
                    }
                }
            }

            if (can_add(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {
                new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(new_position) != null){
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 7){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));
                    }
                }
            }
        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            if (can_add(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {
                new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(new_position) != null){
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 0){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));
                    }
                }
            }

            if (can_add(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
                new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(new_position) != null){
                    if (board.getPiece(new_position).getTeamColor() == ChessGame.TeamColor.BLACK){
                        ChessPiece.PieceType promotion;
                        if (myPosition.getRow() == 0){
                            promotion = ChessPiece.PieceType.ROOK;
                        }
                        else {
                            promotion = null;
                        }
                        moves.add(new ChessMove(myPosition, new_position, promotion));
                    }
                }
            }
        }

        return moves;
        }

    }

