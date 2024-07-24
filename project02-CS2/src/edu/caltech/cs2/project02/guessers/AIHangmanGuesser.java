package edu.caltech.cs2.project02.guessers;


import edu.caltech.cs2.project02.interfaces.IHangmanGuesser;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class AIHangmanGuesser implements IHangmanGuesser {
  private static String fileName = "data/scrabble.txt";
  private static TreeMap<Character, Integer> charReapeats = new TreeMap<Character, Integer>();

  @Override
  //patter
  public char getGuess(String pattern, Set<Character> guesses) throws FileNotFoundException {

    ArrayList<String> allWords = new ArrayList<>();
    ArrayList<String> possibleWords = new ArrayList<>();
    //


    Scanner s =  new Scanner(new File(fileName));


    while(s.hasNext()) {
      String next = s.next();
      if(next.length() == pattern.length()){
        allWords.add(next);
      }

    }



    for(int i = 0; i < allWords.size(); i++) {
      int num = 0;
      for(int j = 0; j < allWords.get(0).length(); j++) {
        if(pattern.charAt(j) == '-') {
          if(!guesses.contains((allWords.get(i).charAt(j)))) {
            num++;
          }
        }
        if(allWords.get(i).charAt(j) == (pattern.charAt(j))) {
          num++;
        }

      }
      if(num == pattern.length()) {
        possibleWords.add(allWords.get(i));
      }
    }

    char ch;

    for(ch = 'a'; ch<= 'z'; ch++) {
      int num = 0;
      if (!guesses.contains(ch)) {
        for (int i = 0; i < possibleWords.size(); i++) {
          for (int j = 0; j < possibleWords.get(i).length(); j++) {
            if (possibleWords.get(i).charAt(j) == ch) {
              num++;
            }
          }
        }
      }
      charReapeats.put(ch, num);
    }


    int num = 0;
    char c = ' ';
    for (Character key : charReapeats.keySet()) {
      if(charReapeats.get(key) > num) {
        num = charReapeats.get(key);
        c = key;
      }
    }

    return c;
  }
}

