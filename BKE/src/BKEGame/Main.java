package BKEGame;


import Game.GameInterface;
import Game.Move;
import Game.Player;

import java.util.ConcurrentModificationException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * Useless temporary object used for independent testing of module..
 */
public class Main implements Observer {

    public static void main(String[] args) throws InterruptedException, ExecutionException{

        Main main = new Main();
        main.testRun();
    }
    public void testRun()  throws InterruptedException, ExecutionException {
        Player p1 = new Player("p1", true);
        Player p2 = new Player("p2");


        GameInterface gamu = new Othello(p1, p2);
        gamu.registerView(this);
        Scanner s = new Scanner(System.in);


        long start = System.currentTimeMillis();
        boolean cont = true;
        while (cont) {
            int t = 0;
            Move move = gamu.findBestMove(p1);
            if (move != null) {
                gamu.playMove(move);
            }
            else { t++;}

            Move move2 = gamu.findBestMove(p2);
            if (move2 != null) {
                gamu.playMove(move2);
            }
            else {t++;}
            if (t == 2) { cont = false;}
        }
    }



    //Testing observer relationship..
    @Override
    public void update(Observable observable, Object o) {
        /*
        System.out.println("State has changed, observer was notified..");
        TicTacToe b = (TicTacToe)o;
        b.getBoard().print();
*/
    }
}
