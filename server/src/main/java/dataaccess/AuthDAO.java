package dataaccess;

import Model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void deleteAllAuth() throws DataAccessException;
}
