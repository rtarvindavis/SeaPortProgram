/*
* File:  PassengerShip.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The PassengerShip class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// PassengerShip class; extends Ship class
class PassengerShip extends Ship {

    // Variable declaration
    // Int variable to hold # of occupied rooms
    int numberOfOccupiedRooms;
    // Int variable to hold # of passengers
    int numberOfPassengers;
    // Int variable to hold # of rooms
    int numberOfRooms;

    // Scanner constructor; adds Thing object (parent)
    public PassengerShip(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        if (scanner.hasNextInt()) {
            numberOfPassengers = scanner.nextInt();
        }
        if (scanner.hasNextInt()) {
            numberOfRooms = scanner.nextInt();
        }
        if (scanner.hasNextInt()) {
            numberOfOccupiedRooms = scanner.nextInt();
        }
    }// end PassengerShip Scanner constructor

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "Passenger ship:" + super.infoString();
        string += "\n        Occupied Rooms: " + numberOfOccupiedRooms;
        string += "\n        Passengers: " + numberOfPassengers;
        string += "\n        Rooms: " + numberOfRooms;
        if (jobs.size() == 0) {
            return string;
        }
        for (Job mj : jobs) {
            string += "\n\n     " + mj.infoString();
        }
        return string;
    } // end toString method

}// end PassengerShip class
