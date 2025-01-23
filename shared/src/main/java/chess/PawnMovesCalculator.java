package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator{
    PawnMovesCalculator() {}

    private boolean canAdd(int row, int column){
        return row >= 1 && row <= 8 && column >= 1 && column <= 8;
    }


    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition newPosition;

//        white
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
//            first move
            if (myPosition.getRow() == 2) {
                if (canAdd(myPosition.getRow() + 2, myPosition.getColumn())) {
                    newPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                    ChessPosition MiddlePosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    if (board.getPiece(newPosition) == null && board.getPiece(MiddlePosition) == null){
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }

//            basic move
            if (canAdd(myPosition.getRow() + 1, myPosition.getColumn())) {
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                if (board.getPiece(newPosition) == null){
                    ifPromoteWhite(myPosition, moves, newPosition);}
            }

//            try to capture
            if (canAdd(myPosition.getRow() + 1, myPosition.getColumn() + 1)) {
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        ifPromoteWhite(myPosition, moves, newPosition);
                    }
                }
            }

            if (canAdd(myPosition.getRow() + 1, myPosition.getColumn() - 1)) {
                newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        ifPromoteWhite(myPosition, moves, newPosition);
                    }
                }
            }
        }

//        black
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
//            first move
            if (myPosition.getRow() == 7) {
                    newPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                    ChessPosition MiddlePosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(newPosition) == null && board.getPiece(MiddlePosition) == null){
                        moves.add(new ChessMove(myPosition, newPosition, null));}
            }

//            basic move
                if (canAdd(myPosition.getRow() - 1, myPosition.getColumn())) {
                    newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    if (board.getPiece(newPosition) == null){
                        ifPromoteBlack(myPosition, moves, newPosition);}
                }
            }

//            try and capture
            if (canAdd(myPosition.getRow() - 1, myPosition.getColumn() + 1)) {
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ifPromoteBlack(myPosition, moves, newPosition);
                    }
                }
            }

            if (canAdd(myPosition.getRow() - 1, myPosition.getColumn() - 1)) {
                newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(newPosition) != null) {
                    if (board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ifPromoteBlack(myPosition, moves, newPosition);
                    }
                }
            }

        return moves;
        }

    private void ifPromoteBlack(ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition newPosition) {
                if (myPosition.getRow() == 2){
                    List<ChessPiece.PieceType> possiblePieces = List.of(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN);
                    for (ChessPiece.PieceType possiblePiece : possiblePieces){
                        moves.add(new ChessMove(myPosition, newPosition, possiblePiece));
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }


    }

    private void ifPromoteWhite(ChessPosition myPosition, Collection<ChessMove> moves, ChessPosition newPosition) {
                if (myPosition.getRow() == 7){
                    List<ChessPiece.PieceType> possiblePieces = List.of(ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN);
                    for (ChessPiece.PieceType possiblePiece : possiblePieces){
                        moves.add(new ChessMove(myPosition, newPosition, possiblePiece));
                    }
                }
                else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
    }

}

