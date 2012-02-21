/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

/** Resets the display so that only the scaffold/beam/rope/hint appears */
    public void reset() {
        // clear any existing objects for new game
        removeAll();
        // clear incorrect guesses from previous game
        guess = "";
        
        // create initial graphics (scaffold, beam, rope)
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        beam = new GLine(width-BEAM_LENGTH, height/4, width, height/4);
        
        scaff = new GLine(beam.getX(), height/4, beam.getX(), SCAFFOLD_HEIGHT-(height/4));
        GPoint beamPoint = new GPoint(beam.getEndPoint());
        double beamX = beamPoint.getX();
        double beamY = beamPoint.getY();
        
        rope = new GLine(beamX, beamY, beamX, beamY+ROPE_LENGTH);
        GPoint ropeEnd = new GPoint(rope.getEndPoint());
        ropeEndY = ropeEnd.getY();
        
        add(beam);
        add(scaff);
        add(rope);
        
        // create hint object
        hint = new GLabel("");
        hint.setFont("Ariel-20");
        add(hint);
        
        // create wrong guesses label
        wrongGuess = new GLabel("");
        add(wrongGuess);
        
    }

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
    public void displayWord(String word) {
        hint.setLabel(word);
        
        // center hint
        double hintWidth = hint.getWidth();
        hint.setLocation((getWidth()/2)-(hintWidth/2), getHeight()-wordOffset);
    }

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
    public void noteIncorrectGuess(String letter, int guessNum) {
        guess += letter;
        wrongGuess.setLabel(guess);
        double wrongGuessWidth = wrongGuess.getWidth();
        wrongGuess.setLocation((getWidth()/2)-(wrongGuessWidth/2), getHeight()-guessOffset);
        drawMan(guessNum);
    }

/** Draws body parts depending on guesses remaining */
    private void drawMan(int guessNum) {
        switch(guessNum){
            case 8: drawHead();
                    break;
            case 7: drawBody();
                    break;
            case 6: drawLeftArm();
                    break;
            case 5: drawRightArm();
                    break;
            case 4: drawLeftLeg();
                    break;
            case 1: drawRightLeg();
                    drawGameOver();
                    break;
        }
    }
    
/** Draws game over label */
    private void drawGameOver() {
        GLabel over = new GLabel("YOU'VE BEEN HANGED!!!!");
        over.setFont("Serif-bold-20");
        double overWidth = over.getWidth();
        over.setLocation((getWidth()/2) - (overWidth/2), GAME_OVER_OFFSET);
        add(over);
    }
    
/** Draws the head */
    private void drawHead() {
        double x = (getWidth() / 2) - HEAD_RADIUS;
        GOval head = new GOval(x, ropeEndY, HEAD_RADIUS*2, HEAD_RADIUS*2);
        add(head);
    }
    
/** Draws the body */
    private void drawBody() {
        double bodyY = ropeEndY + (HEAD_RADIUS*2)+1;
        bodyEndY = bodyY + BODY_LENGTH;
        body = new GLine(getWidth()/2, bodyY, getWidth()/2, bodyEndY);
        add(body);

    }
    
/** Draws the left arm (2 GLines) */
    private void drawLeftArm() {
        double x = getWidth()/2;
        double y = bodyEndY - BODY_LENGTH + (BODY_LENGTH/4);
        double xEnd = x - UPPER_ARM_LENGTH;
        GLine leftUpper = new GLine(x, y, xEnd, y);
        add(leftUpper);
        GLine leftLower = new GLine(xEnd, y, xEnd, y+LOWER_ARM_LENGTH);
        add(leftLower);
    }
    
/** Draws the right arm (2 GLines) */
    private void drawRightArm() {
        double x = getWidth()/2;
        double y = bodyEndY - BODY_LENGTH + (BODY_LENGTH/4);
        double xEnd = x + UPPER_ARM_LENGTH;
        GLine rightUpper = new GLine(x, y, xEnd, y);
        add(rightUpper);
        GLine rightLower = new GLine(xEnd, y, xEnd, y+LOWER_ARM_LENGTH);
        add(rightLower);
    }
    
/** Draws the left leg (3 GLines for hip/leg/foot) */
    private void drawLeftLeg() {
        double x = (getWidth()/2);
        GLine leftHip = new GLine(x, bodyEndY, x - HIP_WIDTH, bodyEndY);
        add(leftHip);
        GLine leftLeg = new GLine(x  - HIP_WIDTH, bodyEndY, x  - HIP_WIDTH, bodyEndY + LEG_LENGTH);
        add(leftLeg);
        GPoint leftLegEnd = new GPoint(leftLeg.getEndPoint());
        double leftLegY = leftLegEnd.getY();
        double leftLegX = leftLegEnd.getX();
        GLine leftFoot = new GLine(leftLegX, leftLegY, leftLegX - FOOT_LENGTH, leftLegY);
        add(leftFoot);
    }
    
/** Draws the right leg (3 GLines for hip/leg/foot) */
    private void drawRightLeg() {
        double x = (getWidth()/2);
        GLine rightHip = new GLine(x, bodyEndY, x + HIP_WIDTH, bodyEndY);
        add(rightHip);
        GLine rightLeg = new GLine(x  + HIP_WIDTH, bodyEndY, x  + HIP_WIDTH, bodyEndY + LEG_LENGTH);
        add(rightLeg);
        GPoint rightLegEnd = new GPoint(rightLeg.getEndPoint());
        double rightLegY = rightLegEnd.getY();
        double rightLegX = rightLegEnd.getX();
        GLine rightFoot = new GLine(rightLegX, rightLegY, rightLegX + FOOT_LENGTH, rightLegY);
        add(rightFoot);
    
    }
    
    
/* Constants for the simple version of the picture (in pixels) */
    private static final int SCAFFOLD_HEIGHT = 360;
    private static final int BEAM_LENGTH = 144;
    private static final int ROPE_LENGTH = 18;
    private static final int HEAD_RADIUS = 30;
    private static final int BODY_LENGTH = 100;
    private static final int UPPER_ARM_LENGTH = 50;
    private static final int LOWER_ARM_LENGTH = 25;
    private static final int HIP_WIDTH = 32;
    private static final int LEG_LENGTH = 65;
    private static final int FOOT_LENGTH = 20;
    private static final double GAME_OVER_OFFSET = 20;
    
/* Y coordinate for the rope end point */
    double ropeEndY;
/* Y coordinate for the body end point */
    double bodyEndY;
    
/* Graphical objects */
    private GLine beam;
    private GLine scaff;
    private GLine rope;
    private GLine body;
    private GLabel wrongGuess;
    private GLabel hint;
    
/* Stores incorrect guess letters */
    private String guess;
   
/* Y coordinate offsets for the answer and incorrect guess GLabels */
    private static final int guessOffset = 50;
    private static final int wordOffset = 100;
}
