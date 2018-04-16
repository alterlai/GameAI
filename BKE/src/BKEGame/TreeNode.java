package BKEGame;

import Game.Move;

import java.util.ArrayList;

public class TreeNode {
    private int score;
    private Move move;
    private TreeNode parent;
    private ArrayList<TreeNode> childeren;

    public TreeNode(Move move){
        this.move = move;
    }

    public TreeNode (Move move, TreeNode parent){
        this.move = move;
        this.parent = parent;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addChild(TreeNode node){
        childeren.add(node);
    }

    public Move getMove() {
        return move;
    }

    public int getScore(){
        return score;
    }
}
