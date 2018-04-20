package Server;

import java.io.BufferedReader;

/**
 * <H1> MessageHandler</H1>
 * This class handels messages and passes them to the appropiate handler
 * @author Rudolf Klijnhout
 * @version 1.0
 * @since 16-04-2018
 **/

public class MessageHandler implements MessageHandlerInterface {

    private static boolean lastCommandAnswer = false;

    /**
     * This method is used to handle the messages
     * @param message String server message
     * @throws Exception if the is a unkown message
     */
    public static void handleMessage(String message) throws Exception {
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
        }
    }

    /**
     * This method is used to wait for an ok after a command and handels in between messages
     * @param dataIn BufferedReader that is the dataIn for the connection
     * @throws Exception if it is not able to read a new line
     */
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
