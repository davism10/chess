package dataaccess;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public int createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames() throws DataAccessException;
    public void updateGame(Integer oldGameID, GameData newGameData) throws DataAccessException;
    public void clearAll() throws DataAccessException;
}
