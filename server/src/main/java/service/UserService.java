package service;

import Model.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    final private UserDAO userMemory;
    final private AuthDAO authMemory;

    public UserService(UserDAO userMemory, AuthDAO authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        if (registerRequest.username() == null || registerRequest.email() == null || registerRequest.password() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            UserData user = userMemory.getUser(registerRequest.username());
            if (user != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            userMemory.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));

            String authToken = generateToken();
            authMemory.createAuth(new AuthData(authToken, registerRequest.username()));

            return new RegisterResult(registerRequest.username(), authToken);
        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e);
        }

    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {

        try{
            UserData user = userMemory.getUser(loginRequest.username());
            if (user == null){
                throw new ResponseException(401, "Error: username not found");
            }
            if (!Objects.equals(user.password(), loginRequest.password())){
                throw new ResponseException(401, "Error: unauthorized");
            }
            String authToken = generateToken();
            authMemory.createAuth(new AuthData(authToken, loginRequest.username()));
            return new LoginResult(loginRequest.username(), authToken);
        }
        catch (DataAccessException e){
            throw new ResponseException(500, "Error: " + e);
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        try{
            AuthData authData = authMemory.getAuth(logoutRequest.authToken());
            if (authData == null){
                throw new ResponseException(401, "Error: unauthorized");
            }
            authMemory.deleteAuth(logoutRequest.authToken());
        }
        catch (DataAccessException e){
            throw new ResponseException(500, "Error: " + e);
        }
    }
}
