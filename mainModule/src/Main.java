import Server.Server;

public class Main {
    public static void main(String args[]) throws Exception {
        Server server = new Server("localhost", 7789);
        server.connect();
        server.login("rudolf");
        server.run();
    }
}
