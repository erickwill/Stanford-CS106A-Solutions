/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
    public static void main(String[] args) {
        new NameSurfer().start(args);
    }

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
    public void init() {
        data = new NameSurferDataBase(NAMES_DATA_FILE);
        graph = new NameSurferGraph();
        add(graph);
        createButtons();
        addActionListeners();
    }
/**
 * Creates Interactors
 */    
    private void createButtons() {
        add(new JLabel("Name"), SOUTH);
        
        txt = new JTextField(20);
        txt.setActionCommand("Graph");
        txt.addActionListener(this);
        add(txt, SOUTH);
        
        add(new JButton("Graph"), SOUTH);
        add(new JButton("Clear"), SOUTH);
    }

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Graph")) {
            String name = txt.getText().toUpperCase();
            NameSurferEntry entry = data.findEntry(name);
            if (entry != null) {
                graph.addEntry(entry);
                graph.update();
            }
        } else if (cmd.equals("Clear")) {
            graph.clear();
        }
    }
    
    private NameSurferGraph graph;
    private NameSurferDataBase data;
    private JTextField txt;
}
