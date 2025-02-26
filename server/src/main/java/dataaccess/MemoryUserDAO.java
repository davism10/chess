package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> userDataCollection = new HashMap<>();

    public void clear(String username){
        userDataCollection.remove(username);
    }

    public void clearAll(){
        userDataCollection.clear();
    }

    public UserData getUser(String username){
        return userDataCollection.get(username);
    }
    public void createUser(UserData userData){
        userDataCollection.put(userData.username(), userData);
    }
}
