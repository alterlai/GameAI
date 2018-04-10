package BKEGame;

import Game.Move;
import Game.Player;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * Useless object used for independent testing of module..
 */
public class Main implements Observer {

    public static void main(String[] args){

        Main main = new Main();
        main.testRun();
    }
    public void testRun() {
        Player p1 = new Player("p1", true);
        Player p2 = new Player("p2");

        Game TTT = new TicTacToe(p1, p2);
        TTT.registerView(this);
        Scanner s = new Scanner(System.in);

        while (true) {
            Move m = TTT.findBestMove( p1);
            TTT.playMove(m);

            String move = s.nextLine();
            String[] split = move.split("");
            int x = new Integer(split[0]);
            int y = new Integer(split[1]);


            Move pmove =  TTT.createMove(x, y, p2);
            if (TTT.isValid(pmove)) {
                TTT.playMove(pmove);
            }
            else { System.out.println("Move not valid.."); break;}

            //Playing move based on 2 dimensional position.. made by BKEGame.Game objects or in the controller..
            //Move pmove = ((BKEGame.TicTacToe) TTT).createMove(x, y,  p2); //deze hoort in ttt

            ///TTT.playMove(pmove);

            //Playing moves based on 1 dimensional position.. made by server
            TTT.playMove(new Move(4, p2));
            TTT.playMove(new Move(5, p2));
            TTT.playMove(new Move(6, p2));
            TTT.playMove(new Move(7, p2));
        }
    }

    //Testing observer relationship..
    @Override
    public void update(Observable observable, Object o) {
        System.out.println("State has changed, observer was notified..");
        TicTacToe b = (TicTacToe)o;
        b.getBoard().print();

    }
}
