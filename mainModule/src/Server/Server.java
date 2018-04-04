package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

    /**
     * <H1> Server</H1>
     * implements the connection and communication with the gameserver
     * @author Rudolf Klijnhout
     * @version 1.0
     * @since 03-04-2018
     **/

public class Server extends Observable implements Runnable {

        private String serverIp;
        private int serverPort;
        private Socket socket;
        private volatile boolean connected;
        private BufferedReader dataIn;
        private PrintWriter dataOut;


        public Server() {
            serverIp = "localhost";
            serverPort = 7789;
        }

        public Server(String serverIp) {
            this.serverIp = serverIp;
        }

        public Server(int serverPort) {
            this.serverPort = serverPort;
        }

        public Server(String serverIp, int serverPort) {
            this.serverIp = serverIp;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            try {
                connect();
                login("rudolf");
                System.out.println(getGameList());
                getPlayerlist();
                System.out.println(subscribe("Reversi"));
                help();
                //disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void connect() throws IOException {
            socket = new Socket(serverIp, serverPort);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            String data = dataIn.readLine();
            System.out.println(data);
            System.out.println(dataIn.readLine());
            connected = true;
        }

        public void login(String name) throws IOException {
            dataOut.println("login " + name);
            System.out.println(dataIn.readLine());
        }

        public boolean disconnect() throws IOException, InterruptedException {
            dataOut.println("bye");
            Thread.sleep(200);
            socket.close();
            connected = false;
            return connected;
        }

        public ArrayList<String> getGameList() throws Exception {
            dataOut.println("get gamelist");
            String data = dataIn.readLine();
            if (data.equals("OK")) {
                data = dataIn.readLine();
                data = data.replaceAll("\"", "");
                System.out.println(data);
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(14, data.length() - 1).replace(" ", "").split(",")));
                for (String s : list) {
                    System.out.println(s);
                }
                return list;
            } else {
                throw new Exception("no server response");
            }

        }

        public ArrayList<String> getPlayerlist() throws Exception {
            dataOut.println("get playerlist");
            String data = dataIn.readLine();
            if (data.equals("OK")) {
                data = dataIn.readLine();
                data = data.replaceAll("\"", "");
                System.out.println(data);
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(16, data.length() - 1).replace(" ", "").split(",")));
                for (String s : list) {
                    System.out.println(s);
                }
                return list;
            } else {
                throw new Exception("no server response");
            }
        }

        public boolean subscribe(String game) throws IOException {
            dataOut.println("subscribe " + game);
            String data = dataIn.readLine();
            if (data.equals("OK")) {
                return true;
            } else {
                return false;
            }
        }

        public void move(int x, int y) throws IOException {
            dataOut.println("move " + x + ", " + y);
            dataIn.readLine();
            dataIn.readLine();
        }

        public void challenge(String speler, String game) throws IOException {
            dataOut.println("challenge " + speler + " " + game);
            dataIn.readLine();
            dataIn.readLine();
        }

        public void forfeit() throws IOException {
            dataOut.println("forfeit");
            dataIn.readLine();
        }
        public void help() throws IOException {
            dataOut.println("help move");
            while(true) {
                System.out.println(dataIn.readLine());
            }
        }
}