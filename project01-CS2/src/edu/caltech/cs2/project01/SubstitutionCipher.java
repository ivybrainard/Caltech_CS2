package edu.caltech.cs2.project01;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubstitutionCipher {
    private String ciphertext;
    private Map<Character, Character> key;

    // Use this Random object to generate random numbers in your code,
    // but do not modify this line.
    private static final Random RANDOM = new Random();

    /**
     * Construct a SubstitutionCipher with the given cipher text and key.
     * We assume that every letter in ciphertext is an upper-case
     * alphabetic letter. We also assume every letter in the key is an
     * upper-case alphabetic letter.
     * @param ciphertext the cipher text for this substitution cipher
     * @param key the map from cipher text characters to plaintext characters
     */
    public SubstitutionCipher(String ciphertext, Map<Character, Character> key) {
        this.ciphertext = ciphertext;
        this.key = key;
        // TODO: Student fill this in
    }

    /**
     * Construct a SubstitutionCipher with the given cipher text and a randomly
     * initialized key. We assume that every letter in ciphertext is an upper-case
     * alphabetic letter.
     * @param ciphertext the cipher text for this substitution cipher
     */
    public SubstitutionCipher(String ciphertext) {
        this.ciphertext = ciphertext;
        this.key = new HashMap<>();

        for(int i = 0; i < 26; i++) {
            this.key.put(CaesarCipher.ALPHABET[i], CaesarCipher.ALPHABET[i]);
        }
        for(int i = 0; i < 10000; i++) {
            //randomSwap();
            this.key = randomSwap().key;
        }


        // TODO: Student fill this in
    }

    /**
     * Returns the unedited cipher text that was provided by the user.
     * @return the cipher text for this substitution cipher
     */
    public String getCipherText() {
        // TODO: Student fill this in
        return this.ciphertext;
    }

    /**
     * Applies this cipher's key onto this cipher's text.
     * That is, each letter should be replaced with whichever
     * letter it maps to in this cipher's key. We assume that
     * every letter in the plain text is an upper-case alphabetic letter.
     * @return the resulting plain text after the transformation using the key
     */
    public String getPlainText() {

        String word = "";
        String code = this.getCipherText();
        for(int i = 0; i < code.length(); i++) {
            word += key.get(code.charAt(i));
        }
        return word;


    }

    /**
     * Returns a new SubstitutionCipher with the same cipher text as this one
     * and a modified key with exactly one random pair of characters exchanged.
     *
     * @return the new SubstitutionCipher
     */
    public SubstitutionCipher randomSwap() {
        // TODO: Student fill this in
        Map<Character, Character> newKey = new HashMap<>(key);
        //make a new map that is a copy
        //mkae sure hwen using replace if you swap it will swap again
        int randInt1 = RANDOM.nextInt(26);
        char c1 = (char)(randInt1 + 'A');

        int randInt2 = RANDOM.nextInt(26);
        char c2 = (char)(randInt2 + 'A');

        while(c1 == c2) {
            int randInt3 = RANDOM.nextInt(26);
            c2 = (char)(randInt3  + 'A');
        }
        char x = key.get(c1);
        newKey.put(c1, newKey.get(c2));
        newKey.put(c2,x);
        //key.replace(c1, c2);
        //key.replace(c2, c1);


        return new SubstitutionCipher(ciphertext, newKey);
    }

    /**
     * Returns the "score" for the "plain text" for this cipher.
     * The score for each individual quadgram is calculated by
     * the provided likelihoods object. The total score for the text is just
     * the sum of these scores.
     * @param likelihoods the object used to find a score for a quadgram
     * @return the score of the plain text as calculated by likelihoods
     */
    public double getScore(QuadGramLikelihoods likelihoods) {
        // TODO: Student fill this in
        String text = getPlainText();
        double sum  = 0;
        for(int i = 0; i < text.length() - 3; i++) {
            String quad = text.substring(i, i+4);
            sum += likelihoods.get(quad);


        }

        return sum;
    }

    /**
     * Attempt to solve this substitution cipher through the hill
     * climbing algorithm. The SubstitutionCipher this is called from
     * should not be modified.
     * @param likelihoods the object used to find a score for a quadgram
     * @return a SubstitutionCipher with the same ciphertext and the optimal
     *  found through hill climbing
     */
    public SubstitutionCipher getSolution(QuadGramLikelihoods likelihoods) {
        // TODO: Student fill this in
        //double highestScore = -1.0;
        SubstitutionCipher newCipher = new SubstitutionCipher(this.ciphertext);
        int num = 0;
        while(num < 999) {
            num++;
            SubstitutionCipher otherCipher = newCipher.randomSwap();
            double compare1 = newCipher.getScore(likelihoods);
            double compare2 = otherCipher.getScore(likelihoods);
            if(compare2 > compare1) {
                newCipher.key = otherCipher.key;
                num = 0;

            }

        }

        return newCipher;
    }
}
