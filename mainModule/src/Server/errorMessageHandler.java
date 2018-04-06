package Server;

public class errorMessageHandler implements messageHandlerInterface {
    public static void handleMessage(String message) throws Exception {
        if(message.startsWith("SVR GAME CHALLENGE {")) {
            System.out.println("I am challenged");
            return;
        }
        else if(message.startsWith("SVR GAME CHALLENGE CANCELLED")){
            System.out.println("I am no longer challenged");
            return;
        }
        else{
            System.out.println("I need to implement this message");
        }
    }
}
