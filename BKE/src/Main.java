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
        Player p1 = new Player();
        Player p2 = new Player();

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

            Move pmove = new Move(x, y, p2);

            TTT.playMove(pmove);
        }
    }

    //Testing observer relationship..
    @Override
    public void update(Observable observable, Object o) {
        System.out.println("State has changed, observer was notified..");
        Board b = (Board)o;
        b.print();

    }
}
