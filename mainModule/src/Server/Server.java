package Server;

import Game.Move;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

    /**
     * <H1> Server</H1>
     * implements the connection and communication with the gameserver
     * @author Rudolf Klijnhout
     * @version 1.5
     * @since 16-04-2018
     **/

public class Server extends Observable implements Runnable {
        private String serverIp = "localhost";
        private int serverPort = 7789;
        private Socket socket;
        private volatile boolean connected = false;
        private BufferedReader dataIn;
        private PrintWriter dataOut;
        private final Object lock = new Object();
        private volatile boolean waitForMyCommand = false;
        private static Server server = new Server();
        private String playerName;
        private boolean quit = false;
        private boolean isAI;

        /**
         * Constructor of the server class
         * Gets the settings from the settings file on program init
         */
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
            isAI = config.getIsAI();
        }

        /**
         * This method is used to get the instance of the server singelton
         * @return server object
         **/
        static public Server getInstance(){
            return server;
        }

        /**
         * this method is used to monitor messages from the server and offer them to the messagehandler.
         */
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    while (!quit) {
                        if (waitForMyCommand){
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

        /**
         * This method is used to connect to the server sets the boolean connected when the server is connected
         * @throws IOException
         * @see IOException
         */
        public void connect() throws IOException {
            socket = new Socket(serverIp, serverPort);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            dataIn.readLine();
            dataIn.readLine();
            connected = true;
        }

        /**
         * This method is used to login as a player on a remote server
         * @param playerName string the preferred playername
         * @throws Exception when unable to send data
         */
        public void login(String playerName) throws Exception {
            this.playerName = playerName;
            LobbyObservable.setPlayerName(playerName);
            waitForMyCommand = true;
            synchronized (lock){

                dataOut.println("login " + playerName);
                MessageHandler.waitForOk(dataIn);
                waitForMyCommand = false;
                lock.notify();
            }
        }

        /**
         * This method closes the connection with the server and then closes the socket
         * @throws InterruptedException
         */
        public void disconnect() throws InterruptedException {
            if (isConnected()) {
                waitForMyCommand = true;
                synchronized (lock) {
                    dataOut.println("bye");
                    Thread.sleep(200);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    connected = false;
                    waitForMyCommand = false;
                    lock.notify();
                }
            }
        }

        /**
         * This method is used to request the gamelist
         * @return ArrayList of available games
         * @throws Exception when unable to send data
         */
        public ArrayList<String> getGameList() throws Exception {
            waitForMyCommand = true;
            ArrayList<String> list =new ArrayList<String>();
            synchronized (lock){
                dataOut.println("get gamelist");
                MessageHandler.waitForOk(dataIn);
                String data = dataIn.readLine();
                list = ListHandler.handleGamelist(data);
                waitForMyCommand = false;
                lock.notify();
                return list;
            }
        }

        /**
         * This method is used to request the playerlist
         * @return ArrayList of players logged in on the server
         * @throws Exception when unable to send data
         */
        public ArrayList<String> getPlayerlist() throws Exception {
            waitForMyCommand = true;
            ArrayList<String> list =new ArrayList<String>();

            synchronized (lock) {
                dataOut.println("get playerlist");
                MessageHandler.waitForOk(dataIn);
                String data = dataIn.readLine();
                list = ListHandler.handlePlayerList(data);
                waitForMyCommand = false;
                lock.notify();
                return list;
            }
        }

        /**
         * This method is used to subscribe to a game on the server
         * @param game String of the name of the game
         * @return boolean true when subscribed
         * @throws Exception when unable to send data
         */
        public boolean subscribe(String game) throws Exception {
            waitForMyCommand = true;
            synchronized (lock) {
                dataOut.println("subscribe " + game);
                MessageHandler.waitForOk(dataIn);
                waitForMyCommand = false;
                lock.notify();
                return true;
            }
        }

        /**
         * This method is used to play a move on the server
         * @param move the move to be played
         * @throws Exception when unable to send data
         */
        public void doMove(Move move) throws Exception {
            waitForMyCommand = true;
            synchronized (lock){
                int pos = move.getPos();
                dataOut.println("move " + pos);
                MessageHandler.waitForOk(dataIn);
                waitForMyCommand = false;
                lock.notify();
            }
        }

        /**
         * This method is used to challenge another player
         * @param player String name of the player to oppose
         * @param game String name of the game you want to play
         * @throws Exception when unable to send data
         */
        public void challenge(String player, String game) throws Exception {
            waitForMyCommand = true;
            synchronized (lock){
                dataOut.println("challenge " + "\"" + player + "\"" + " " +  "\"" + game +  "\"");
                MessageHandler.waitForOk(dataIn);
                waitForMyCommand = false;
                lock.notify();
            }

        }

        /**
         * This method is used to forfeit your current game
         * @throws IOException
         */
        public void forfeit() throws IOException {
            waitForMyCommand = true;
            synchronized (lock) {
                dataOut.println("forfeit");
                dataIn.readLine();
                waitForMyCommand = false;
                lock.notify();
            }
        }

        /**
         * This method is used to accept challenges
         * @param challenge object of the challenge you want to accept
         * @throws Exception when unable to send data
         */
        public void acceptChallenge(Challenge challenge) throws Exception {
            waitForMyCommand = true;
            synchronized (lock){
                dataOut.println("challenge accept " + challenge.getChallengeNumber());
                MessageHandler.waitForOk(dataIn);
                waitForMyCommand = false;
                lock.notify();
            }
        }

        /**
         * This method safes the current playername serverip and port to the config file
         */
        public void saveConfig() {
            ConfigHandler config = ConfigHandler.getInstance();
            config.saveConfig(serverIp, ""+serverPort, playerName, isAI);
        }

        /**
         * This mehtod is used to check if the server is connected
         * @return boolean connected
         */
        public boolean isConnected() {
            return connected;
        }

        /**
         * This method is used to indicate if you want to play as AI or not
         * @param isAI boolean that indicates if you are playing with ai
         */
        public void setIsAI(boolean isAI) { this.isAI = isAI; }

        /**
         * This method is used to get the serverIp
         * @return string serverIp
         */
        public String getServerIp() {
            return serverIp;
        }

        /**
         * This method is used to get the current server port
         * @return int port number
1         */
        public int getServerPort() {
            return serverPort;
        }

        /**
         * This method is used to set the serverIp
         * @param serverIp String serverip
         */
        public void setServerIp(String serverIp) {
            this.serverIp = serverIp;
        }

        /**
         * This method is used to set the serport
         * @param serverPort int port number
         */
        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        /**
         * This method is used to get the your current name
         * @return string the playername
         */
        public String getPlayerName(){return playerName;}

        /**
         * This method is used to check if you are playing to ai
         * @return boolean Ai
         */
        public boolean getIsAI() { return isAI; }

        /**
         * This method is used to stop the run loop
         */
        public void quit() { this.quit = true; }


}