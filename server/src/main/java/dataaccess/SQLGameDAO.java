package dataaccess;

import model.GameData;

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

}
