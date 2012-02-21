/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import acm.util.*;
import java.io.*;
import java.util.*;

public class HangmanLexicon {

/** Creates a list of words from input file */
    public HangmanLexicon() {
        wordList = new ArrayList<String>();
        try {
            BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
            while(true) {
                String line = rd.readLine();           
                if (line == null) {
                    rd.close();
                    break;
                }
                wordList.add(line);
            }
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

/** Returns the number of words in the lexicon. */
    public int getWordCount() {
        return wordList.size();
    }

/** Returns the word at the specified index. */
    public String getWord(int index) {
        return wordList.get(index);
    }

/* Stores the list of words */
    private ArrayList<String> wordList;
    
}
