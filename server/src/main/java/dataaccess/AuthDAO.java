package dataaccess;

import Model.AuthData;

public interface AuthDAO {
    public Void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public Void deleteAuth(String authToken) throws DataAccessException;
    public Void deleteAllAuth() throws DataAccessException;
}
