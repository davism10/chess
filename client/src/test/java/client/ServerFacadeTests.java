package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.*;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        try {
            facade.clear();
        }
        catch (Exception e){
            System.out.print(e);
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    void register() throws Exception {
        var authData = facade.register(new RegisterRequest("player1",
                "password", "p1@email.com"));
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @Order(2)
    void registerFail() throws Exception {
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                facade.register(new RegisterRequest("player1",
                        "password", "p1@email.com")));
    }

    @Test
    @Order(3)
    void login() throws Exception {
        var authData = facade.login(new LoginRequest("player1", "password"));
        authToken = authData.authToken();
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test
    @Order(4)
    void loginFail() throws Exception {
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                facade.login(new LoginRequest("player2",
                        "password")));
    }

    @Test
    @Order(11)
    void logout() throws Exception {
        facade.logout();
        Assertions.assertTrue(true);
    }

    @Test
    @Order(12)
    void logoutFail() throws Exception {
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                facade.logout());
    }

    @Test
    @Order(6)
    void createGame() throws Exception {
        var authData = facade.createGame(new CreateGameRequest(authToken,
                "game"));
        Assertions.assertEquals(authData.gameID(), 1);
    }

    @Test
    @Order(7)
    void createGameFail() throws Exception {
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                facade.createGame(new CreateGameRequest(null,
                        null)));
    }

    @Test
    @Order(8)
    void listGames() throws Exception {
        var authData = facade.listGames(new ListGamesRequest(authToken));
        Assertions.assertNotNull(authData.games());
    }

    @Test
    @Order(5)
    void listGamesEmpty() throws Exception {
        var authData = facade.listGames(new ListGamesRequest(authToken));
        Assertions.assertNotNull(authData.games());
    }

    @Test
    @Order(9)
    void joinGame() throws Exception {
        facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,
                1, authToken));
        Assertions.assertTrue(true);
    }

    @Test
    @Order(10)
    void joinGameFail() throws Exception {
        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                facade.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,
                        1, authToken)));
    }

    @Test
    @Order(13)
    void clear() throws Exception {
        facade.clear();
        Assertions.assertTrue(true);
    }


}
