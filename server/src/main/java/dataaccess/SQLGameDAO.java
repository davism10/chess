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
        ConfigureDatabase.configureDatabase(createStatements);
    }

    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO gameData (whiteusername, blackusername, gamename, chessgame) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ChessGame myGame = new ChessGame();
                ps.setNull(1, java.sql.Types.VARCHAR);
                ps.setNull(2, java.sql.Types.VARCHAR);
                ps.setString(3, gameName);
                ps.setString(4, new Gson().toJson(myGame));
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                else {
                    return 0;
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameid, whiteusername, blackusername, gamename, chessgame FROM gameData WHERE gameid=?";
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
                    else{
                        throw new DataAccessException("game id not found");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameid, whiteusername, blackusername, gamename, chessgame  FROM gameData";
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
                    return result;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

    }

    public void updateGame(Integer oldGameID, GameData newGameData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            String statement = "UPDATE gameData SET whiteusername = ?, blackusername = ?, gamename = ?, chessgame = ? WHERE gameid = ?";
            try (var ps = conn.prepareStatement(statement)) {
                    ps.setString(1, newGameData.whiteUsername());
                    ps.setString(2, newGameData.blackUsername());
                    ps.setString(3, newGameData.gameName());
                    ps.setString(4, new Gson().toJson(newGameData.game())); // Convert ChessGame to JSON
                    ps.setInt(5, oldGameID);
                    ps.executeUpdate();
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }

    }

    public void clearAll() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
                CREATE TABLE IF NOT EXISTS  gameData (
                  gameid INT NOT NULL AUTO_INCREMENT,
                  whiteusername VARCHAR(256),
                  blackusername VARCHAR(256),
                  gamename VARCHAR(256) NOT NULL,
                  chessgame TEXT NOT NULL,
                  PRIMARY KEY (gameid)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
                """
    };


    }

