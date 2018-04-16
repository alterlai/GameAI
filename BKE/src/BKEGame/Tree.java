package BKEGame;

import java.util.ArrayList;

public class Tree {
    private ArrayList<TreeNode> startroot;
    private TreeNode currentRoot;

    public void addRoot(TreeNode node){
        startroot.add(node);
    }

    public void setCurrentRoot(TreeNode node){
        startroot = null;
        currentRoot = node;
    }

    public ArrayList<TreeNode> getStartroot() {
        return startroot;
    }

    public TreeNode getCurrentRoot() {
        return currentRoot;
    }
}
