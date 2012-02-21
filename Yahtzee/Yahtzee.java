/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
    
    public static void main(String[] args) {
        new Yahtzee().start(args);
    }

    public void run() {
        IODialog dialog = getDialog();
        nPlayers = dialog.readInt("Enter number of players");
        while(true) {
            if (nPlayers > 0 && nPlayers <= MAX_PLAYERS) {
                break;
            // no more than MAX_PLAYERS
            } else if (nPlayers > MAX_PLAYERS) {
                nPlayers = dialog.readInt(MAX_PLAYERS + " is the maximum.\nEnter number of players ");
            } else {
                // no less than 1 player
                nPlayers = dialog.readInt("There must be at least one player.\nEnter number of players ");
            }
        }
        // store player names
        playerNames = new String[nPlayers];
        for (int i = 1; i <= nPlayers; i++) {
            playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
        }
        // initialize graphical canvas
        display = new YahtzeeDisplay(getGCanvas(), playerNames);
        // start game
        playGame();
    }

    private void playGame() {
        // player scores
        scorecard = new int[nPlayers][N_CATEGORIES];
        // keep track of finished categories
        categories = new int[nPlayers][N_CATEGORIES];
        
        // runs each round for the number of players
        for (int i = 1; i <= N_SCORING_CATEGORIES; i++) {
            for (int j = 1; j <= nPlayers; j++) {
                firstRoll(j);
                nextRoll();
                updateCategory(j);
            }
        }
        // compute and announce winner
        gameOver();
    }
    
/** Prompts the player to take their first roll, then displays the values. */
    private void firstRoll(int id) {
        display.printMessage(playerNames[id-1] + "'s turn. Click roll dice.");
        display.waitForPlayerToClickRoll(id);
        rollDice();
        display.displayDice(dice);
    }

/** Gives each die a random value from 1 to 6 inclusive. */
    private void rollDice() {
        for (int i = 0; i < N_DICE; i++) {
            dice[i] = rgen.nextInt(1,6);
        }
    }
/** Overloaded method to change the value of a single selected die. */
    private void rollDice(int die) {
        dice[die] = rgen.nextInt(1,6);
    }

/** 
* Prompts player to select the desired dice to reroll. 
* Assigns new values to selected die.
*/    
    private void nextRoll() {
        for (int i = 0; i < 2; i++) {
            display.printMessage("Select dice to reroll.");
            display.waitForPlayerToSelectDice();
            for (int j = 0; j < N_DICE; j++) {
                if (display.isDieSelected(j)) {
                    rollDice(j);
                }
            }
            display.displayDice(dice);
        }
    }
    
/** 
* Prompts player to assign their score to a category. 
* Ensures categories can only be used once. 
* Updates and displays the scorecard.
*/
    private void updateCategory(int id) {
        display.printMessage("Choose a category.");
        while(true) {
            int selectedCategory = display.waitForPlayerToSelectCategory();
            // check if category is empty
            if (categories[id-1][selectedCategory-1] == 0) {
                // check if dice values are valid for selected category
                boolean valid = validCategory(dice, selectedCategory);
                // calculate the score based on valid/invalid category
                int score = calculateScore(selectedCategory, dice, valid);
                // log finished category
                categories[id-1][selectedCategory-1] = 1;
                // update scorecard
                tallyScorecard(selectedCategory, id, score);
                display.updateScorecard(selectedCategory, id, score);
                break;
            }
            display.printMessage("Category already filled. Please choose another.");
        }
    }

/** Totals and displays the scorecard for the given player. */
    private void tallyScorecard(int category, int id, int score) {
        scorecard[id-1][category-1] = score;
        int upper = 0;
        int lower = 0;
        int sum = 0; 
        
        // sum upper score
        for (int i = ONES; i <= SIXES; i++) {
            upper += scorecard[id-1][i-1];
            display.updateScorecard(UPPER_SCORE, id, upper);
        }
        scorecard[id-1][UPPER_SCORE-1] = upper;
        
        // check if bonus should be awarded
        if (upper >= BONUS_THRESHOLD) {
            display.updateScorecard(UPPER_BONUS, id, BONUS_VALUE);
            scorecard[id-1][UPPER_BONUS-1] = BONUS_VALUE;
            display.updateScorecard(UPPER_BONUS, id, BONUS_VALUE);
        }
        
        // sum lower score
        for (int j = THREE_OF_A_KIND; j <= CHANCE; j++) {
            lower += scorecard[id-1][j-1];
        }
        scorecard[id-1][LOWER_SCORE-1] = lower;
        display.updateScorecard(LOWER_SCORE, id, lower);
        
        // total score
        sum = upper + lower + scorecard[id-1][UPPER_BONUS-1];
        scorecard[id-1][TOTAL-1] = sum;
        display.updateScorecard(TOTAL, id, sum);
    }
    
/** Calculates the score for the given category. */
    private int calculateScore(int category, int[] dice, boolean valid) {
        int score = 0;
        
        if (!valid)
            return score;
        
        if (category >= ONES && category <= SIXES) {
            for (int i = 0; i < N_DICE; i++) {
                if (dice[i] == category)
                    score += category;
            }
        } else if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND) {
            for (int i = 0; i < N_DICE; i++) {
                score += dice[i];
            }
        } else if (category == FULL_HOUSE) {
            score = 25;
        } else if (category == SMALL_STRAIGHT) {
            score = 30;
        } else if (category == LARGE_STRAIGHT) {
            score = 40;
        } else if (category == YAHTZEE) {
            score = 50;
        } else if (category == CHANCE) {
            for (int i = 0; i < N_DICE; i++) {
                score += dice[i];
            }
        } else {
            // category does not exist!
            display.printMessage("SCORE ERROR!!!!");
        }
            return score;
    }

/** Announces the player with the highest score as the winner. */
    private void gameOver() {
        int player = 0;
        int hiScore = 0;
        for (int i = 1; i <= nPlayers; i++) {
            if (scorecard[i-1][TOTAL-1] > hiScore) {
                player = i;
                hiScore = scorecard[i-1][TOTAL-1];
            }
        }
        // announce winner
        String winner = playerNames[player-1].toUpperCase();
        display.printMessage(winner + " WINS WITH A SCORE OF " +
            scorecard[player-1][TOTAL-1] + "!!!");
    }

/** Checks if the dice values are valid for the selected category. */    
    private boolean validCategory(int[] diceValues, int category) {
        if (category == THREE_OF_A_KIND || category == FOUR_OF_A_KIND ||
            category == YAHTZEE) {
            return checkThreeFourYahtzee(diceValues, category);
        } else if (category == FULL_HOUSE) {
            return checkFullHouse(diceValues);
        } else if (category == SMALL_STRAIGHT || category == LARGE_STRAIGHT) {
            return checkStraight(diceValues, category);
        }
        return true;
    }

/** Determines if the dice configuration is a valid straight. */
    private boolean checkStraight(int[] diceValues, int category) {
        Arrays.sort(diceValues);
        int count = 0;
        for (int i = 0; i < N_DICE-1; i++) {
            if (diceValues[i+1] - diceValues[i] == 1) {
                count++;
            }
        }
        if (category == SMALL_STRAIGHT && count >= 3) {
            return true;
        } else if (category == LARGE_STRAIGHT && count == 4) {
            return true;
        } else {
            return false;
        }
    }
    
/** Checks the dice values for three of a kind, four of a kind, or yahtzee. */
    private boolean checkThreeFourYahtzee(int[] diceValues, int category) {
        // track how many dice equal each other
        for (int i = 0; i < N_DICE; i++) {
            int count = 0;
            for (int j = 0; j < N_DICE; j++) {
                if (diceValues[i] == diceValues[j]) {
                    count++;
                }
            }
            if ((category == THREE_OF_A_KIND && count >= 3) || 
               (category == FOUR_OF_A_KIND && count >= 4) || 
               (category == YAHTZEE && count == N_DICE)) {
               return true;
            }
        }
        return false;
    }

/** Determines if the dice values are a full house. */
    private boolean checkFullHouse(int[] diceValues) {
        // found three of a kind
        boolean foundThree = false;
        // found two of a kind
        boolean foundTwo = false;
        
        for (int i = 0; i < N_DICE; i++) {
            int count = 0;
            for (int j = 0; j < N_DICE; j++) {
                if (diceValues[i] == diceValues[j]) {
                    count++;
                }
            }
            if (count == 3) {
                foundThree = true;
            } else if (count == 2) {
                foundTwo = true;
            }
        }
        if (foundThree && foundTwo) {
            return true;
        } else {
            return false;    
        }
    }
    
/* Private instance variables */
    private int nPlayers;
    private String[] playerNames;
    private YahtzeeDisplay display;
    private RandomGenerator rgen = new RandomGenerator();
    private int[][] scorecard;
    private int[][] categories;
    private int[] dice = new int[N_DICE];
    private int BONUS_VALUE = 35;
    private int BONUS_THRESHOLD = 63;
}
