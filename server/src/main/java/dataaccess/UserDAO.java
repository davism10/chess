package dataaccess;

import Model.UserData;

public interface UserDAO {
    public void clear(String username) throws DataAccessException;
    public void clearAll() throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData userData) throws DataAccessException;
}
