package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException{
        configureDatabase();
    }

    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (whiteusername, blackusername, gamename, chessgame) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ChessGame myGame = new ChessGame();
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, gameName);
                ps.setString(4, new Gson().toJson(myGame));
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameid FROM gameData WHERE gameid=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var whiteUsernameData = rs.getString("whiteusername");
                        var blackUserNameData = rs.getString("blackusername");
                        var gameNameData = rs.getString("gamename");
                        var chessGameData = rs.getString("chessgame");
                        ChessGame chessGame = new Gson().fromJson(chessGameData, ChessGame.class);
                        return new GameData(gameID, whiteUsernameData, blackUserNameData, gameNameData, chessGame);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

public Collection<GameData> listGames() throws DataAccessException {
    var result = new ArrayList<GameData>();
    try (var conn = DatabaseManager.getConnection()) {
        var statement = "SELECT gameid, json FROM gameData";
        try (var ps = conn.prepareStatement(statement)) {
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    var gameID = rs.getInt("gameid");
                    var whiteUsernameData = rs.getString("whiteusername");
                    var blackUserNameData = rs.getString("blackusername");
                    var gameNameData = rs.getString("gamename");
                    var chessGameData = rs.getString("chessgame");
                    ChessGame chessGame = new Gson().fromJson(chessGameData, ChessGame.class);
                    GameData thisGame =  new GameData(gameID, whiteUsernameData, blackUserNameData, gameNameData, chessGame);
                    result.add(thisGame);
                }
            }
        }
    } catch (Exception e) {
        throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return result;

}

public void updateGame(Integer oldGameID, GameData newGameData) {


}

public void clearAll() {

}

private final String[] createStatements = {
        """
            CREATE TABLE IF NOT EXISTS  gameData (
              gameid INT NOT NULL AUTO_INCREMENT,
              whiteusername VARCHAR(256),
              blackusername VARCHAR(256),
              gamename VARCHAR(256) NOT NULL,
              chessgame TEXT NOT NULL,
              PRIMARY KEY (gameid),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
};

private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
        for (var statement : createStatements) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
    } catch (SQLException ex) {
        throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
}

}

