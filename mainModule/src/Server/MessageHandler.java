package Server;

import Controller.GameController;

import java.io.BufferedReader;

public class MessageHandler implements MessageHandlerInterface {

    private static boolean lastCommandAnswer = false;


    //Server should have a reference to the game controller when a game is ongoing, but it should not be given as a parameter here.. Gamecontroller is irrelevant when in lobby-mode.
    public static void handleMessage(String message, GameController controller) throws Exception {
        if(message.startsWith("SVR GAME")) {
            GameMessageHandler.handleMessage(message, controller);
        }
        else if(message.startsWith("ERR")){
            lastCommandAnswer = false;
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
            new MessageHandler().handleMessage(data, new GameController()); //Uhh.. see the comments above.
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
