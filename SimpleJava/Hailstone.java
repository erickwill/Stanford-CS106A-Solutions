/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
    public void run() {
        int counter = 0;
        print("Enter a number: ");
        int num = readInt();
        
        while(num != 1) {
            if(num % 2 == 0) {
                int temp = num;
                num = num / 2;
                println(temp + " is even, so I take half: " + num);
            } else {
                int temp = num;
                num = (3 * num) + 1;
                println(temp + " is odd, so I make 3n + 1: " + num);
            }
            counter++;
        }
        println("The process took " + counter + " to reach 1");
    }
}

