/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {

    private static final int sentinel = 0;
    
    public void run() {
    
        int counter = 0;
        int large = 0;
        int small = 0;
        
        println("This program finds the largest and smallest numbers.");
    
        while(true) {
            print("? ");
            int x = readInt();
            
            if (counter == 0) {
                if(x == sentinel) {
                    println("No values entered!!!");
                    return;
                }
                large = x;
                small = x;
            }
            if(x == sentinel){
                break;
            }
            
            //test greater or less than
            if( x >= large) {
                large = x;
            }
            
            if (x <= small) {
                small = x;
            }
            counter++;
        }
        
        // print the final values
        println("smallest: " + small);
        println("largest: " + large);
        
    }
}

