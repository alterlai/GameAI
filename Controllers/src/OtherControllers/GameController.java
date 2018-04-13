package OtherControllers;

import BKEGame.Othello;
import Game.GameInterface;
import BKEGame.TicTacToe;
import GUI.*;
import Game.Move;
import Game.Player;
import Server.LobbyObservable;

import java.io.IOException;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import MainControllers.GameControllerInterface;
import Server.Server;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class GameController implements GameControllerInterface {
    private GameInterface game;
    private Server server;
    private ViewActionHandler view;

    private CountDownLatch moveLatch;
    private Move selectedMove;
    private GameBoardHandler gameBoardHandler;
    private Player localPlayer;


    public GameController(Player starter, Player opponent, String nameGame){
        this.server = Server.getInstance();
        if (starter.getName().equals(server.getPlayerName())){
            localPlayer = starter;
            System.out.println("white power");
        }
        else{
            localPlayer = opponent;
            System.out.println("I am black");
        }
        if(nameGame.equals("Tic-tac-toe")) {
            game = new TicTacToe(starter, opponent);
        }
        else if (nameGame.equals("Reversi")) {
            game = new Othello(starter, opponent);
        }
        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GameBoard.fxml"));
        Parent gameView =loader.load();
        ViewController viewController = ViewController.getInstance();
        viewController.addView("gameView", gameView);
        viewController.activate("gameView");
        //viewController.removeView("homeView");


        // Get view handler.
        GameBoardHandler gameBoardHandler = loader.getController();
        gameBoardHandler.setController(this, game);
        gameBoardHandler.setPlayerNames(game.getPlayer1().getName(), game.getPlayer2().getName());

    }

    @Override //Deprecated
    public void init(Player local, Player opponent, String nameGame) {

    }

    public Move getMove() throws InterruptedException {

        if (localPlayer.isAI()) {
            try {
                return game.findBestMove(localPlayer);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        selectedMove = null;
        moveLatch = new CountDownLatch(1);
        moveLatch.await(10, TimeUnit.SECONDS);
        if (selectedMove == null) {
            //Handling of no input, server makes you lose..

        }
        return selectedMove;

    }

    public void moveSucces(Move move){
        game.playMove(move);

    }

    public void forfeit(){
        //TODO server forfeit functionality
    }

    /**
     * Let the view show the end game message.
     * @param //win
     */
    public void endGame(/*boolean win*/){
        /*if (win) {
            gameBoardHandler.showEndScreen("You have won! \nClick on OK to return to the lobby.");
        } else {
            gameBoardHandler.showEndScreen("You have lost!. \nClick on OK to return to the lobby.");
        }*/
        ViewController.getInstance().activate("homeView");
        ViewController.getInstance().removeView("gameView");
        new Thread(LobbyObservable.getInstance()).start();
    }

    public void registerView(Observer view) { //heh
        game.registerView(view);
    }

    public void registerMove(int pos){
        Move move = new Move(pos, new Player(Server.getInstance().getPlayerName())); //NO!
        if (game.isValid(move)) {
            selectedMove = move;
            moveLatch.countDown();
        }
    }

    public Player getPlayer(int number) {
        switch(number) {
            case 1:
                return game.getPlayer1();

            case 2:
                return game.getPlayer2();

        }
        return null;
    }
}