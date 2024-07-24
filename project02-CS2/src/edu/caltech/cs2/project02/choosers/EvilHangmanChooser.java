package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class EvilHangmanChooser implements IHangmanChooser {

  private int numGuesses;


  private ArrayList<String> words = new ArrayList<String>();


  private SortedSet<Character> guess = new TreeSet<>();




    
  public EvilHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {

    this.numGuesses = maxGuesses;

    if(wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException("wrong");
    }

    Scanner s =  new Scanner(new File("data/scrabble.txt"));

    while(s.hasNext()) {
      String next = s.next();
      if(next.length() == wordLength){
        words.add(next);
      }

    }

    if(words.size() == 0) {
      throw new IllegalStateException("no");
    }
      
  }

  @Override
  public int makeGuess(char letter) {
    if(!Character.isLowerCase(letter)) {
      throw new IllegalArgumentException(("Wrong"));

    }

    if(numGuesses <= 0) {
      throw new IllegalStateException("wrong");
    }

    if(guess.contains(letter)) {
      throw new IllegalArgumentException(("wrong"));
    }

    this.guess.add(letter);


    Map<String,TreeSet<String>> familes = new TreeMap<String, TreeSet<String>>();



    for(int i = 0; i < words.size(); i++) {
      String update = words.get(0);
      for (int j = 0; j < words.get(i).length(); j++) {
        if (words.get(i).charAt(j) == letter) {
          update += letter;// words.get(i).charAt(j); //this is a change
        } else {
          update += "-";
        }

      }

      if(!familes.containsKey(update)) {
        familes.put(update, new TreeSet<String>());
        TreeSet<String> secertWord = familes.get(update);
        secertWord.add(words.get(i));
        familes.put(update, secertWord);

      } else {
        TreeSet<String> word = familes.get(update);
        word.add(words.get(i));
        familes.put(update, word);

      }
    }


    int num = 0;
    for (String key : familes.keySet()) {
      if(num < familes.get(key).size()) {
        num = familes.get(key).size();
        words = new ArrayList<String>(familes.get(key));
      }

    }

    int occurence = 0;
    int length = words.get(0).length();

    for(int i = 0; i < length; i++) {
      if(words.get(0).charAt(i) == letter) {
        occurence ++;
      }

    }
    if(occurence == 0) {
      numGuesses = numGuesses - 1;
    }
    return occurence;
  }

  @Override
  public boolean isGameOver() {
    if(numGuesses == 0) {
      return true;
    }

    int num = 0;
    String word = words.get(0);
    for(int i = 0; i < word.length(); i++) {
      if(guess.contains(word.charAt(i))) {
        num += 1;
      }
    }

    if(num == word.length()) {
      return true;
    }


    return false;
  }

  @Override
  public String getPattern() {
    String pattern = "";
    String word = words.get(0);
    for(int i = 0; i < word.length(); i++) {
      if(guess.contains(word.charAt(i))) {
        pattern += word.charAt(i);
      } else {
        pattern += "-";
      }

    }


    return pattern;
  }

  @Override
  public SortedSet<Character> getGuesses() {

    return this.guess;
  }

  @Override
  public int getGuessesRemaining() {
    return this.numGuesses;
  }

  @Override
  public String getWord() {
    numGuesses = 0;
    return words.get(0);
  }
}