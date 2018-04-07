import Server.Server;
import GUI.MainFrame;

import static javafx.application.Application.launch;


/*public class Main {
    public static void main(String args[]) throws Exception {
        Server server = new Server("145.33.225.170", 7789);
        server.connect();
        server.login("Bot2");
        System.out.println(server.getGameList());
        server.challenge("freek", "Reversi");
        Thread thread = new Thread(server);
        launch(MainFrame.class);
        thread.start();
        while (true) {
            System.out.println(server.getPlayerlist());
            thread.sleep(1000);
        }
    }
}*/