package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> gameDataCollection = new HashMap<>();
    int gameID = 1;

    public int createGame(String gameName){
        int ourGameID = gameID;
        GameData gameData = new GameData(ourGameID, null, null, gameName, new ChessGame());
        gameDataCollection.put(ourGameID, gameData);
        gameID += 1;
        return ourGameID;
    }

    public GameData getGame(int gameID){
        return gameDataCollection.get(gameID);
    }

    public Collection<GameData> listGames(){
        return gameDataCollection.values();
    }

    public void updateGame(Integer oldGameID, GameData newGameData){
        gameDataCollection.replace(oldGameID, newGameData);
    }

    public void clearGame(int gameID){
        gameDataCollection.remove(gameID);
    }

    public void clearAll(){
        gameDataCollection.clear();
    }
}
