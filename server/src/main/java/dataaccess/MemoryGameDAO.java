package dataaccess;

import Model.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> gameDataCollection = new HashMap<>();

    public void createGame(GameData gameData){
        gameDataCollection.put(gameData.gameID(), gameData);
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
