package edu.caltech.cs2.project01;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SubstitutionCipherSolver {


    public static String fileToString(String f) {
        String lines = "";

        File file = new File(f);
        try{
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()) {
                lines += scan.nextLine();
            }
            scan.close();
        } catch(FileNotFoundException err) {
            err.printStackTrace();
        }
        return lines;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Type a sentence to decrypt: ");
        String ciphertext = scan.nextLine();


        String words = fileToString("cryptogram.txt");

        String lines = "";
        for(int i=0; i < words.length(); i++) {
            if(Character.isUpperCase(words.charAt(i))) {
                lines += words.charAt(i);
            }
        }

        QuadGramLikelihoods likelihoods = new QuadGramLikelihoods();
        SubstitutionCipher best = new SubstitutionCipher(ciphertext);//ciphertext);
        for (int i = 0; i < 20; i ++) {
            SubstitutionCipher cipher = best.getSolution(likelihoods);
            if (cipher.getScore(likelihoods) > best.getScore(likelihoods)) {
                best = cipher;
            }
        }
        System.out.println(best.getPlainText());
    }





}