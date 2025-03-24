package ui;
//
import client.websocket.NotificationHandler;
import net.ClientCommunicator;
import webSocketMessages.Notification;

import java.util.Scanner;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET;

public class Repl implements ClientCommunicator {
    private ClientObject client;
    private final ClientObject preClient;
    private final ClientObject postClient;
    private final ClientObject gameClient;

    public Repl(String serverUrl) {
        preClient = new PreLoginClient(serverUrl, this);
        postClient = new PostLoginClient(serverUrl, this);
        gameClient = new GameClient(serverUrl, this);

        client = preClient;
    }

    public void run() {
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
                    switchClient(postClient);
                } else if (client.getPre()) {
                    switchClient(preClient);
                } else if (client.getGame()) {
                    switchClient(gameClient);
                }

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void switchClient(ClientObject newClient) {
        client = newClient;
    }

    public void notify(Notification notification) {
        System.out.println(RED + notification.message());
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + "\u001B[38;2;34;139;34m");
    }

}