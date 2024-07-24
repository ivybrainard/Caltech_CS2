

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class graph {


    public static void main(String[] args) throws FileNotFoundException {


        TreeMap<Integer, Integer> nodeValues = new TreeMap<>();

        try (Scanner value = new Scanner(new File("values.txt"))) {
            while(value.hasNext()) {
                String n = value.nextLine();
                int idx1 = n.indexOf("-");
                int idx2 = n.indexOf("=");
                int valNum = Integer.valueOf(n.substring(idx2+2));
                if(valNum != 0) {
                    nodeValues.put(Integer.valueOf(n.substring(idx1+1, idx2-1)), valNum);
                }
            }
        }

        String message = "";
        for(int k : nodeValues.keySet()) {
            int x = nodeValues.get(k);
            message += (char) x;
        }


        QuadGramLikelihoods likelihoods = new QuadGramLikelihoods();
        SubstitutionCipher best = new SubstitutionCipher(message);
        for(int i = 0; i < 20; i++) {
            SubstitutionCipher cipher = best.getSolution(likelihoods);
            if(cipher.getScore(likelihoods) > best.getScore(likelihoods)) {
                best = cipher;
            }
        }
        System.out.println(best.getPlainText());

//        Graphputer g = new Graphputer();
//
//        g.run("cd mmio-000000");
//        g.run("frob");
//        String listNodes = g.run("ls");
//        String[] split = listNodes.split("\t");
//
//        ArrayList<String> nodes = new ArrayList<>();
//        for(String item : split) {
//            nodes.add(item);
//        }
//
//        //listNodes = g.run("ls");
//        while(!listNodes.isEmpty()) {
//            g.run("cd " + listNodes);
//            g.run("frob");
//            listNodes = g.run("ls ");
//        }








    }








}




