package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    public void notifyError(ErrorMessage notification);

    public void notify(NotificationMessage notification);

    public void notifyLoadGame(LoadGameMessage notification);

    public void printPrompt();


}
