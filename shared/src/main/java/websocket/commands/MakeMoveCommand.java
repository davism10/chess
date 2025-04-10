package websocket.commands;

import model.GameData;

public class MakeMoveCommand extends UserGameCommand {
    private final GameData gameData;

    public MakeMoveCommand(UserGameCommand.CommandType commandType, String authToken, Integer gameID, GameData gameData) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.gameData = gameData;
    }

    public GameData getGameData() {
        return gameData;
    }
}
