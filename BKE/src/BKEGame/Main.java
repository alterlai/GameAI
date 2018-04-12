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
 * Useless object used for independent testing of module..
 */
public class Main implements Observer {

    public static void main(String[] args) throws InterruptedException, ExecutionException{

        Main main = new Main();
        main.threadComparison();
    }

    public void threadComparison() throws InterruptedException, ExecutionException {
        int amount = 3;
        int threadpoolsize = 4;
        int depth = 3;

        Player p1 = new Player("p1", true);
        Player p2 = new Player("p2", true);

        long noThreadTime;
        long avgNoThreadTime;
        long threadTime;
        long avgThreadTime;


        System.out.println("Depth: "+  depth +"\nGametype: Othello \nAmount of games to simulate: "+amount+"\nThread pool size: "+threadpoolsize+"\n--------------");
        System.out.println("\nStarting new test without multithreading:");
        long[] gameLengths = new long[amount];
        long loopStart = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            GameInterface gameNoThread = new OthelloNoThreads(p1, p2, depth);
            long gameStart = System.currentTimeMillis();
            boolean cont = true;
            while (cont) {
                int nomove = 0;

                Move move = gameNoThread.findBestMove(p1);
                if (move != null) {
                    gameNoThread.playMove(move);
                }
                else { nomove++;}

                Move move2 = gameNoThread.findBestMove(p2);
                if (move2 != null) {
                    gameNoThread.playMove(move2);
                }
                else {nomove++;}
                if (nomove == 2) { cont = false;}

            }
            long gameEnd = System.currentTimeMillis();
            long gameLength = gameEnd - gameStart;
            gameLengths[i] = gameLength;
            System.out.println("Game "+ (i + 1) + " without multi-threading took: " + gameLength + " ms.");
        }
        long loopEnd = System.currentTimeMillis();
        long loopLength = loopEnd - loopStart;
        noThreadTime = loopLength;
        System.out.println("Playing 10 games without multi-threading took: " + loopLength + " ms.");
        long sum = 0;
        for (long time : gameLengths) {
            sum += time;
        }
        avgNoThreadTime = sum / amount;
        System.out.println("On average the games took: " + avgNoThreadTime + " ms.");

        System.out.println("\n\n\nStarting new test with multithreading:");
        gameLengths = new long[amount];
        loopStart = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            GameInterface game = new Othello(p1, p2, depth, threadpoolsize);
            long gameStart = System.currentTimeMillis();
            boolean cont = true;
            while (cont) {
                int nomove = 0;

                Move move = game.findBestMove(p1);
                if (move != null) {
                    game.playMove(move);
                }
                else { nomove++;}

                Move move2 = game.findBestMove(p2);
                if (move2 != null) {
                    game.playMove(move2);
                }
                else {nomove++;}
                if (nomove == 2) { cont = false;}

            }
            long gameEnd = System.currentTimeMillis();
            long gameLength = gameEnd - gameStart;
            gameLengths[i] = gameLength;
            System.out.println("Game "+ (i + 1) + " with multi-threading took: " + gameLength + " ms.");
        }
        loopEnd = System.currentTimeMillis();
        loopLength = loopEnd - loopStart;
        threadTime = loopLength;
        System.out.println("Playing 10 games with multi-threading took: " + loopLength + " ms.");
        sum = 0;
        for (long time : gameLengths) {
            sum += time;
        }
        avgThreadTime = sum / amount;
        System.out.println("On average the games took: " + avgThreadTime + " ms.");

        System.out.println("\n \nTime without threading: " + noThreadTime + " ms.\nTime with threading: " + threadTime + " ms.");
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
