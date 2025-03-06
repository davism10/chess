package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO {
    public SQLGameDAO(){

    }

    public int createGame(String gameName) {

    }

    public GameData getGame(int gameID) {

    }

    public Collection<GameData> listGames() {

    }

    public void updateGame(Integer oldGameID, GameData newGameData) {

    }

    public void clearAll() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameid` INT NOT NULL AUTO_INCREMENT,
              `whiteusername` varchar(256),
              `blackusername` varchar(256),
              `gamename` varchar(256) NOT NULL,
              `chessgame` TEXT NOT NULL,
              PRIMARY KEY (`gameid`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException, ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
