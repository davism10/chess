package ui;

import chess.ChessGame;
import model.GameData;

public interface ClientObject {
    public String help();
    public String eval(String line);
    public boolean getPost();
    public boolean getPre();
    public boolean getGame();
    public void connectAuthToken(String authToken);
    public void setObserve(Boolean observe);
    public boolean isObserved();
    public GameData getGameInfo();
    public void attatchGameInfo(GameData gameData);
    public ChessGame.TeamColor getColor();
    public void attatchColor(ChessGame.TeamColor color);
}
