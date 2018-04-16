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

/**
 * Game controller. Takes care of the communication between the server, view and the game.
 */
public class GameController implements GameControllerInterface {
    private GameInterface game;
    private Server server;

    private CountDownLatch moveLatch; //Used for registering player input
    private Move selectedMove; //Last move that the player has selected

    private GameBoardHandler gameBoardHandler; //Handler of the gameboard view
    private Player localPlayer; //The non-remote player

    /**
     * Initializes the game controller. Sets up a game and initalizes the views.
     * @param starter starting Player
     * @param opponent not-starting Player
     * @param nameGame game that is going to be played
     */
    public GameController(Player starter, Player opponent, String nameGame){
        this.server = Server.getInstance();
        if (starter.getName().equals(server.getPlayerName())){
            localPlayer = starter;
        }
        else{
            localPlayer = opponent;
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

    /**
     * Initializes the game board view
     * @throws IOException
     */
    public void initView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/GameBoard.fxml"));
        Parent gameView =loader.load();
        ViewController viewController = ViewController.getInstance();
        viewController.addView("gameView", gameView);
        viewController.activate("gameView");
        //viewController.removeView("homeView");


        // Get view handler.
        GameBoardHandler gameBoardHandler = null;
        try {
            gameBoardHandler = (GameBoardHandler) ViewHandlers.getInstance().getHandler("GameBoardHandler");
        } catch (HandlerNotRegisterdException e) {
            e.printStackTrace();
        }
        //GameBoardHandler gameBoardHandler = loader.getController();
        gameBoardHandler.setController(this, game);
        gameBoardHandler.setPlayerNames(game.getPlayer1().getName(), game.getPlayer2().getName());

    }

    /**
     * Retrieves a move from the local player, either AI or not-AI
     * @return legal move that the local player is going to play
     * @throws InterruptedException
     */
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
        return selectedMove;

    }

    /**
     * Called when the server confirms that a move has been succesfully played, moves from local and remote players. Plays the move on the local model.
     * @param move
     */
    public void moveSucces(Move move){
        game.playMove(move);
    }

    public void forfeit(){
        //TODO server forfeit functionality
    }

    /**
     * Makes the view show the end game message.
     * @param
     */
    public void endGame(int status){
        try {
            gameBoardHandler = (GameBoardHandler)ViewHandlers.getInstance().getHandler("GameBoardHandler");
        } catch (HandlerNotRegisterdException e) {
            e.printStackTrace();
        }
        if (status == -1) {
            gameBoardHandler.showEndScreen("You have lost! \nClick on OK to return to the lobby.");
        } else if (status == 0 ) {
            gameBoardHandler.showEndScreen("Draw. \nClick on OK to return to the lobby.");
        } else {
            gameBoardHandler.showEndScreen("You have won! \nClick on OK to return to the lobby");
        }

        new Thread(LobbyObservable.getInstance()).start();
    }

    /**
     * Subscribes the observing view to the observable game
     * @param view
     */
    public void registerView(Observer view) {
        game.registerView(view);
    }

    /**
     * Registers user input, called from pane action handlers. If it's legit it saves it until it's requested by the server object.
     * @param pos one dimensional position of the clicked cell
     */
    public void registerMove(int pos){
        Move move = new Move(pos, new Player(Server.getInstance().getPlayerName())); //NO!
        if (game.isValid(move)) {
            selectedMove = move;
            moveLatch.countDown();
        }
    }

    /**
     * @param number player number
     * @return the requested player associated with the in-progress game
     */
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