/*
* File:  CargoShip.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The CargoShip class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// CargoShip class; extends Ship class
class CargoShip extends Ship {

    // Variable declaration
    // Double variable to hold cargo value
    double cargoValue;
    // Double variable to hold cargo volume
    double cargoVolume;
    // Double variable to hold cargo weight
    double cargoWeight;

    // Scanner constructor; adds Thing object (parent)
    public CargoShip(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        if (scanner.hasNextDouble()) {
            cargoWeight = scanner.nextDouble();
        }
        if (scanner.hasNextDouble()) {
            cargoVolume = scanner.nextDouble();
        }
        if (scanner.hasNextDouble()) {
            cargoValue = scanner.nextDouble();
        }
    }// end CargoShip Scanner constructor

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "Cargo ship:" + super.infoString();
        string += "\n        Cargo Value: " + cargoValue;
        string += "\n        Cargo Volume: " + cargoVolume;
        string += "\n        Cargo Weight: " + cargoWeight;
        if (jobs.size() == 0) {
            return string;
        }
        for (Job mj : jobs) {
            string += "\n\n     " + mj.infoString();
        }
        return string;
    } // end toString method

}// end CargoShip class
