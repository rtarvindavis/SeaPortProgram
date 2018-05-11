/*
* File:  Thing.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The Thing class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// Thing class; implements Comparable<Thing>
class Thing implements Comparable<Thing> {

    // Variable declaration
    // String variable to hold name
    String name;
    // Thing object to hold parent information
    Thing parent;

    // Scanner constructor; adds Thing object (parent)
    public Thing(Scanner scanner, String name, Thing parent) {
        this.name = name;
        this.parent = parent;
    }// end Thing Scanner constructor

    // Empty, no-argument constructor
    public Thing() {
    }// end Thing constructor

    // Compare object by Name
    public int compareTo(Thing o) {
        return name.compareTo(o.name);
    } // end compareTo() method

    // toString() method; modified from Project 2 (now only contains name - no parent); used to
    // display the name of the objects (Things) in the JTree
    @Override
    public String toString() {
        String string = name;
        return string;
    } // end toString() method

    // Method used by subclasses to print object information; replaces old toString() method
    public String infoString() {
        String string = " " + name;
        return string;
    } // end infoString() method

    // Method to return Name
    public String getName() {
        return name;
    } // end getName() method

    // Method to return Parent
    public Thing getParent() {
        return parent;
    } // end getParent()

}// end Thing class
