/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
    implements NameSurferConstants, ComponentListener {

    /**
    * Creates a new NameSurferGraph object that displays the data.
    */
    public NameSurferGraph() {
        addComponentListener(this);
        update();
    }
    
    /**
    * Clears the list of name surfer entries stored inside this class.
    */
    public void clear() {
        for (int i = 0; i < surfers.size(); i++) {
            remove(surfers.get(i));
        }
        surfers.clear();
        nameList.clear();
    }
    
    /* Method: addEntry(entry) */
    /**
    * Adds a new NameSurferEntry to the list of entries on the display.
    * Note that this method does not actually draw the graph, but
    * simply stores the entry; the graph is drawn by calling update.
    */
    public void addEntry(NameSurferEntry entry) {
        nameList.add(entry);
    }

    /**
    * Updates the display image by deleting all the graphical objects
    * from the canvas and then reassembling the display according to
    * the list of entries. Your application must call update after
    * calling either clear or addEntry; update is also called whenever
    * the size of the canvas changes.
    */
    public void update() {
        removeAll();
        drawGrid();
        drawNames();
    }
    
    /**
    * Iterates through the list of added names 
    * and creates the required graphical objects.
    */
    private void drawNames() {
        int nameSize = nameList.size();
        for (int i = 0; i < nameSize; i++) {
            add(createSurf(nameList.get(i), i));
        }
    }
    
    /**
    * Creates a GCompound for the given NameSurferEntry. 
    * The GCompound consists of a GLines (graph lines) 
    * and a GLabels (names + ranks).
    */
    private GCompound createSurf(NameSurferEntry entry, int colorCount) {
        colorCount = colorCount % 4;
        GCompound surfCompound = new GCompound();
        surfCompound.add(createSurfLabels(entry, colorCount));
        surfCompound.add(createSurfLines(entry, colorCount));
        surfers.add(surfCompound);
        return surfCompound;
    }
    
    /**
    * Creates a GCompound containing the GLabels for the given
    * NameSurferEntry.
    */
    private GCompound createSurfLabels(NameSurferEntry entry, int colorCount) {
        GCompound labels = new GCompound();
        // set decade divider
        int div = getWidth() / NDECADES;
        // starting x coordinate
        double startX = 0;
        
        // create label for each decade
        for (int i = 0; i < NDECADES; i++) {
            // set Y coordinate increments based on current window height
            double yIncrements = (getHeight() - (GRAPH_MARGIN_SIZE*2.0)) / MAX_RANK;
            // set Y coordinate start point based on rank
            double y = (entry.getRank(i)*yIncrements) + GRAPH_MARGIN_SIZE;
            String name = entry.getName();
            
            /*
            * If the rank is zero, pin the label
            * to the bottom of the graph and display a '*'.
            */
            if (entry.getRank(i) == 0) {
                y = (getHeight() - GRAPH_MARGIN_SIZE) - BOTTOM_PADDING;
                name += "*";
            } else {
                name += " " + entry.getRank(i);
            }
            GLabel surfLabel = new GLabel(name, startX, y);
            surfLabel.setFont("Courrier-10");
            surfLabel.setColor(getColor(colorCount));
            labels.add(surfLabel);
            // increment decade divider
            startX += div;
        }
        return labels;
    }
    
    /**
    * Creates a GCompound containing the GLines for the given
    * NameSurferEntry.
    */
    private GCompound createSurfLines(NameSurferEntry entry, int colorCount) {
        GCompound lines = new GCompound();
        // set decade divider
        int div = getWidth() / NDECADES;
        // set starting X coordinate
        double startX = 0;
        
        // create line for decade rank
        for (int i = 0; i < NDECADES-1; i++) {
            // set Y coordinate increments
            double yIncrements = (getHeight() - (GRAPH_MARGIN_SIZE*2.0)) / MAX_RANK;
            // set Y coordinate based on rank
            double y = (entry.getRank(i)*yIncrements) + GRAPH_MARGIN_SIZE;
            // if rank is zero, set Y coordinate to bottom of graph
            if (entry.getRank(i) == 0) {
                y = getHeight() - GRAPH_MARGIN_SIZE;
            }
            
            // set line end point based on next rank
            double nextY;
            // calculate Y based on next decade rank
            if (entry.getRank(i+1) == 0) {
                nextY = getHeight() - GRAPH_MARGIN_SIZE;
            } else {    
                nextY = (entry.getRank(i+1)*yIncrements) + GRAPH_MARGIN_SIZE;
            }
            //create line
            GLine surfLine = new GLine(startX, y, startX+div, nextY);
            surfLine.setColor(getColor(colorCount));
            // add line to GCompound
            lines.add(surfLine);
            // increment X coordinate decade divider
            startX += div;
        }
        return lines;
    }
    /**
    * Returns a color based on the counter.
    */
    private Color getColor(int colorCount) {
        switch(colorCount) {
            case 0: return Color.black;
            case 1: return Color.red;
            case 2: return Color.blue;
            default: return Color.magenta;
        }
    }
    
    /**
    * Draws the grid, to which the names will be added.
    */
    private void drawGrid() {
        // set decade dividers
        int div = getWidth() / NDECADES;
        // add upper and lower margin lines
        add(new GLine(0, getHeight()-GRAPH_MARGIN_SIZE, getWidth(), getHeight()-GRAPH_MARGIN_SIZE));
        add(new GLine(0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE));
        
        // draw vertical lines and decade labels
        double verticalStart = 0;
        int startDate = START_DECADE;
        for(int i = 0; i < NDECADES; i++) {
            add(new GLine(verticalStart, 0, verticalStart, getHeight()));
            add(new GLabel(Integer.toString(startDate), verticalStart + (div/4), getHeight()-(GRAPH_MARGIN_SIZE/4)));
            // increment date value
            startDate += 10;
            // increment horizontal decade divider
            verticalStart += div;
        }
    }

    /* Implementation of the ComponentListener interface */
    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentResized(ComponentEvent e) { update(); }
    public void componentShown(ComponentEvent e) { }
    
    /* Padding for the name GLabel when its rank is zero */
    private static final int BOTTOM_PADDING = 5;
    /* Stores the NameSurferEntry's for the added names */
    private ArrayList<NameSurferEntry> nameList = new ArrayList<NameSurferEntry>();
    /* Stores the GCompounds that represent the added names to the graph */
    private ArrayList<GCompound> surfers = new ArrayList<GCompound>();

}
