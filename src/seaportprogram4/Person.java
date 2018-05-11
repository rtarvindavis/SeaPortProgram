/*
* File:  Person.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The Person class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// Person class; extends Thing class
class Person extends Thing {

    // Variable declaration
    // String to hold skill
    private String skill;
    // Boolean to hold whether or not person is working
    private boolean isWorking;
    // Ship object; represents the ship that the person is working on
    private Ship ship;
    // Int variable to hold row of table where data is held
    private int rowTable;

    // Scanner constructor; adds Thing object (parent)
    public Person(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        if (scanner.hasNext()) {
            skill = scanner.next();
        }
        isWorking = false;
        ship = null;
    }// end Person Scanner constructor

    // Method to assign ship and set person to working
    public void assignShip(Ship ship) {
        this.ship = ship;
        isWorking = true;
        // Updates appropriate row in 'Person' table
        ((World) parent.getParent()).updatePersonTable(rowTable);
    } // end assignShip() method
    
    // Method to unassign ship and set person to not working
    public void unassignShip() {
        ship = null;
        isWorking = false;
        // Updates appropriate row in 'Person' table
        ((World) parent.getParent()).updatePersonTable(rowTable);
    } // end setShip() method
    
    // Method to return ship name
    public String getShipName() {
        if (ship == null) {
            return "";
        }
        return ship.getName();
    } // end getShip() method

    // Method to return whether person is working on a job
    public boolean getIsWorking() {
        return isWorking;
    } // end getIsWorking() method

    // Method to return skill
    public String getSkill() {
        return skill;
    } // end getSkill() method
    
    // Method to set rowTable value
    public void setRow(int rowTable) {
        this.rowTable = rowTable;
    } // end setRow() method

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "Person:" + super.infoString();
        string += "\n        Skill: " + skill;
        return string;
    } // end toString method

}// end Person class

