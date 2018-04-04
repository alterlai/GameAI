import Server.Server;

public class Main {
    public static void main(String args[]) throws Exception {
        Server server = new Server("localhost", 7789);
        server.connect();
        server.login("henk");
        server.getGameList();
        Thread thread = new Thread(server);
        thread.start();
        while (true) {
            System.out.println("hallo?");
            System.out.println(server.getGameList());
            thread.sleep(100);
        }
    }
}
