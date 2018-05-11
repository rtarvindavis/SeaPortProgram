/*
* File:  Ship.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The Ship class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// Ship class; extends Thing class
class Ship extends Thing {

    // Variable declaration
    // Double to hold draft value of ship
    double draft;
    // Double to hold length value of ship
    double length;
    // Double to hold weight value of ship
    double weight;
    // Double to hold width value of ship
    double width;
    // ArrayList to hold jobs
    ArrayList<Job> jobs = new ArrayList<>();

    // Scanner constructor; adds Thing object (parent)
    public Ship(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        if (scanner.hasNextDouble()) {
            weight = scanner.nextDouble();
        }
        if (scanner.hasNextDouble()) {
            length = scanner.nextDouble();
        }
        if (scanner.hasNextDouble()) {
            width = scanner.nextDouble();
        }
        if (scanner.hasNextDouble()) {
            draft = scanner.nextDouble();
        }
    }// end Ship Scanner constructor

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = super.infoString();
        string += "\n        draft: " + draft;
        string += "\n        length: " + length;
        string += "\n        weight: " + weight;
        string += "\n        width: " + width;
        return string;
    } // end toString method

    //  Method to sort Jobs by Name; uses Collections.sort() method
    public void sortJobsByName() {
        Collections.sort(jobs);
    } // end sortJobsByName() method

    //  Method to sort Jobs by Duration; uses Collections.sort() method; creates new comparator
    public void sortJobsByDuration() {
        Collections.sort(jobs, new JobDurationComparator());
    } // end sortJobsByDuration() method

    //  Method to change Parent for the moving Ships (from Queue) to Docks
    public void changeParent(Thing newParent) {
        parent = newParent;
    } // end changeParent() method

    // Method to return Port
    public SeaPort getPort() {
        if (parent instanceof Dock) {
            return ((SeaPort) parent.getParent());
        } else if (parent != null) {
            return ((SeaPort) parent);
        } else {
            return null;
        }
    } // end getPort() method

    // Method to return whether a Ship is finished processing all Jobs or has no Jobs to process
    public boolean isShipFinished() {
        boolean isFinished = true;
        for (Job mj : jobs) {
            isFinished &= mj.isFinished();
        }
        return isFinished;
    } // end isShipFinished() method

    // Method to return whether a Ship is ready to process Jobs
    public boolean isShipReady() {

        if (parent instanceof Dock) {
            boolean isBusy = false;
            for (Job mj : jobs) {
                isBusy |= mj.isBusy();
            }
            return !isBusy;
        } else {
            return false;
        }
    } // end isShipReady() method

}// end Ship class

// Comparator class for Duration values
class JobDurationComparator implements Comparator<Job> {

    public int compare(Job j1, Job j2) {
        return Double.compare(j1.duration, j2.duration);
    }
} // end JobDurationComparator class
