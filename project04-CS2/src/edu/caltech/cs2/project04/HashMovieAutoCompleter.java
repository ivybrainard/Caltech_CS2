package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.interfaces.IDeque;

import java.util.HashMap;
import java.util.Map;

public class HashMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static Map<String, IDeque<String>> titles = new HashMap<>();



    public static void populateTitles() {
        titles.clear();
        int count = 0;

        for (String key : ID_MAP.keySet()) {
            IDeque<String> suffixes = new ArrayDeque<>();
            String movTitle = key;
            for (int i = movTitle.length() - 1; i >= 0; i--) {
                String suff = "";
                if (movTitle.charAt(i) == ' ') {
                    count = movTitle.length();
                    suff = movTitle.substring(i + 1, count);
                    suffixes.addFront(suff);
                }
            }
            suffixes.addFront(movTitle);
            titles.put(movTitle, suffixes);
        }


    }

    public static IDeque<String> complete(String term) {
        ArrayDeque<String> choices = new ArrayDeque<>();
        HashMovieAutoCompleter checks = new HashMovieAutoCompleter();
        checks.populateTitles();


        for (String key: checks.titles.keySet()) {
            String movTitle = key;

            while (!checks.titles.get(key).isEmpty()) {
                String suffixes = checks.titles.get(key).peekBack().toLowerCase();
                String front = checks.titles.get(key).peekFront().toLowerCase();
                String spaceBack = term.toLowerCase() + " ";

                if (suffixes.contains(term.toLowerCase())) {
                    int size = term.length();
                    String word2 = suffixes.substring(0, size);
                    if(word2.equals(term.toLowerCase()) && ((word2.length() == suffixes.length()) || suffixes.contains(spaceBack))) {
                        int suffixSize = suffixes.length();
                        if(!front.substring(0, key.length() - suffixSize).contains(term.toLowerCase())) {
                            choices.addFront(movTitle);
                            break;
                        }
                    }

                }

                titles.get(key).removeBack();
            }
        }


        return choices;
    }
}
