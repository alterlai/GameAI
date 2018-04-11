package OtherControllers;

import Game.Game;
import BKEGame.TicTacToe;
import GUI.*;
import Game.Move;
import Game.Player;

import java.io.IOException;
import java.util.Observer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import MainControllers.GameControllerInterface;
import Server.Server;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class GameController implements GameControllerInterface {
    private Game game;
    private Server server;
    private ViewActionHandler view;

    private CountDownLatch moveLatch;
    private Move selectedMove;
    private GameBoardHandler gameBoardHandler;


    public GameController(Player local, Player opponent, String nameGame){
        this.server = Server.getInstance(); //Server should be singleton.
        if(nameGame.equals("Tic-tac-toe")) {
            game = new TicTacToe(local, opponent);
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
        viewController.removeView("homeView");


        // Get view handler.
        GameBoardHandler gameBoardHandler = loader.getController();
        gameBoardHandler.setController(this);
    }

    /*public void init() {


        }
        else{
        }
    }*/


    @Override
    public void init(Player local, Player opponent, String nameGame) {

    }

    public Move getMove(Player player) throws InterruptedException {

        if (player.isAI()) {
            return game.findBestMove(player);
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

    //public void forfeit(){
    //    server.forfeit();
    //}

    public void endGame(){

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