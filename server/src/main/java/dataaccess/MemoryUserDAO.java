package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData hashedUserData = new UserData(userData.username(), hashedPassword, userData.email());
        userDataCollection.put(userData.username(), hashedUserData);
    }
}
