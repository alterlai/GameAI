package BKEGame;


import Game.GameInterface;
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


        GameInterface gamu = new Othello(p1, p2);
        gamu.registerView(this);
        Scanner s = new Scanner(System.in);


        boolean cont = true;
        while (cont) {
            //gamu.playMove(new Move(29, p1));
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

            //gamu.getBoard().print();

        }
        //System.out.println(move.getPos() + "h");
    /*
        while (true) {

            Move m = gamu.findBestMove(p1);
            gamu.playMove(m);

            String move = s.nextLine();
            String[] split = move.split("");
            int x = new Integer(split[0]);
            int y = new Integer(split[1]);


            Move pmove =  gamu.createMove(x, y, p2);
            if (gamu.isValid(pmove)) {
                gamu.playMove(pmove);
            }
            else { System.out.println("Move not valid.."); break;}


            //Playing move based on 2 dimensional position.. made by BKEGame.GameInterface objects or in the controller..
            //Move pmove = ((BKEGame.TicTacToe) gamu).createMove(x, y,  p2); //deze hoort in gamu


            ///gamu.playMove(pmove);

            //Playing moves based on 1 dimensional position.. made by server



        }*/


            //TTT.playMove(new Move(4, p2));
            //TTT.playMove(new Move(5, p2));
            //TTT.playMove(new Move(6, p2));
            //TTT.playMove(new Move(7, p2));
            //char[] xy = gamu.getBoard().getCells1D();
            //for (int i = 0; i < (9); i++){
                //System.out.println("Pos " + i + ": " + xy[i]);
            //}
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
