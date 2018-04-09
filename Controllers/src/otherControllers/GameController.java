package otherControllers;

import BKEGame.Game;
import BKEGame.TicTacToe;
import GUI.ViewActionHandler;
import Game.Move;
import Game.Player;
import MainControllers.GameControllerInterface;
import Server.Server;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GameController implements GameControllerInterface {
    private Game game;
    private Server server;
    private ViewActionHandler view;

    private CountDownLatch moveLatch;
    private Move selectedMove;


    public GameController(ViewActionHandler view){
        this.server = Server.getInstance(); //Server should be singleton.
        this.view = view;
    }

    public void init(Player opponent) {
        game = new TicTacToe(new Player(), opponent);
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

    public void forfeit() throws IOException {
        server.forfeit();
    }

    public void endGame(){

    }

    public void registermove(Move move){
        if (game.isValid(move)) {
            selectedMove = move;
            moveLatch.countDown();
        }
    }

}