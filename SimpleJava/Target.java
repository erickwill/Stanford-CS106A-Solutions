/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {    
    public void run() {
        int center_y = getHeight()/2;
        int center_x = getWidth()/2;
        
        GOval big_oval = new GOval(center_x-72,center_y-72,144,144);
        big_oval.setFilled(true);
        big_oval.setFillColor(Color.red);
        
        GOval middle_oval = new GOval(center_x-46.8,center_y-46.8,93.6,93.6);
        middle_oval.setFilled(true);
        middle_oval.setFillColor(Color.white);
        
        GOval small_oval = new GOval(center_x-21.6,center_y-21.6,43.2,43.2);
        small_oval.setFilled(true);
        small_oval.setFillColor(Color.red);
        
        add(big_oval);
        add(middle_oval);
        add(small_oval);
    }
}
