package edu.caltech.cs2.project02.choosers;

import edu.caltech.cs2.project02.interfaces.IHangmanChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class RandomHangmanChooser implements IHangmanChooser {

  private static final Random r = new Random();
  private final String word;

  private int numGuesses;

  private SortedSet<Character> guess = new TreeSet<>();

  public RandomHangmanChooser(int wordLength, int maxGuesses) throws FileNotFoundException {
    //this.word = word;
    this.numGuesses = maxGuesses;

    if(wordLength < 1 || maxGuesses < 1) {
      throw new IllegalArgumentException();

    }

    Scanner s =  new Scanner(new File("data/scrabble.txt"));

    SortedSet<String> words = new TreeSet<>();


    while(s.hasNext()) {
      String next = s.next();
      if(next.length() == wordLength){
        words.add(next);
      }
    }


    ArrayList<String> wordsList = new ArrayList<>(words);
    if(wordsList.size() == 0) {
      throw new IllegalStateException();
    }

    int randomInt = r.nextInt(words.size());
    word = wordsList.get(randomInt);
      
  }    

  @Override
  public int makeGuess(char letter) {
    if(numGuesses <= 0) {
      throw new IllegalStateException();
    }

    if(guess.contains(letter)) {
      throw new IllegalArgumentException();
    }

    if(!Character.isLowerCase(letter)) {
      throw new IllegalArgumentException();
    }

    this.guess.add(letter);

    int num = 0;
    int idx = 0;
    for(int i = 0; i < this.word.length(); i++) {
      if(word.charAt(i) == letter) {
        num += 1;
      } else {
        idx += 1;

      }

    }
    if(idx == word.length()) {
      numGuesses = numGuesses - 1;
    }

    return num;
  }

  @Override
  public boolean isGameOver() {
    ArrayList<Character> guessList = new ArrayList<Character>(guess);
    int num = 0;
    for(int i = 0; i < word.length(); i++) {
      if(guessList.contains(word.charAt(i))) {
        num += 1;
      }
    }

    if(num == word.length()) {
      return true;
    }
    if(numGuesses <= 0) {
      return true;
    }
    return false;

  }

  @Override
  public String getPattern() {
    String update = "";
    for(int i = 0; i < word.length(); i++) {
      if(this.guess.contains(word.charAt(i))) {
        update += word.charAt(i);
      } else {
        update += "-";
      }
    }

    return update;
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
    return this.word;
  }
}