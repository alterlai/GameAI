package Server;

import GUI.ViewController;
import Game.Move;
import Game.Player;
import MainControllers.GameControllerInterface;
import OtherControllers.GameController;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMessageHandler implements MessageHandlerInterface {

    private static GameControllerInterface gameController;

    public static void handleMessage(String message) throws Exception {
        if(message.startsWith("SVR GAME CHALLENGE {")) {
            handleChallengeMessage(message);
            return;
        }
        else if(message.startsWith("SVR GAME CHALLENGE CANCELLED")){
            handleChallengeCancel(message);
            return;
        }
        else if(message.startsWith("SVR GAME MATCH")){
            setupMatch(message);
            return;
        }
        else if (message.startsWith("SVR GAME YOURTURN")){
            gameController = GameHandler.getInstance().getGameController();
            Server.getInstance().doMove(gameController.getMove());
            return;
        }
        else if (message.startsWith("SVR GAME MOVE")){
            processMove(message);
            return;
        }
        else if (message.startsWith("SVR GAME WIN")){
            GameHandler.getInstance().getGameController().endGame(1);
            return;
        }
        else if (message.startsWith("SVR GAME LOSS")){
            GameHandler.getInstance().getGameController().endGame(-1);
            return;
        }
        else if (message.startsWith("SVR GAME DRAW")){
            GameHandler.getInstance().getGameController().endGame(0);
            return;
        }
        else{
            //throw new Exception("unkown message");
            return;
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

        Player local = new Player(Server.getInstance().getPlayerName(), Server.getInstance().getIsAI());
        Player remote = new Player(list.get(2).substring(12, list.get(2).length()-1));

        String nameGame = list.get(1).substring(12, list.get(1).length()-1);
        GameHandler handler = GameHandler.getInstance();

        if (message.contains("PLAYERTOMOVE: \""+ Server.getInstance().getPlayerName() +"\"")) {
            handler.initGameController(local, remote, nameGame);
        }
        else {
            handler.initGameController(remote, local, nameGame);
        }

    }
}
