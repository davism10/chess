package service;

import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ClearServiceTests {
    private static UserDAO userMemory;
    private static GameDAO gameMemory;
    private static AuthDAO authMemory;
    private static ClearService clearService;
    private static UserService userService;
    private static GameService gameService;
    private static String authToken;

    @BeforeAll
    public static void init() throws ResponseException {
        gameMemory = new MemoryGameDAO();
        authMemory = new MemoryAuthDAO();
        userMemory = new MemoryUserDAO();

        gameService = new GameService(gameMemory, authMemory);
        clearService = new ClearService(gameMemory, authMemory, userMemory);
        userService = new UserService(userMemory, authMemory);

        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");
        RegisterResult registerResult = userService.register(registerRequest);
        authToken = registerResult.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "gameName");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

    }

    @Test
    public void clearSuccess() throws ResponseException{
        clearService.clear();
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                gameService.listGames(listGamesRequest));

        Assertions.assertEquals(401, myException.statusCode());
    }
}
