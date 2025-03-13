package dataaccess;

import exception.ResponseException;
import model.RegisterRequest;
import model.RegisterResult;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.UserService;

import javax.xml.crypto.Data;

public class UserSQLTests {
    private static UserDAO userMemory;
    private static UserData userDataFirst;

    @BeforeAll
    public static void init() throws DataAccessException {
        userMemory = new SQLUserDAO();
        userMemory.clearAll();
        userDataFirst = new UserData("username", "password", "email");

    }

    @Test
    @Order(1)
    public void createUserSuccess() throws DataAccessException {
        UserData userDataSecond = new UserData("username1", "password2", "email3");
        userMemory.createUser(userDataFirst);
        userMemory.createUser(userDataSecond);
        userDataFirst = new UserData("username", BCrypt.hashpw("password", BCrypt.gensalt()), "email");
        Assertions.assertEquals(userMemory.getUser("username").email(), userDataFirst.email());
    }

    @Test
    @Order(2)
    public void createUserFail() throws DataAccessException {
        userMemory.createUser(userDataFirst);
        DataAccessException myException = Assertions.assertThrows(DataAccessException.class, () ->
                userMemory.createUser(userDataFirst));
    }

    @Test
    @Order(3)
    public void getUserSuccess() throws DataAccessException {
        Assertions.assertEquals(userMemory.getUser("username").email(), userDataFirst.email());
    }

    @Test
    @Order(4)
    public void getUserFail() throws DataAccessException {
        Assertions.assertNull(userMemory.getUser("notausername"));
    }

    @Test
    @Order(5)
    public void clearUserSuccess() throws DataAccessException {
        userMemory.clear("username");
        Assertions.assertNull(userMemory.getUser("username"));
    }

    @Test
    @Order(6)
    public void clearAllSuccess() throws DataAccessException {
        userMemory.clearAll();
        Assertions.assertNull(userMemory.getUser("username"));
        Assertions.assertNull(userMemory.getUser("username1"));

    }

}
