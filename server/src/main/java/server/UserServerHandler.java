package server;

import model.*;
import com.google.gson.Gson;
import exception.ResponseException;
import service.UserService;
import spark.*;

public class UserServerHandler {
    final private UserService userService;

    public UserServerHandler(UserService userService){
        this.userService = userService;
    }

    public Object registerHandler(Request req, Response res) throws ResponseException {
        var registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            return new Gson().toJson(registerResult);
        }
        catch (ResponseException e){
            int error = e.statusCode();
            res.status(error);
            throw e;
        }
    }

    public Object loginHandler(Request req, Response res) throws ResponseException{
        var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult loginResult = userService.login(loginRequest);
            return new Gson().toJson(loginResult);
        }
        catch (ResponseException e){
            int error = e.statusCode();
            res.status(error);
            throw e;
        }

    }

    public Object logoutHandler(Request req, Response res) throws ResponseException{
        var logoutRequest = new LogoutRequest(req.headers("authorization"));
        try {
            userService.logout(logoutRequest);
            return new Gson().toJson(null);
        }
        catch (ResponseException e){
            int error = e.statusCode();
            res.status(error);
            throw e;
        }
    }
}
