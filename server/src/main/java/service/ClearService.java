package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;

public class ClearService {
    final private GameDAO gameMemory;
    final private AuthDAO authMemory;
    final private UserDAO userMemory;

    public ClearService(GameDAO gameMemory, AuthDAO authMemory, UserDAO userMemory) {
        this.gameMemory = gameMemory;
        this.authMemory = authMemory;
        this.userMemory = userMemory;
    }

    public void clear() throws ResponseException {
        try {
            gameMemory.clearAll();
            authMemory.deleteAllAuth();
            userMemory.clearAll();
        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e);
        }
    }
}
