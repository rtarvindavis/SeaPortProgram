/*
* File:  Job.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The Job class is outlined in this file.
 */
// Package
package seaportprogram4;

// Imports
import java.awt.Color;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author rebec
 */
// Job class; extends Thing class
class Job extends Thing implements Runnable {

    // Variable declaration
    // Double variable to hold duration of job
    double duration;
    // ArrayList to hold job requirements (should overlap with some of the skills of the people)
    ArrayList<String> requirements = new ArrayList<>();
    // Boolean variable to return whether job has suspended by the user
    private boolean suspendFlag;
    // Boolean variable to return whether job has canceled by the user
    private boolean cancelFlag;
    // JButton for 'Status' button; displays Enum type defined below; can be used to suspend jobs
    private JButton statusButton;
    // JButton for 'Cancel' button
    private JButton cancelButton;
    // JProgressBar for each job
    private JProgressBar progressBar;
    // Object array to hold data for row of table
    Object[] tableRow;
    // Int variable to hold row of table where data is held
    private int rowTable;

    // Enum type representing status of a thread
    enum Status {
        RUNNING, SUSPENDED, WAITING, DONE
    }
    // Creation of a Status variable; description of Status enum above
    private Status status;

    // Scanner constructor; adds Thing object (parent)
    public Job(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
        if (scanner.hasNextDouble()) {
            duration = scanner.nextDouble();
        }
        while (scanner.hasNext()) {
            requirements.add(scanner.next());
        }
        // Job status = Waiting
        status = Status.WAITING;
        // Sets suspendFlag to false
        suspendFlag = false;
    }// end Job Scanner constructor

    // Method to set up table object to display job threading data
    public void setTableObject(Object[] object, int rowTable) {
        // Sets tableRow to data in object array
        tableRow = object;
        // Fills tableRow (index 0) with port name
        tableRow[0] = ((Ship) parent).getPort().getName();
        // Fills tableRow (index 1) with ship name
        tableRow[1] = ((Ship) parent).getName();
        // String holding list of requirements
        String requirementList = "";
        // If ArrayList is not empty
        if (!requirements.isEmpty()) {
            for (int i = 0; i < (requirements.size() - 1); i++) {
                // Adds requirements to String
                requirementList += requirements.get(i) + ", ";
            }
            requirementList += requirements.get(requirements.size() - 1);
        }
        // Fills tableRow (index 2) with requirementList
        tableRow[2] = requirementList;
        this.rowTable = rowTable;
        // 'Status' button; sets text
        statusButton = new JButton("");
        // Disables 'Status' button
        statusButton.setEnabled(false);
        // tableRow index 6 - this is where resources (people) will be listed
        tableRow[6] = "";
        // 'Cancel' button; sets text
        cancelButton = new JButton("Cancel");
        // Disables 'Cancel' button
        cancelButton.setEnabled(false);
        // Fills tableRow (index 5) with 'Cancel' button
        tableRow[5] = cancelButton;
        // Fills tableRow (index 4) with 'Status' button
        tableRow[4] = statusButton;
        // Progress bar; sets setsStringPainted to true
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        // Fills tableRow (index 3) with progressBar
        tableRow[3] = progressBar;

        // Action Listener for 'Status' button
        statusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusButtonActionPerformed(evt);
            }
        }); // end Action Listener for status button

        // Action Listener for 'Cancel' button
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        }); // end Action Listener for cancel button

    }// end World Scanner constructor

    // Event handler for 'Status' button
    private void statusButtonActionPerformed(java.awt.event.ActionEvent evt) {
        toggleSuspendFlag();
    } // end statusButtonActionPerformed()

    // Event handler for 'Cancel' button
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        toggleCancelFlag();
    } // end cancelButtonActionPerformed()

    // Method to start a thread/job
    public void startJob() {
        String ThreadName = ((Ship) parent).getPort().getName() + "-" + parent.getName() + "-" + getName();
        new Thread(this, ThreadName).start();
    } // end startJob() method

    // Method to handle threads; makes use of synchronized directive
    public void run() {
        // ArrayList to hold people working on a job
        ArrayList<Person> workers = null;
        // String to hold workers
        String workerList = "";
        // Synchronized directive
        synchronized (((Ship) parent).getPort()) {
            // 'While' block
            while (true) {
                // Job status = Waiting
                showStatus(Status.WAITING);
                cancelFlag = false;
                // Nested loop
                if (((Ship) parent).isShipReady()) {
                    // Cancels job if there are not enough people with skills to cover the requirements of a job
                    if (!(((Ship) parent).getPort()).areRequirementsCovered(requirements)) {
                        cancelFlag = true;
                        break;
                        // Adds people to ArrayList if they have requirements for job
                    } else {
                        workers = ((Ship) parent).getPort().getPeopleForRequirements(requirements);
                        if (workers != null) {
                            // If ArrayList is not empty
                            if (!workers.isEmpty()) {
                                for (Person mp : workers) {
                                    // Assigns ship to person
                                    mp.assignShip(((Ship) parent));
                                    // Adds name of workers to workerList
                                    workerList += mp.getName() + ", ";
                                }
                                workerList = workerList.substring(0, workerList.length() - 2);
                            }
                            break;
                        }

                    }
                }
                // Try & Catch block - InterrupedException
                try {
                    // Wait() method called here
                    ((Ship) parent).getPort().wait();
                } catch (InterruptedException e) {
                }
            }
            if (!cancelFlag) {
                // Job status = Running
                showStatus(Status.RUNNING);
                // Enables 'Status' button
                statusButton.setEnabled(true);
                // Enables 'Cancel' button
                cancelButton.setEnabled(true);
                // Sets data of tableRow (index 6) to worker names
                tableRow[6] = workerList;
            }
            // Updates table - rows corresponding to indices 1, 4, 5, 6
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 1);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 4);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 5);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 6);
        }

        // Time variable holding current time
        long time = System.currentTimeMillis();
        // Start time for Job running
        long startTime = time;
        // Duration of a Job used to set stop time for a Job
        long stopTime = time + 1000 * (long) duration;
        // Variable holding the amount of time needed for a Job to run
        double timeNeeded = stopTime - time;
        // While current time is less than stop time and the 'Cancel' button has not been selected
        while (time < stopTime && !cancelFlag) {
            // Try & Catch - InterruptedException
            try {
                // Thread goes to sleep/temporarily pause execution for a period of time
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            // If the 'Status' button has not been selected (i.e., job has not been suspended)
            if (!suspendFlag) {
                // Job status = Running
                showStatus(Status.RUNNING);
                // Adds 100 ms to current time
                time += 100;
                // Sets value of progressBar to the percentage of job completed
                progressBar.setValue((int) (((time - startTime) / timeNeeded) * 100));
            } else {
                // Job status = Suspended
                showStatus(Status.SUSPENDED);
            }
            // Updates table - row corresponding to index 3
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 3);
        }
        // Sets value of progressBar to 100 when completed
        progressBar.setValue(100);
        // Disables 'Status' button
        statusButton.setEnabled(false);
        // Disables 'Cancel' button
        cancelButton.setEnabled(false);
        // Synchronized directive
        synchronized (((Ship) parent).getPort()) {
            // Job status = Done
            showStatus(Status.DONE);
            if (workers != null) {
                for (Person mp : workers) {
                    // Unassigns person to ship
                    mp.unassignShip();
                }
            }
            // Saves current Port and Dock for future use after Ship's parent is set to null
            SeaPort currentPort = ((Ship) parent).getPort();
            Dock currentDock = ((Dock) ((Ship) parent).getParent());
            // Data in rows corresponding to index 6 cleared
            tableRow[6] = "";
            // Updates table - rows corresponding to indices 3, 4, 5, 6
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 3);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 4);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 5);
            ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 6);
            // 'While' loop to handle cases when a Ship is finished; placed in 'while' loop instead
            // of 'if' statement to handle when next Ship has no Jobs, thus is finished immediately
            while (currentDock.ship.isShipFinished()) {
                // Method to process next Ship is called
                currentDock.processNextShip();
                // Handles when there is no new Ship to move in from the Queue; prevents NullPointerException
                if (currentDock.ship == null) {
                    break;
                }
            }
            // notifyAll() method used to notify the port that Jobs have all been processed
            currentPort.notifyAll();
        }
    } // end run() method

    // Method to set suspendFlag variable to true; when 'Status' button is selected to suspend a Job
    public void toggleSuspendFlag() {
        suspendFlag = !suspendFlag;
    } // end toggleSuspendFlag() method

    // Method to set cancelFlag variable to true; when 'Cancel' button is selected to cancel a Job
    public void toggleCancelFlag() {
        cancelFlag = true;
    } // end toggleCancelFlag() method

    // Method to show Job status
    private void showStatus(Status st) {
        status = st;
        switch (status) {
            // Job status = Running
            case RUNNING:
                statusButton.setBackground(Color.GREEN);
                statusButton.setText(" Running ");
                break;
            // Job status = Suspended
            case SUSPENDED:
                statusButton.setBackground(Color.YELLOW);
                statusButton.setText("Suspended");
                break;
            // Job status = Waiting
            case WAITING:
                statusButton.setBackground(Color.ORANGE);
                statusButton.setText(" Waiting ");
                break;
            // Job status = Done
            case DONE:
                statusButton.setBackground(Color.RED);
                statusButton.setText("  Done   ");
                break;
        }
        ((World) ((Ship) parent).getPort().getParent()).updateTable(rowTable, 4);
    } // end showStatus() method

    // Method to return whether a job is finished
    public boolean isFinished() {
        return (status == Status.DONE);
    } // end isFinished() method

    // Method to return whether a job is busy; prevents another thread/job from starting
    public boolean isBusy() {
        return ((status == Status.RUNNING) || (status == Status.SUSPENDED));
    } // end isBusy() method

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "Job:" + super.infoString();
        string += "\n        Duration: " + duration;
        if (requirements.size() == 0) {
            return string;
        }
        string += "\n        Requirements:";
        for (String requirement : requirements) {
            string += "\n          ~ " + requirement;
        }
        return string;
    } // end toString() method

}// end Job class

