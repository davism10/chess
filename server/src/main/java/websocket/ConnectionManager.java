
package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
//import Messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, websocket.Connection> connections = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Boolean> validGame = new ConcurrentHashMap<>();

    public void add(String visitorName, int gameId, Session session) {
        var connection = new websocket.Connection(visitorName, session, gameId);
        validGame.put(gameId, false);
        connections.put(visitorName, connection);
    }

    public boolean isResign(int gameId){
        return validGame.get(gameId);
    }

    public void resigned(int gameId){
        validGame.put(gameId, true);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcastAll(ServerMessage notification, int gameId) throws IOException {
        var removeList = new ArrayList<websocket.Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (c.gameId == gameId) {
                    c.send(notification);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }

    public void broadcast(String excludeVisitorName, ServerMessage notification, int gameId) throws IOException {
        var removeList = new ArrayList<websocket.Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    if (c.gameId == gameId) {
                        c.send(notification);
                    }
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.visitorName);
        }
    }
}
