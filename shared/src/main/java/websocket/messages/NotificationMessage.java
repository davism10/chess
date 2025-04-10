package websocket.messages;

import model.GameData;

public class NotificationMessage extends ServerMessage {
    private final String message;
    public NotificationMessage(ServerMessage.ServerMessageType type, String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
