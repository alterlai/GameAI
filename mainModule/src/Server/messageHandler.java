package Server;

import java.io.BufferedReader;

public class MessageHandler implements MessageHandlerInterface {

    public static void handleMessage(String message) throws Exception {
        if(message.startsWith("SVR GAME")) {
            GameMessageHandler.handleMessage(message);
        }
        else if(message.startsWith("ERR")){
            ErrorMessageHandler.handleMessage(message);
        }
        else if(message.startsWith("OK")){
            System.out.println("I lost a OK");
        }
        else{
            throw new Exception("unkown message");
        }
    }

    public static void waitForOk(BufferedReader dataIn) throws Exception {
        String data = dataIn.readLine();
        while (!data.equals("OK")) {
            System.out.println(data);
            MessageHandler.handleMessage(data);
            if (data.startsWith("ERR")) {
                break;
            }
            data = dataIn.readLine();
        }
    }
}
