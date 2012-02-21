/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {

    private static final int RECT_WIDTH = 150;

    private static final int RECT_HEIGHT = 50;

    public void run() {
        int x_center = getWidth() / 2;
        int y_center = getHeight() / 2;
        
        GRect top_rect = new GRect(x_center - (RECT_WIDTH / 2), y_center - (2 * RECT_HEIGHT), RECT_WIDTH, RECT_HEIGHT);
        add(top_rect);
        
        GRect bottom_left_rect = new GRect((x_center - (RECT_WIDTH / 2)) - (RECT_WIDTH + 15), y_center, RECT_WIDTH, RECT_HEIGHT);
        add(bottom_left_rect);
        
        GRect bottom_middle_rect = new GRect(x_center - (RECT_WIDTH / 2), y_center, RECT_WIDTH, RECT_HEIGHT);
        add(bottom_middle_rect);
        
        GRect bottom_right_rect = new GRect((x_center - (RECT_WIDTH / 2)) + (RECT_WIDTH + 15), y_center, RECT_WIDTH, RECT_HEIGHT);
        add(bottom_right_rect);
        
        GLine left_line = new GLine(x_center, y_center - RECT_HEIGHT, x_center - (RECT_WIDTH + 15), y_center);
        add(left_line);
        
        GLine right_line = new GLine(x_center, y_center - RECT_HEIGHT, x_center + (RECT_WIDTH + 15), y_center);
        add(right_line);
        
        GLine middle_line = new GLine(x_center, y_center - RECT_HEIGHT, x_center, y_center);
        add(middle_line);
        
        GLabel top_label = new GLabel("Program", top_rect.getX() + (RECT_WIDTH / 2), top_rect.getY() + (RECT_HEIGHT / 2));
        top_label.move(-top_label.getWidth() / 2, top_label.getAscent() / 2);
        add(top_label);
        
        GLabel bottom_left_label = new GLabel("GraphicsProgram", bottom_left_rect.getX() + (RECT_WIDTH / 2), bottom_left_rect.getY() + (RECT_HEIGHT / 2));
        bottom_left_label.move(-bottom_left_label.getWidth() / 2, bottom_left_label.getAscent() / 2);
        add(bottom_left_label);
        
        GLabel bottom_middle_label = new GLabel("ConsoleProgram", bottom_middle_rect.getX() + (RECT_WIDTH / 2), bottom_middle_rect.getY() + (RECT_HEIGHT / 2));
        bottom_middle_label.move(-bottom_middle_label.getWidth() / 2, bottom_middle_label.getAscent() / 2);
        add(bottom_middle_label);
        
        GLabel bottom_right_label = new GLabel("DialogProgram", bottom_right_rect.getX() + (RECT_WIDTH / 2), bottom_right_rect.getY() + (RECT_HEIGHT / 2));
        bottom_right_label.move(-bottom_right_label.getWidth() / 2, bottom_right_label.getAscent() / 2);
        add(bottom_right_label);
        
        
        
        
    }
}

