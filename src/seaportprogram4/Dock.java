/*
* File:  Dock.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The Dock class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// Dock class; extends Thing class
class Dock extends Thing {

    // Variable declaration
    // Ship object; contains information pertaining to a Ship
    Ship ship;

    // Scanner constructor; adds Thing object (parent)
    public Dock(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        int shipIndex;
        if (scanner.hasNextInt()) {
            shipIndex = scanner.nextInt();
        }
    }// end Dock Scanner constructor

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "Dock: " + super.infoString();
        if (ship != null) {
            string += "\n  >" + ship.getName() + "\n";
        } else {
            string += "\n  >*EMPTY*\n";
        }
        return string;
    } // end toString method

    // Method to return Ship
    public Ship getShip() {
        return ship;
    } // end getShip() method

    // Method used to move the next Ship in Queue to the Dock to begin processing Jobs
    public void processNextShip() {
        ship.changeParent(null);
        ship = ((SeaPort) parent).getNextShipInQue();
        if (ship != null) {
            ship.changeParent(this);
        }
    } // end processNextShip()

}// end Dock class
