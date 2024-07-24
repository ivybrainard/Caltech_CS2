package edu.caltech.cs2.project04;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.TrieMap;
import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

public class TrieMovieAutoCompleter extends AbstractMovieAutoCompleter {
    private static ITrieMap<String, IDeque<String>, IDeque<String>> titles = new TrieMap<>((IDeque<String> s) -> s);

    public static void populateTitles() {
        titles.clear();

        for (String key : ID_MAP.keySet()) {
            IDeque<String> movTitles = new ArrayDeque<>();
            String title = key;
            int num = 0;
            for (int i = 0; i < title.length(); i++) {
                if(title.charAt(i) == ' ') {
                    String word = title.substring(num, i);
                    movTitles.addBack(word.toLowerCase());
                    num = i + 1;
                }
            }
            String newWord = title.substring(num);
            movTitles.addBack(newWord.toLowerCase());
            int size = movTitles.size();

            for (int i = 0; i < size; i++) {
                IDeque<String> suffix = new ArrayDeque<>();
                if (titles.containsKey(movTitles)) {
                    suffix = titles.get(movTitles);
                }
                //System.out.println(suffix);
                suffix.addBack(title);
                titles.put(movTitles, suffix);
                movTitles.removeFront();
            }
        }

    }

    public static IDeque<String> complete(String term) {
        IDeque<String> termList = new ArrayDeque<>();

        int num = 0;
        for (int i = 0; i < term.length(); i++) {
            if(term.charAt(i) == ' ') {
                String word = term.substring(num, i);
                termList.addBack(word.toLowerCase());
                num = i + 1;
            }
        }
        String newWord = term.substring(num);
        termList.addBack(newWord.toLowerCase());

        ICollection<IDeque<String>> completion = titles.getCompletions(termList);
        IDeque<String> choices = new ArrayDeque<>();
        for (IDeque<String> c : completion) {
            for (String movTitle : c) {
                if (!choices.contains(movTitle)) {
                    choices.addFront(movTitle);
                }
            }
        }
        return choices;
    }

}
