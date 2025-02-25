package service;

import Model.*;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exception.ResponseException;

import java.util.Collection;

public class GameService {
    final private GameDAO gameMemory;
    final private AuthDAO authMemory;

    public GameService(GameDAO gameMemory, AuthDAO authMemory) {
        this.gameMemory = gameMemory;
        this.authMemory = authMemory;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        try {
            AuthData authData = authMemory.getAuth(listGamesRequest.authToken());
            if (authData == null){
                throw new ResponseException(401, "Error: unauthorized");
            }

            Collection<GameData> games = gameMemory.listGames();

            return new ListGamesResult(games);
        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e);
        }
    }

    public CreateGameResult CreateGame(CreateGameRequest createGameRequest) throws ResponseException {
        if (createGameRequest.authToken() == null || createGameRequest.gameName() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            AuthData authData = authMemory.getAuth(createGameRequest.authToken());
            if (authData == null){
                throw new ResponseException(401, "Error: unauthorized");
            }

            int gameID = gameMemory.createGame(createGameRequest.gameName());
            return new CreateGameResult(gameID);

        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e);
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        if (joinGameRequest.gameID() == null || joinGameRequest.authToken() == null || (joinGameRequest.playerColor() != ChessGame.TeamColor.BLACK && joinGameRequest.playerColor() != ChessGame.TeamColor.WHITE)){
            throw new ResponseException(400, "Error: bad request");
        }
        try {
            AuthData authData = authMemory.getAuth(joinGameRequest.authToken());
            if (authData == null){
                throw new ResponseException(401, "Error: unauthorized");
            }

            GameData gameData = gameMemory.getGame(joinGameRequest.gameID());
            if ((joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK && gameData.blackUsername() != null) || (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE && gameData.whiteUsername() != null)) {
                throw new ResponseException(403, "Error: already taken");
            }

            GameData newGameData;
            if (joinGameRequest.playerColor() == ChessGame.TeamColor.BLACK) {
                newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
            }
            else {
                newGameData = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
            }
            gameMemory.updateGame(joinGameRequest.gameID(), newGameData);

        }
        catch (DataAccessException e) {
            throw new ResponseException(500, "Error: " + e);
        }
    }
}
