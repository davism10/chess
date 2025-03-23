package net;
import webSocketMessages.Notification;

public interface ClientCommunicator {
    void notify(Notification notification);
}
