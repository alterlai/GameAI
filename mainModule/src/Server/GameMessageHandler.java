package Server;

import Controller.GameController;
import com.sun.xml.internal.ws.api.message.Message;
import game.Move;
import game.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class GameMessageHandler implements MessageHandlerInterface {



    //Message Handler can't be static as it should be tracking connected players and such. Instance variables could be moved to Server, maybe. In that case the handlers could stay static..
    public static void handleMessage(String message, GameController controller) throws Exception {
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
            setupMatch(message, controller);
            System.out.println("I got a match!!!");
            return;
        }
        else if (message.startsWith("SVR GAME YOURTURN")){
            System.out.println("It's my turn");
            Move move = controller.getMove(controller.getPlayer(1)); //find best move for person making move
            Server.getInstance().move(move);
            controller.moveSucces(move); //For some reason the remote server was not giving me "SVR GAME MOVE" messages for my own moves, only moves made by the remote opponent. -> no verification, bad.
            return;
        }
        else if (message.startsWith("SVR GAME MOVE")){
            System.out.println("This was a move");
            System.out.println(message); //S: SVR GAME MOVE {PLAYER: "<speler>", DETAILS: "<reactie spel op zet>", MOVE: "<zet>"}
            processMove(message, controller);
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
    private static void processMove(String message, GameController controller) {
        //S: SVR GAME MOVE {PLAYER: "<speler>", MOVE: "<zet>", DETAILS: "<reactie spel op zet>"}
        int playerIndex = 24;
        int moveIndex = message.indexOf("MOVE:");
        String name = message.substring(playerIndex, moveIndex - 3);

        int detailIndex = message.indexOf("DETAILS");
        String move = message.substring(moveIndex + new String("MOVE: ").length() + 1, detailIndex -3);

        Move playedMove;

        if (name == controller.getPlayer(1).getName()) {
            playedMove = new Move(new Integer(move), controller.getPlayer(1));
        }
        else {
            playedMove = new Move(new Integer(move), controller.getPlayer(2));
        }
        controller.moveSucces(playedMove);
    }
    public static void setupMatch(String message, GameController controller) {
        Player player1 = new Player("Mandela", true); //Should know whether the user selected AI vs remote or player vs remote.. Should also retrieve name from config.
        int p2Index = message.indexOf("OPPONENT:");
        String p2Name = message.substring(p2Index + new String("Opponent: ").length() + 1, message.length() - 1);
        Player player2 = new Player(p2Name);
        controller.init(player1, player2); //Might be easier to only pass player2 (opponent) as a parameter here. Controller would have knowledge of player name and player modus already?
    }

}
