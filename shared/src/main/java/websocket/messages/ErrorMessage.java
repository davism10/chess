package websocket.messages;

import model.GameData;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;
    public ErrorMessage(ServerMessage.ServerMessageType type, String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage(){
        return this.errorMessage;
    }
}
