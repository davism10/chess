package ui;
import chess.ChessGame;
import model.GameData;
import net.ClientCommunicator;
import net.ServerFacade;
import java.util.Scanner;
//import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;

public class Repl implements ClientCommunicator {
    private ClientObject client;
    private final ClientObject preClient;
    private final ClientObject postClient;
    private final ClientObject gameClient;
    private String authToken = null;
    private Boolean observed;
    ServerFacade serverFacade;
    private ChessGame.TeamColor color;
    private GameData gameData;

    public Repl(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
        preClient = new PreLoginClient(serverUrl, this, serverFacade);
        postClient = new PostLoginClient(serverUrl, this, serverFacade);
        gameClient = new GameClient(serverUrl, this, serverFacade);

        client = preClient;
    }

    public void run() {
        System.out.print("\u001B[38;2;255;102;204m");
        System.out.println("\uD83D\uDC51 Welcome to 240 chess. Type Help to get started. \uD83D\uDC51");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print("\u001B[38;2;255;102;204m" + result);
                if (client.getPost()){
                    this.authToken = serverFacade.getAuth();
                    switchClient(postClient);
                    client.connectAuthToken(authToken);
                } else if (client.getPre()) {
                    switchClient(preClient);
                } else if (client.getGame()) {
                    observed = client.isObserved();
                    color = client.getColor();
                    gameData = client.getGameInfo();

                    switchClient(gameClient);

                    client.setObserve(observed);
                    client.attatchGameInfo(gameData);
                    client.attatchColor(color);
                }

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
        System.out.println("Thanks for playing \u2661");
    }

    public void switchClient(ClientObject newClient) {
        client = newClient;
    }


    private void printPrompt() {
//        System.out.print("\n" + RESET + ">>> " + "\u001B[38;2;34;139;34m");
        System.out.print("\n" +  ">>> " + "\u001B[38;2;34;139;34m");
    }

}