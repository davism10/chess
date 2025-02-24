package dataaccess;

import Model.AuthData;
import Model.GameData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    final private HashMap<String, AuthData> authDataCollection = new HashMap<>();

    public void createAuth(AuthData authData){
        authDataCollection.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken){
        return authDataCollection.get(authToken);
    }

    public void deleteAuth(String authToken){
        authDataCollection.remove(authToken);
    }

    public void deleteAllAuth(){
        authDataCollection.clear();
    }

}
