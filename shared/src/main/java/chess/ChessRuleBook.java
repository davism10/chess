package chess;

import java.util.Collection;

public interface ChessRuleBook {
    public Collection<ChessMove> validMoves(ChessBoard board, ChessPosition myPosition);
    public boolean isBoardValid(ChessBoard board);
    public boolean isInCheck(ChessBoard board, ChessGame.TeamColor color);
    public boolean isInCheckmate(ChessBoard board, ChessGame.TeamColor color);
    public boolean isInStalemate(ChessBoard board, ChessGame.TeamColor color);
}
