package client;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private String host;
    private int port;
    private String nickname;

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Client("localhost", 6788).run();
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws UnknownHostException, IOException {
        Socket client = new Socket(host, port);
        System.out.println("Client kết nối server thành công!");

        new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();

        Scanner sc = new Scanner(System.in);
        System.out.print("nhập username: ");
        nickname = sc.nextLine();

        System.out.println("gửi tin nhắn: ");
        PrintStream output = new PrintStream(client.getOutputStream());
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if(s.equals("q")){
                output.println(nickname + ": " + "đã rời khỏi cuộc trò chuyện :(");
                break;
            }else{
                output.println(nickname + ": " + s);

            }
        }

        output.close();
        sc.close();
        client.close();
    }
}
