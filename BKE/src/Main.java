import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        TicTacToe TTT = new TicTacToe();
        Scanner s = new Scanner(System.in);

        while (true) {
            TTT.findBestMove(true);
            String move = s.nextLine();
            String[] split = move.split("");
            int x = new Integer(split[0]);
            int y = new Integer(split[1]);

            Move pmove = new Move(x, y, false);

            TTT.playMove(pmove);

        }

    }
}
