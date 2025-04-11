package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;
    public int gameId;

    public Connection(String visitorName, Session session, int gameId) {
        this.visitorName = visitorName;
        this.session = session;
        this.gameId = gameId;
    }

    public void send(ServerMessage msg) throws IOException {
        session.getRemote().sendString(new Gson().toJson(msg));
    }
}