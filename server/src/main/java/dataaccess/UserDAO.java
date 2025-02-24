package dataaccess;

import Model.UserData;

public interface UserDAO {
    public Void clear(String username) throws DataAccessException;
    public Void clearAll() throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public Void createUser(UserData userData) throws DataAccessException;
}
