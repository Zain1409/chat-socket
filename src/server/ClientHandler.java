package server;

import java.io.InputStream;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Server server;
    private InputStream client;

    public ClientHandler(Server server, InputStream client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        String message;
        Scanner sc = new Scanner(this.client);
        while(sc.hasNextLine()){
            message = sc.nextLine();
            server.broadcastMessages(message);
            System.out.println(message);
        }
        sc.close();
    }
}
