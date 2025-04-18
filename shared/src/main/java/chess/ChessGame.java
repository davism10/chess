package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard = new ChessBoard();
    private TeamColor whoTurn = TeamColor.WHITE;

    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.whoTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.whoTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && whoTurn == chessGame.whoTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, whoTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", whoTurn=" + whoTurn +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Return the other color
     *
     * @param color the color you have
     * @return the other color
     */
    public static TeamColor notColor(TeamColor color) {
        if (color == TeamColor.WHITE) {
            return TeamColor.BLACK;
        }
        return TeamColor.WHITE;
    }

    /**
     * Tries a move in a chess game
     *
     * @param move chess move to preform
     */
    public boolean tryMove(ChessMove move) {
        boolean shouldAdd = false;
//        move piece
        ChessPiece oldPiece = gameBoard.getPiece(move.getEndPosition());
        gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
//        set old position to null
        gameBoard.addPiece(move.getStartPosition(), null);
        if (!isInCheck(gameBoard.getPiece(move.getEndPosition()).getTeamColor())) {
            shouldAdd = true;
        }
//        undo
        gameBoard.addPiece(move.getStartPosition(), gameBoard.getPiece(move.getEndPosition()));
        gameBoard.addPiece(move.getEndPosition(), oldPiece);

        return shouldAdd;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> goodMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (ChessMove testMove : goodMoves) {
            if (tryMove(testMove)) {
                possibleMoves.add(testMove);
            }
        }
        return possibleMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (gameBoard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at starting position");
        }
        if (gameBoard.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Not your turn!");
        }
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        if (possibleMoves.contains(move)) {
            if (move.getPromotionPiece() == null) {
                gameBoard.addPiece(move.getEndPosition(), gameBoard.getPiece(move.getStartPosition()));
            } else {
                gameBoard.addPiece(move.getEndPosition(), new ChessPiece(getTeamTurn(), move.getPromotionPiece()));
            }
            gameBoard.addPiece(move.getStartPosition(), null);
            setTeamTurn(notColor(getTeamTurn()));
        } else {
            throw new InvalidMoveException("move not valid");
        }
    }

    /**
     * Determines if one of the possible moves involves taking a king
     *
     * @param moves the potential moves to search through
     * @return True if the king is captured
     */
    public boolean isKing(Collection<ChessMove> moves) {
        for (ChessMove move : moves) {
            if (gameBoard.getPiece(move.getEndPosition()) != null) {
                if (gameBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessPosition> possiblePosition = gameBoard.getAllPieces(teamColor);
        Collection<ChessMove> validPieceMoves;
        for (ChessPosition position : possiblePosition) {
            if (teamColor.equals(TeamColor.WHITE)) {
                validPieceMoves = gameBoard.getPiece(position).pieceMoves(getBlackBoard(), position);
            }
            else {
                validPieceMoves = gameBoard.getPiece(position).pieceMoves(getBoard(), position);
            }
            if (isKing(validPieceMoves)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && anyMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && anyMoves(teamColor);
    }

    /**
     * loops through and sees if there are any valid moves that can be made for a team
     *
     * @param teamColor the color you want to loop through
     * @return true if there are no moves and false if there are any moves
     */

    public boolean anyMoves(TeamColor teamColor) {
        Collection<ChessPosition> possibleMoves = gameBoard.getAllPieces(notColor(teamColor));
        for (ChessPosition possibleMove : possibleMoves) {
            Collection<ChessMove> possibleValidMoves = validMoves(possibleMove);
            if (!possibleValidMoves.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    public ChessBoard getBlackBoard() {
//        ChessBoard updated = new ChessBoard();
//        for (int row = 8; row > -1 ; --row){
//            for (int col = 8; col > -1 ; --col){
//                updated.addPiece(new ChessPosition(row, col), gameBoard.getPiece(new ChessPosition(9 - row, 9 - col)));
//            }
//        }
        return gameBoard;
    }
}
