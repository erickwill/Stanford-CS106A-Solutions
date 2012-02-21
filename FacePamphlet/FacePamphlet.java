/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

    public static void main(String[] args){
        new FacePamphlet().start(args);
    }

    /**
     * This method has the responsibility for initializing the 
     * interactors in the application, and taking care of any other 
     * initialization that needs to be performed.
     */
    public void init() {
        database = new FacePamphletDatabase();
        createButtons();
        addActionListeners();
        canvas = new FacePamphletCanvas();
        add(canvas);
    }
    
    /**
    * Creates interactors.
    */
    private void createButtons() {
        add(new JLabel("Name "),NORTH);
        name = new JTextField(TEXT_FIELD_SIZE);
        add(name, NORTH);
        add(new JButton("Add"), NORTH);
        add(new JButton("Delete"), NORTH);
        add(new JButton("Lookup"), NORTH);
        
        changeStatus = new JTextField(TEXT_FIELD_SIZE);
        changeStatus.setActionCommand("Change Status");
        changeStatus.addActionListener(this);
        add(changeStatus, WEST);
        add(new JButton("Change Status"), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        
        changePicture = new JTextField(TEXT_FIELD_SIZE);
        changePicture.setActionCommand("Change Picture");
        changePicture.addActionListener(this);
        add(changePicture, WEST);
        add(new JButton("Change Picture"), WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        
        addFriendToProfile = new JTextField(TEXT_FIELD_SIZE);
        addFriendToProfile.setActionCommand("Add Friend");
        addFriendToProfile.addActionListener(this);
        add(addFriendToProfile, WEST);
        add(new JButton("Add Friend"), WEST);
    }
    
    /**
    * Deletes a profile. Also removes this profile as a
    * friend from all other profiles.
    */
    private void removeFromAll(String n) {
        currentProfile = database.getProfile(n);
        if (currentProfile == null) {
            canvas.displayProfile(currentProfile);
            canvas.showMessage("A profile with the name " + n + " does not exist");
            return;
        }        
        // delete from database
        database.deleteProfile(currentProfile.getName());
        currentProfile = null;
        canvas.displayProfile(currentProfile);
        canvas.showMessage("Profile of " + n + " deleted");
    }
    
    /**
    * Creates a new profile.
    */
    private void addNewProfile() {
        if (!database.containsProfile(name.getText())) {
            currentProfile = new FacePamphletProfile(name.getText());
            database.addProfile(currentProfile);
            canvas.displayProfile(currentProfile);        
            canvas.showMessage("New profile created");
        } else {
            canvas.showMessage("A profile with name " + name.getText() + " already exists");
        }
        
    }
    
    /**
    * Changes the image for a profile.
    */
    private void changeProfileImage() {
        if (currentProfile == null) {
            canvas.showMessage("Please Select a profile to change picture");
            return;
        }
        GImage image = null;
        // attempt to open image file
        try {
            image = new GImage("images/"+changePicture.getText());
        } catch (ErrorException e) {
            canvas.showMessage("Unable to open image file: " + changePicture.getText());
            return;
        }
        currentProfile.setImage(image);
        canvas.displayProfile(currentProfile);
        canvas.showMessage("Picture updated");
    }
    
    /**
    * Creates a reciprocal friend relationship 
    * between profiles.
    */
    private void createProfileRelationship() {
        if (currentProfile == null) {
            canvas.showMessage("Please select a profile to add friend");
            return;
        }
        if (currentProfile.getName().equals(addFriendToProfile.getText())) {
            canvas.showMessage("You can't add yourself as a friend");
            return;
        }
        if (!database.containsProfile(addFriendToProfile.getText())) {
            canvas.showMessage(addFriendToProfile.getText() + " does not exist");
            return;
        // cycle through existing friends to avoid duplicates
        } else {
            Iterator<String> it = currentProfile.getFriends();
            if (it != null) {
                while(it.hasNext()) {
                    String friend = it.next();
                    if (friend.equals(addFriendToProfile.getText())) {
                        canvas.showMessage(currentProfile.getName() + " already has " + friend + " as a friend");
                        return;
                    }
                }
            }
        }
        // add friend to current profile
        currentProfile.addFriend(addFriendToProfile.getText());
        // add current profile to friend's list
        database.getProfile(addFriendToProfile.getText()).addFriend(currentProfile.getName());
        canvas.displayProfile(currentProfile);
        canvas.showMessage(addFriendToProfile.getText() + " added as a friend");
    }
    
    /**
    * Checks for and displays a user selected
    * profile.
    */
    private void lookupProfile() {
        if (database.getProfile(name.getText()) != null) {
            currentProfile = database.getProfile(name.getText());
            canvas.displayProfile(currentProfile);
            canvas.showMessage("Displaying " + currentProfile.getName());
        } else {
            currentProfile = null;
            canvas.displayProfile(currentProfile);
            canvas.showMessage("A profile with the name " + name.getText() + " does not exist");
        }
    }
    
    /**
    * Updates a profile's status message.
    */
    private void changeProfileStatus() {
        if (currentProfile == null) {
            canvas.displayProfile(currentProfile);
            canvas.showMessage("Pleas select a profile to change status");
            return;
        }
        currentProfile.setStatus(changeStatus.getText());
        canvas.displayProfile(currentProfile);
        canvas.showMessage("Status updated to: " + currentProfile.getStatus());
    }
    
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Add") && !name.getText().equals("")) {
            addNewProfile();
        } else if (cmd.equals("Delete") && !name.getText().equals("")) {
            removeFromAll(name.getText());
        } else if (cmd.equals("Lookup") && !name.getText().equals("")) {
            lookupProfile();
        } else if (cmd.equals("Change Status") && !changeStatus.getText().equals("")) {
            changeProfileStatus();
        } else if (cmd.equals("Change Picture") && !changePicture.getText().equals("")) {
            changeProfileImage();
        } else if (cmd.equals("Add Friend") && !addFriendToProfile.getText().equals("")) {
            createProfileRelationship();
        }
    }

    // profiles database
    private FacePamphletDatabase database;
    // keeps track of current profile
    private FacePamphletProfile currentProfile;
    // graphical canvas
    private FacePamphletCanvas canvas;
    
    /* interactors */
    private JTextField name;
    private JTextField changeStatus;
    private JTextField changePicture;
    private JTextField addFriendToProfile;
}
