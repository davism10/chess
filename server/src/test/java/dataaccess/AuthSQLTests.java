package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthSQLTests {
    private static AuthDAO authMemory;
    private static AuthData authDataFirst;

    @BeforeAll
    public static void init() throws DataAccessException {
        authMemory = new SQLAuthDAO();
        authMemory.deleteAllAuth();
        authDataFirst = new AuthData("authname", "usesrname");

    }

    @Test
    @Order(1)
    public void createAuthSuccess() throws DataAccessException {
        AuthData authDataSecond = new AuthData("authname1", "username1");
        authMemory.createAuth(authDataFirst);
        Assertions.assertEquals(authMemory.getAuth("authname"), authDataFirst);
    }

    @Test
    @Order(2)
    public void createAuthFail() throws DataAccessException {
        DataAccessException myException = Assertions.assertThrows(DataAccessException.class, () ->
                authMemory.createAuth(authDataFirst));
    }

    @Test
    @Order(3)
    public void getAuthSuccess() throws DataAccessException {
        Assertions.assertEquals(authMemory.getAuth("authname"), authDataFirst);
    }

    @Test
    @Order(4)
    public void getAuthFail() throws DataAccessException {
        Assertions.assertNull(authMemory.getAuth("notaauthname"));
    }

    @Test
    @Order(5)
    public void clearAuthSuccess() throws DataAccessException {
        authMemory.deleteAuth("authname");
        Assertions.assertNull(authMemory.getAuth("authname"));
    }

    @Test
    @Order(6)
    public void clearAllSuccess() throws DataAccessException {
        authMemory.deleteAllAuth();
        Assertions.assertNull(authMemory.getAuth("authname"));
        Assertions.assertNull(authMemory.getAuth("authname1"));

    }

}
