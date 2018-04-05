package Server;

import java.util.ArrayList;
import java.util.Arrays;

public class messageHandler {

    /*public messageHandler(String message){
        this.message = message;
    }*/

    public static void handleMessage(String message) throws Exception {
        System.out.println(message);
        if(message.startsWith("SVR GAME CHALLENGE {")) {
            System.out.println("I am challenged");
            return;
        }
        else if(message.startsWith("SVR GAME CHALLENGE CANCELLED")){
            System.out.println("I am no longer challenged");
            return;
        }
        else if(message.startsWith("SVR GAME MATCH")){
            System.out.println("I got a match!!!");
            return;
        }
        else if (message.startsWith("SVR GAME YOURTURN")){
            System.out.println("It's my turn");

            return;
        }
        else if (message.startsWith("SVR GAME MOVE")){
            System.out.println("This was a move");
            return;
        }
        else if (message.startsWith("SVR GAME WIN")){
            System.out.println("I win!!!");
            return;
        }
        else if (message.startsWith("SVR GAME LOSS")){
            System.out.println("I lose :(");
            return;
        }
        else if (message.startsWith("SVR GAME DRAW")){
            System.out.println("I'm more even then the other guy");
            return;
        }
        else{
            throw new Exception("unkown message");
        }
    }

}
