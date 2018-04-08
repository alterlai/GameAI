package Controller;

import GUI.ViewActionHandler;
import game.Move;
import game.Player;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import Server.Server;

import Game.*;

public class GameController{
    private Game game;
    private Server server;
    private ViewActionHandler view;

    private CountDownLatch moveLatch;
    private Move selectedMove;


    public GameController(/*ViewActionHandler view*/){
        this.server = Server.getInstance(); //Server should be singleton.
        //this.view = view;
    }

    public void init(Player local, Player opponent) {
        game = new TicTacToe(local, opponent);
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

    public void registermove(Move move){
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
