package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class messageHandler {

    /*public messageHandler(String message){
        this.message = message;
    }*/

    public static void handleMessage(String message) throws Exception {
        if(message.startsWith("SVR GAME")) {
            gameMessageHandler.handleMessage(message);
            return;
        }
        else if(message.startsWith("ERR")){
            System.out.println("I need to handle a error");
            return;
        }
        else if(message.startsWith("OK")){
            System.out.println("I lost a OK");
            return;
        }
        else{
            throw new Exception("unkown message");
        }
    }

    public static void waitForOk(BufferedReader dataIn) throws Exception {
        String data = dataIn.readLine();
        while (!data.equals("OK")) {
            System.out.println(data);
            messageHandler.handleMessage(data);
            if (data.startsWith("ERR"))
                break;
            data = dataIn.readLine();
        }
    }
}
