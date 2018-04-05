package Server;

import game.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;

    /**
     * <H1> Server</H1>
     * implements the connection and communication with the gameserver
     * @author Rudolf Klijnhout
     * @version 1.0
     * @since 03-04-2018
     **/

public class Server extends Observable implements Runnable {

        private String serverIp = "localhost";
        private int serverPort = 7789;
        private Socket socket;
        private volatile boolean connected;
        private BufferedReader dataIn;
        private PrintWriter dataOut;
        private final Object lock = new Object();
        private volatile boolean wait = false;


        public Server() {}

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
            synchronized (lock) {
                try {
                    while (true) {
                        if (wait){
                            lock.wait();
                        }
                        while(dataIn.ready()){
                            System.out.println("I need to handle a message");
                            messageHandler.handleMessage(dataIn.readLine());
                        }
                        Thread.sleep(10);
                    }
                    //help();
                    // disconnect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

        public boolean login(String name) throws IOException {
            dataOut.println("login " + name);
            if(dataIn.readLine().equals("OK")){
                return true;
            }
            return false;
        }

        public boolean disconnect() throws IOException, InterruptedException {
            dataOut.println("bye");
            Thread.sleep(200);
            socket.close();
            connected = false;
            return connected;
        }

        public ArrayList<String> getGameList() throws Exception {
            wait = true;

            synchronized (lock){
                dataOut.println("get gamelist");
                String data = dataIn.readLine();
                if (data.equals("OK")) {
                    data = dataIn.readLine();
                    System.out.println(data);
                    data = data.replaceAll("\"", "");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(14, data.length() - 1).replace(" ", "").split(",")));
                    wait = false;
                    lock.notify();
                    return list;
                } else {
                    throw new Exception("no games were returnred by the server");
                }
            }
        }

        public ArrayList<String> getPlayerlist() throws Exception {
            wait = true;
            ArrayList<String> list =new ArrayList<String>();
            synchronized (lock) {
                dataOut.println("get playerlist");
                String data = dataIn.readLine();
                if (data.equals("OK")) {
                    data = dataIn.readLine();
                   list = listHandler.handlePlayerList(data);
                    wait = false;
                    lock.notify();
                    System.out.println("returning the list");
                    return list;
                } else {
                    messageHandler.handleMessage(data);
                }
            }return list;
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

        public void move(Move move) throws IOException {
            int x = move.getX();
            int y = move.getY();
            dataOut.println("move " + x + " " + y);
            dataIn.readLine();
            dataIn.readLine();
        }

        public void challenge(String speler, String game) throws IOException {
            dataOut.println("challenge " + "\"" + speler + "\"" + " " +  "\"" + game +  "\"");
            System.out.println(dataIn.readLine());
        }

        public void forfeit() throws IOException {
            wait = true;
            synchronized (lock) {
                dataOut.println("forfeit");
                dataIn.readLine();
                wait = false;
                lock.notify();
            }
        }
        public void help() throws IOException {
            dataOut.println("help move");
            while(dataIn.ready()) {
                System.out.println(dataIn.readLine());
            }
        }
}