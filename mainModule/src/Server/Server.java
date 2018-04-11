package Server;

import Game.Move;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
        private volatile boolean connected = false;
        private BufferedReader dataIn;
        private PrintWriter dataOut;
        private final Object lock = new Object();
        private volatile boolean wait = false;
        private static Server server = new Server();
        private String playerName;
        private boolean quit = false;


        private Server() {
            ConfigHandler config = ConfigHandler.getInstance();
            serverIp = config.getIp();
            try {
                serverPort = Integer.parseInt(config.getPort());
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number in config. replacing with default port");
                serverPort = 7789;
            }
            playerName = config.getNickname();
        }

        static public Server getInstance(){
            return server;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    while (!quit) {
                        if (wait){
                            lock.wait();
                        }
                        while(dataIn.ready()){
                            MessageHandler.handleMessage(dataIn.readLine());
                        }
                        Thread.sleep(10);
                    }
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
            dataIn.readLine();
            dataIn.readLine();
            connected = true;
        }

        public void login(String playerName) throws Exception {
            this.playerName = playerName;
            System.out.println(this.playerName);
            LobbyObservable.setPlayerName(playerName);
            wait = true;
            synchronized (lock){

                dataOut.println("login " + playerName);
                MessageHandler.waitForOk(dataIn);
                wait = false;
                lock.notify();
            }
        }

        public boolean disconnect() throws InterruptedException {
            //if (isConnected()) {
            wait = true;
            synchronized (lock){
                dataOut.println("bye");
                Thread.sleep(200);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected = false;
                wait = false;
                lock.notify();
            }
            return connected;
        }

        public ArrayList<String> getGameList() throws Exception {
            wait = true;
            ArrayList<String> list =new ArrayList<String>();
            synchronized (lock){
                dataOut.println("get gamelist");
                MessageHandler.waitForOk(dataIn);
                String data = dataIn.readLine();
                list = ListHandler.handleGamelist(data);
                wait = false;
                lock.notify();
                return list;
            }
        }

        public ArrayList<String> getPlayerlist() throws Exception {
            wait = true;
            ArrayList<String> list =new ArrayList<String>();

            synchronized (lock) {
                dataOut.println("get playerlist");
                MessageHandler.waitForOk(dataIn);
                String data = dataIn.readLine();
                list = ListHandler.handlePlayerList(data);
                wait = false;
                lock.notify();
                return list;
            }
        }

        public boolean subscribe(String game) throws Exception {
            wait = true;
            synchronized (lock) {
                dataOut.println("subscribe " + game);
                MessageHandler.waitForOk(dataIn);
                wait = false;
                lock.notify();
                return true;
            }
        }

        public void doMove(Move move) throws Exception {
            wait = true;
            synchronized (lock){
                int pos = move.getPos();
                dataOut.println("move " + pos);
                MessageHandler.waitForOk(dataIn);
                wait = false;
                lock.notify();
            }
        }

        public void challenge(String speler, String game) throws Exception {
            wait = true;
            synchronized (lock){
                dataOut.println("challenge " + "\"" + speler + "\"" + " " +  "\"" + game +  "\"");
                MessageHandler.waitForOk(dataIn);
                wait = false;
                lock.notify();
            }

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

        public void acceptChallenge(Challenge challenge) throws Exception {
            wait = true;
            synchronized (lock){
                dataOut.println("challenge accept " + challenge.getChallengeNumber());
                MessageHandler.waitForOk(dataIn);
                wait = false;
                lock.notify();
            }
        }

        public void saveConfig() {
            ConfigHandler config = ConfigHandler.getInstance();
            config.saveConfig(serverIp, ""+serverPort, playerName);
        }

        public void commandStatus() {

        }

        public boolean isConnected() {
            return connected;
        }

        public String getServerIp() {
            return serverIp;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        public String getPlayerName(){return playerName;}

        public void quit() { this.quit = true; }


}