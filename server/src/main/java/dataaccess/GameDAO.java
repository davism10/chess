package dataaccess;
import Model.GameData;
import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    public void createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(Integer oldGameID, GameData newGameData) throws DataAccessException;
    public void clearGame(int gameID) throws DataAccessException;
    public void clearAll() throws DataAccessException;
}
