import Server.Server;


public class Main {
    public static void main(String args[]) throws Exception {
        Server server = new Server("145.33.225.170", 7789);
        server.connect();
        server.login("Karel");
        System.out.println(server.getGameList());
        server.challenge("Karel2", "Reversi");
        Thread thread = new Thread(server);
        thread.start();
        while (true) {
            System.out.println(server.getPlayerlist());
            thread.sleep(500);
        }
    }
}