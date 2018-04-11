package Server;

import java.io.BufferedReader;

public class MessageHandler implements MessageHandlerInterface {

    private static boolean lastCommandAnswer = false;

    public static void handleMessage(String message) throws Exception {
       System.out.println(message);
        if(message.startsWith("SVR GAME")) {
            GameMessageHandler.handleMessage(message);
        }
        else if(message.startsWith("ERR")){
            lastCommandAnswer = false;
            ErrorMessageHandler.handleMessage(message);
        }
        else if(message.startsWith("OK")){
            System.out.println("I lost a OK");
        }
        else{
            System.out.println(message);
            throw new Exception("unkown message");
        }
    }

    public static void waitForOk(BufferedReader dataIn) throws Exception {
        String data = dataIn.readLine();
        while (!data.equals("OK")) {
            MessageHandler.handleMessage(data);
            if (data.startsWith("ERR")) {
                lastCommandAnswer = false;
                break;
            }
            data = dataIn.readLine();
        }
        lastCommandAnswer = true;
    }

    /**
     * Returns the status of the last message sent.
     * @return boolean
     */
    public static boolean lastMessageStatus() {
        return lastCommandAnswer;
    }
}
