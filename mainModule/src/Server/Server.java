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

public class Server extends Observable implements Runnable{

    private String serverIp;
    private int serverPort;
    private Socket socket;
    private volatile boolean connected;
    private BufferedReader dataIn;
    private PrintWriter dataOut;


    public Server(){
        serverIp = "localhost";
        serverPort = 7789;
    }

    public Server(String serverIp){
        this.serverIp = "serverIp";
    }

    public Server(int serverPort){
        this.serverPort = serverPort;
    }

    public Server(String serverIp, int serverPort){
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
            disconnect();
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
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = dataIn.readLine();
            System.out.println(data);
            System.out.println(dataIn.readLine());
            connected = true;
    }
    public void login(String name) throws IOException {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("login " + name);
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(dataIn.readLine());
    }
    public boolean disconnect() throws IOException, InterruptedException {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("bye");
            Thread.sleep(200);
            socket.close();
            connected = false;
            return connected;
    }

    public ArrayList<String> getGameList() throws Exception {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("get gamelist");
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data = dataIn.readLine();
            if (data.equals("OK")){
               data = dataIn.readLine();
               data = data.replaceAll("\"", "");
               System.out.println(data);
               ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(14, data.length()-1).replace(" ", "").split(",")));
               for(String s : list) {
                   System.out.println(s);
               }
                return list;
            }
            else {
                throw new Exception("no server response");
            }

    };

    public ArrayList<String> getPlayerlist() throws Exception {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("get playerlist");
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String data = dataIn.readLine();
        if (data.equals("OK")){
            data = dataIn.readLine();
            data = data.replaceAll("\"", "");
            System.out.println(data);
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(data.substring(16, data.length()-1).replace(" ", "").split(",")));
            for(String s : list) {
                System.out.println(s);
            }
            return list;
        }
        else {
            throw new Exception("no server response");
        }
    }



}
