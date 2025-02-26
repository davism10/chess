package server;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;
import service.GameService;
import spark.*;

import java.util.Map;

public class GameServerHandler {
    final private GameService gameService;

    public GameServerHandler(GameService gameService){
        this.gameService = gameService;
    }

    public Object listGamesHandler(Request req, Response res) throws ResponseException{
        var listGamesRequest = new ListGamesRequest(req.headers("authorization"));
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            return new Gson().toJson(Map.of("games", listGamesResult));
        }
        catch (ResponseException e){
            int error = e.StatusCode();
            res.status(error);
            throw e;
        }
    }

    public Object createGameHandler(Request req, Response res) throws ResponseException{
        var jsonBody = new Gson().fromJson(req.body(), JsonObject.class);
        jsonBody.addProperty("authToken", req.headers("authorization"));
        var fullJson = new Gson().toJson(jsonBody);

        var createGameRequest = new Gson().fromJson(fullJson, CreateGameRequest.class);
        try {
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return new Gson().toJson(createGameResult);
        }
        catch (ResponseException e){
            int error = e.StatusCode();
            res.status(error);
            throw e;
        }
    }

    public Object joinGameHandler(Request req, Response res) throws ResponseException {
        var jsonBody = new Gson().fromJson(req.body(), JsonObject.class);
        jsonBody.addProperty("authToken", req.headers("authorization"));
        var fullJson = new Gson().toJson(jsonBody);

        var joinGameRequest = new Gson().fromJson(fullJson, JoinGameRequest.class);
        try {
            gameService.joinGame(joinGameRequest);
            return new Gson().toJson(null);
        }
        catch (ResponseException e){
            int error = e.StatusCode();
            res.status(error);
            throw e;
        }
    }
}
