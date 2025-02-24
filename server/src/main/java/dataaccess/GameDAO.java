package dataaccess;
import Model.GameData;
import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public Void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public Void updateGame(ChessGame myGame, String oldGameID, GameData newGameData) throws DataAccessException;
    public Void clearGame(int gameID) throws DataAccessException;
    public Void clearALL() throws DataAccessException;
}
