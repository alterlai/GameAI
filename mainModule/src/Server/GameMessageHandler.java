package Server;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMessageHandler implements MessageHandlerInterface {

    public static void handleMessage(String message) throws Exception {
        if(message.startsWith("SVR GAME CHALLENGE {")) {
            handleChallengeMessage(message);
            System.out.println("I am challenged");
            return;
        }
        else if(message.startsWith("SVR GAME CHALLENGE CANCELLED")){
            handleChallengeCancel(message);
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

    private static void handleChallengeMessage(String message) {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.substring(20, message.length() - 1).split(",")));
        String playerName = list.get(0);
        String game = list.get(2);
        int challengeNumber = Integer.parseInt(list.get(1).substring(19, list.get(1).length()-1));
        playerName = playerName.substring(13, playerName.length()-1);
        game = game.substring(12, game.length()-1);
        LobbyObservable.getInstance().addChallenge(new Challenge(playerName, game, challengeNumber));
    }

    private static void handleChallengeCancel(String message){
        LobbyObservable.getInstance().removeChallenge(Integer.parseInt(message.substring(48, message.length()-2)));
    }
}
