package server;

import Model.*;
import com.google.gson.Gson;
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
        var listGamesRequest = new Gson().fromJson(req.body(), ListGamesRequest.class);
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

    public Object CreateGameHandler(Request req, Response res) throws ResponseException{
        var createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
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
        var joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
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
