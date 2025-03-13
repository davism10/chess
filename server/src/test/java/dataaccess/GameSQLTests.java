package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameSQLTests {
    private static GameDAO gameMemory;
    private static GameData gameDataFirst;
    private static GameData gameDataSecond;

    @BeforeAll
    public static void init() throws DataAccessException {
        gameMemory = new SQLGameDAO();
        gameMemory.clearAll();

    }

    @Test
    @Order(1)
    public void createGameSuccess() throws DataAccessException {
        int gameIDOne = gameMemory.createGame("gamename1");
        int gameIDTwo = gameMemory.createGame("gamename2");
        gameDataFirst = new GameData(gameIDOne, null, null, "gamename1", new ChessGame());
        gameDataSecond = new GameData(gameIDTwo, null, null, "gamename2", new ChessGame());
        Assertions.assertEquals(gameMemory.getGame(gameIDOne), gameDataFirst);
    }

    @Test
    @Order(2)
    public void createGameTwoSuccess() throws DataAccessException {
        Assertions.assertNotEquals(gameDataFirst.gameID(), gameDataSecond.gameID());
    }

    @Test
    @Order(3)
    public void getUserSuccess() throws DataAccessException {
        Assertions.assertEquals(gameMemory.getGame(gameDataFirst.gameID()), gameDataFirst);
    }

    @Test
    @Order(4)
    public void getGameFail() throws DataAccessException {
        int notGameID = gameDataFirst.gameID() + 20;
        DataAccessException myException = Assertions.assertThrows(DataAccessException.class, () ->
                gameMemory.getGame(notGameID));
    }

    @Test
    @Order(5)
    public void updateGameSuccess() throws DataAccessException {
        GameData gameDataNew = new GameData(gameDataFirst.gameID(), "whiteUsername", null, "gameame1", new ChessGame());
        gameDataFirst = gameDataSecond;
        gameMemory.updateGame(gameDataFirst.gameID(), gameDataNew);
        Assertions.assertEquals(gameMemory.getGame(gameDataFirst.gameID()).whiteUsername(), gameDataNew.whiteUsername());
    }

    @Test
    @Order(6)
    public void updateGameFail() throws DataAccessException {
        int notGameID = gameDataFirst.gameID() + 27;
        gameMemory.updateGame(notGameID, gameDataFirst);
        DataAccessException myException = Assertions.assertThrows(DataAccessException.class, () ->
                gameMemory.getGame(notGameID));
    }

    @Test
    @Order(7)
    public void listGameSuccess() throws DataAccessException {
        var allGames = gameMemory.listGames();
        Assertions.assertNotNull(allGames);
    }

    @Test
    @Order(9)
    public void listGameSuccessMore() throws DataAccessException {
        var allGames = gameMemory.listGames();
        Collection<GameData> trueGames = new ArrayList<>();
        Assertions.assertEquals(allGames, trueGames);
    }

    @Test
    @Order(8)
    public void clearAllSuccess() throws DataAccessException {
        gameMemory.clearAll();
        DataAccessException myException = Assertions.assertThrows(DataAccessException.class, () ->
                gameMemory.getGame(gameDataSecond.gameID()));
        DataAccessException myExceptionSecond = Assertions.assertThrows(DataAccessException.class, () ->
                gameMemory.getGame(gameDataFirst.gameID()));

    }

}
