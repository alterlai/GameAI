package Server;

import GUI.ViewController;
import Game.Move;
import Game.Player;
import MainControllers.GameControllerInterface;
import OtherControllers.GameController;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMessageHandler implements MessageHandlerInterface {
    private static boolean isAI = false;

    private static GameControllerInterface gameController;

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
            gameController = GameHandler.getInstance().getGameController();
            Server.getInstance().doMove(gameController.getMove(gameController.getPlayer(Server.getInstance().localPlayer)));
            return;
        }
        else if (message.startsWith("SVR GAME MOVE")){
            System.out.println("This was a move");
            processMove(message);
            return;
        }
        else if (message.startsWith("SVR GAME WIN")){
            //gameController.endGame(true);
            System.out.println("I win!!!");
            GameHandler.getInstance().getGameController().endGame();
            return;
        }
        else if (message.startsWith("SVR GAME LOSS")){
            System.out.println("I lose :(");
            //gameController.endGame(false);
            GameHandler.getInstance().getGameController().endGame();
            return;
        }
        else if (message.startsWith("SVR GAME DRAW")){
            System.out.println("I'm more even then the other guy");
            GameHandler.getInstance().getGameController().endGame();
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
        GameControllerInterface gameController = GameHandler.getInstance().getGameController();
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.substring(15, message.length() - 1).split(",")));
        String playername = list.get(0).substring(9, list.get(0).length()-1);
        int move = Integer.parseInt(list.get(1).substring(8, list.get(1).length()-1));
        if (list.get(2).substring(11, list.get(2).length()-1).equals("Illegal move")){
            System.out.println(playername + " tried to cheat with Illegal move " + move);
            return;
        }


        if (playername.equals(gameController.getPlayer(1).getName())){
            gameController.moveSucces(new Move(move, gameController.getPlayer(1)));
        }
        else{
            gameController.moveSucces(new Move(move, gameController.getPlayer(2)));
        }
    }

    private static void setupMatch(String message){
        LobbyObservable.getInstance().stop();
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(message.substring(16, message.length() - 1).split(",")));
        Player player1 = new Player(Server.getInstance().getPlayerName(), true /*isAI*/);
        Player player2 = new Player(list.get(2).substring(12, list.get(2).length()-1));
        String nameGame = list.get(1).substring(12, list.get(1).length()-1);
        GameHandler handler = GameHandler.getInstance();

        if (message.contains("PLAYERTOMOVE: \""+ Server.getInstance().getPlayerName() +"\"")) {
            handler.initGameController(player1, player2, nameGame);
            Server.getInstance().localPlayer = 1;
        }
        else {
            System.out.println("PLAYERTOMOVE: \""+ Server.getInstance().getPlayerName() +"\" ");
            handler.initGameController(player2, player1, nameGame);
            Server.getInstance().localPlayer = 2;
        }

    }

    public static void setisAI(Boolean isAI){GameMessageHandler.isAI = isAI;}
    public static boolean getisAI(){return GameMessageHandler.isAI;}
}
