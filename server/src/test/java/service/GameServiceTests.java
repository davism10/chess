package service;
import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {
    private static UserDAO userMemory;
    private static GameDAO gameMemory;
    private static AuthDAO authMemory;
    private static UserService userService;
    private static GameService gameService;
    private static String authToken;
    private static Integer gameID;

    @BeforeAll
    public static void init() throws ResponseException {
        gameMemory = new MemoryGameDAO();
        authMemory = new MemoryAuthDAO();
        userMemory = new MemoryUserDAO();

        gameService = new GameService(gameMemory, authMemory);
        userService = new UserService(userMemory, authMemory);

        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");

        RegisterResult registerResult = userService.register(registerRequest);
        authToken = registerResult.authToken();
        gameID = 1;
    }

    @Test
    @Order(1)
    public void createUniqueGameSuccess() throws ResponseException{
        RegisterRequest registerRequest2 = new RegisterRequest("username1", "password1", "email1");
        RegisterResult registerResult2 = userService.register(registerRequest2);

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "gameName");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        gameID = createGameResult.gameID();

        CreateGameRequest createGameRequest2 = new CreateGameRequest(registerResult2.authToken(), "gameName2");
        CreateGameResult createGameResult2 = gameService.createGame(createGameRequest2);

        Assertions.assertNotEquals(createGameResult.gameID(),createGameResult2.gameID() );

    }

    @Test
    @Order(2)
    public void createGameWithoutAuthToken() {
        CreateGameRequest createGameRequest = new CreateGameRequest(null, "gameName");

        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                gameService.createGame(createGameRequest));

        Assertions.assertEquals(400, myException.statusCode());
    }

    @Test
    @Order(3)
    public void joinGameSuccess() throws ResponseException{
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID, authToken);
        gameService.joinGame(joinGameRequest);
        Assertions.assertNull(null);
    }

    @Test
    @Order(4)
    public void joinGameSameColor(){
        JoinGameRequest joinGameRequest = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID, authToken);

        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                gameService.joinGame(joinGameRequest));

        Assertions.assertEquals(403, myException.statusCode());
    }

    @Test
    @Order(5)
    public void listGamesSuccess() throws ResponseException {
        ListGamesRequest listGameRequest = new ListGamesRequest(authToken);
        ListGamesResult allGames = gameService.listGames(listGameRequest);


        Assertions.assertNotNull(allGames.games());
    }

    @Test
    @Order(6)
    public void invalidAuthTokenListGames() {
        ListGamesRequest listGameRequest = new ListGamesRequest(authToken + 20);
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                gameService.listGames(listGameRequest));

        Assertions.assertEquals(401, myException.statusCode());
    }
}
