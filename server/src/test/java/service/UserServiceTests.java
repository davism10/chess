package service;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;


public class UserServiceTests {
    private static UserDAO userMemory;
    private static AuthDAO authMemory;
    private static UserService userService;
    private static String authToken;

    @BeforeAll
    public static void init() {
        userMemory = new MemoryUserDAO();
        authMemory = new MemoryAuthDAO();

        userService = new UserService(userMemory, authMemory);

    }

    @Test
    public void registerSuccess() throws ResponseException {

        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");

        RegisterResult registerResult = userService.register(registerRequest);
        RegisterResult truth = new RegisterResult("username", null);

        Assertions.assertEquals(truth.username(), registerResult.username());
        Assertions.assertNotNull(registerResult.authToken());
        authToken = registerResult.authToken();
    }

    @Test
    public void registerUsernameAgain() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email");

        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                userService.register(registerRequest));

        Assertions.assertEquals(403, myException.statusCode());
    }

    @Test
    public void loginSuccess() throws ResponseException {

        LoginRequest loginRequest = new LoginRequest("username", "password");

        LoginResult loginResult = userService.login(loginRequest);

        Assertions.assertEquals("username", loginResult.username());
        Assertions.assertNotNull(loginResult.authToken());
        Assertions.assertNotEquals(loginResult.authToken(), authToken);
    }

    @Test
    public void loginIncorrectPassword() {
        LoginRequest loginRequest = new LoginRequest("username", "passwordBad");

        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                userService.login(loginRequest));

        Assertions.assertEquals(401, myException.statusCode());
    }

    @Test
    public void logoutSuccess() throws ResponseException {

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);
        Assertions.assertNull( null );
    }

    @Test
    public void logoutInvalidAuthToken() {
        LogoutRequest logoutRequest = new LogoutRequest(authToken + 20);

        ResponseException myException = Assertions.assertThrows(ResponseException.class, () ->
                userService.logout(logoutRequest));

        Assertions.assertEquals(401, myException.statusCode());
    }

}
