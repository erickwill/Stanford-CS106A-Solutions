/*
 * File: Hangman.java
 * ------------------
 * Plays the game Hangman
 * by Derek Bikoff
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.util.*;
import java.awt.*;

public class Hangman extends ConsoleProgram {

/** Main method for .jar */
    public static void main(String[] args) {
        new Hangman().start(args);
    }
    
/** Intializes graphical canvas */
    public void init() {
       canvas = new HangmanCanvas();
       add(canvas);
    }

/** Creates wordlist and run program */
    public void run() {
        
        // create new lexicon
        lex = new HangmanLexicon();
        
        // begin hangman game
        startGame();
        
    }
    
/** Gets answer word from lexicon; plays game */
    public void startGame() {
        // clear graphics for new game
        canvas.reset();

        println("WELCOME TO HANGMAN!");
        
        guesses = NUMBER_OF_GUESSES;
        
        // random number for selecting word from lexicon
        RandomGenerator rgen = new RandomGenerator();
        int rand = rgen.nextInt(0, lex.getWordCount()-1);
        
        // get word from HangmanLexicon
        word = lex.getWord(rand);
        int wordLength = word.length();

        // create hint
        for (int i = 0; i < wordLength; i++) {
            gameWord += '-';
        }
        
        // game loop for getting guesses and checking game conditions
        gameLoop();
        // game finished
        gameOver();
    }

/** Tests wether a finished game is a win or a loss and asks the user to replay */    
    public void gameOver() {
        // game lost
        if (guesses == 0) {
            println("GAME OVER!!!!");
            println("The answer was: " + word);
            canvas.displayWord(word);
            replay();
        } 
        // winning game
        else {
            // display hint word
            println("The word now looks like this: " + gameWord);
            canvas.displayWord(gameWord);
            println("CONGRATULATIONS!!!! YOU'VE WON!!!");
            replay();
        }
    }
    
/** Main game loop; asks for guesses and tests them */
    public void gameLoop() {
        while (!word.equals(gameWord) && guesses != 0) {
            // display hint word
            println("The word now looks like this: " + gameWord);
            canvas.displayWord(gameWord);
            
            // print guesses remaining
            printGuesses();                                           

            // get user guess
            String yourGuess = getLetter();

            // test user guess
            checkGuess(word, yourGuess);
        }
    }
    
/** Asks user to replay game */
    public void replay() {
        String yn = readLine("Would you like to play again? Y/N ");
        yn = yn.toUpperCase();
        if (yn.equals("Y") || yn.equals("YES")) {
            println("**************");
            gameWord = "";
            startGame();
        }
        else if (yn.equals("N") || yn.equals("NO")) {
            exit();
        }
        else {
            replay();
        }
    }

/** Prints guesses remaining */
    public void printGuesses() {
        println("You have " + guesses + " guesses left.");
    }
    
/** Gets guess from user */
    public String getLetter() {
        while(true) {
            String letter = readLine("Your guess: ");
            if (letter.length() != 1)
                println("INVALID ENTRY. GUESS AGAIN.");
            else if (!Character.isLetter(letter.charAt(0)))
                println("LETTERS ONLY PLEASE.");
            else 
                return letter.toUpperCase();
        }
    }
    
/** Tests guess against answer word and change hint word if correct */
    public void checkGuess(String answer, String guessLetter) {
        if (answer.contains(guessLetter)) {
            println("That guess is correct.");
            for (int i = 0; i < answer.length(); i++) {
                if(answer.charAt(i) == guessLetter.charAt(0)){
                    gameWord = gameWord.substring(0,i) + guessLetter.charAt(0) + gameWord.substring(i+1, gameWord.length());
                }
            }
        } else {
            println("There are no " + guessLetter + "'s in the word.");
            canvas.noteIncorrectGuess(guessLetter, guesses);
            guesses--;
        }
    }

/* Canvas for graphics */
    private HangmanCanvas canvas;

/* Word list */
    private HangmanLexicon lex;

/* Number of incorrect guesses allowed */
    private static final int NUMBER_OF_GUESSES = 8;

/* Incorrect guess counter */
    private int guesses;

/* Hint word */
    private String gameWord = "";

/* Answer word */
    private String word;

}
