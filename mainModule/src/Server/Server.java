package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class Server extends Observable implements Runnable{

    private String serverIp = "localhost";
    private int serverPort = 7789;
    private Socket socket;

    @Override
    public void run() {
        try {
            this.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {
        socket = new Socket(serverIp, serverPort);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(br.readLine());
    }
}
