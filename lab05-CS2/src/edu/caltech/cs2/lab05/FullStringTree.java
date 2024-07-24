package edu.caltech.cs2.lab05;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullStringTree {

    protected StringNode root;

    protected static class StringNode {
        public final String data;
        public StringNode left;
        public StringNode right;

        public StringNode(String data) {
            this(data, null, null);
        }

        public StringNode(String data, StringNode left, StringNode right) {
            this.data = data;
            this.left = left;
            this.right = right;
            // Ensures that the StringNode is either a leaf or has two child nodes.
            if ((this.left == null || this.right == null) && !this.isLeaf()) {
                throw new IllegalArgumentException("StringNodes must represent nodes in a full binary tree");
            }
        }

        // Returns true if the StringNode has no child nodes.
        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    protected FullStringTree() {}

    public FullStringTree(Scanner in) {
       this.root = deserialize(in);
    }



    private StringNode deserialize(Scanner in) {
        String word = in.nextLine();
        if(word.charAt(0) == 'I') {
            int num = word.indexOf(" ");
            StringNode tree = new StringNode(word.substring(num + 1), deserialize(in), deserialize(in));
            return tree;
        } else {
            int num = word.indexOf(" ");
            StringNode tree = new StringNode(word.substring(num + 1), null, null);
            return tree;
        }

    }

    public List<String> explore() {
        ArrayList<String> list = new ArrayList<>();
        exploreHelper(root, list);
        return list;
    }

    private void exploreHelper(StringNode node, List<String> list) {
        if(node.isLeaf()) {
            String letter = "L: " + node.data;
            list.add(letter);
        } else {
            String letter = "I: " + node.data;
            list.add(letter);
            exploreHelper(node.left, list);
            exploreHelper(node.right, list);

        }

    }

    public void serialize(PrintStream output) {
        List<String> list = explore();
        for(String l : list) {
            output.println(l);
        }

    }
}