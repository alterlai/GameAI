package Server;

import Game.Move;
import Game.Player;
import MainControllers.GameControllerInterface;

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
            setupMatch(message);
            return;
        }
        else if (message.startsWith("SVR GAME YOURTURN")){
            System.out.println("It's my turn");
            GameControllerInterface gameController = Server.GameHandler.getInstance().getGameController();
            Server.getInstance().doMove(gameController.getMove(gameController.getPlayer(1)));
            return;
        }
        else if (message.startsWith("SVR GAME MOVE")){
            System.out.println("This was a move");
            processMove(message);
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

    private static void processMove(String message){
        GameControllerInterface gameController = Server.GameHandler.getInstance().getGameController();
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.substring(15, message.length() - 1).split(",")));
        System.out.println(list);
        String playername = list.get(0).substring(9, list.get(0).length()-1);
        int move = Integer.parseInt(list.get(1).substring(8, list.get(1).length()-1));

        if (playername.equals(gameController.getPlayer(1).getName())){
            gameController.moveSucces(new Move(move, gameController.getPlayer(1)));
        }
        else{
            gameController.moveSucces(new Move(move, gameController.getPlayer(2)));
        }
    }

    private static void setupMatch(String message){
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.substring(16, message.length() - 1).split(",")));
        Player player1 = new Player(Server.getInstance().getPlayerName(), true);
        Player player2 = new Player(list.get(2).substring(12, list.get(2).length()-1));
        String nameGame = list.get(1).substring(12, list.get(1).length()-1);
        Server.GameHandler handler = Server.GameHandler.getInstance();
        handler.initGameController();
        GameControllerInterface gameController = handler.getGameController();;
        gameController.init(player1, player2, nameGame);
    }
}
