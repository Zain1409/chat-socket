package client;

import java.io.InputStream;
import java.util.Scanner;

public class ReceivedMessagesHandler implements Runnable {
    private InputStream server;
    public ReceivedMessagesHandler(InputStream server) {

        this.server = server;
    }

    @Override
    public void run() {
        Scanner s = new Scanner(server);
        while (s.hasNextLine()){
            System.out.println(s.nextLine());
        }
        s.close();
    }
}
