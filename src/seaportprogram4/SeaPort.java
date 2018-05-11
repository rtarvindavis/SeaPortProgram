/*
* File:  SeaPort.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The SeaPort class is outlined in this file.
 */
// Package
package seaportprogram4;

// Import
import java.util.*;

/**
 *
 * @author rebec
 */
// SeaPort class; extends Thing class
class SeaPort extends Thing {

    // Variable declaration
    // ArrayList to hold docks
    ArrayList<Dock> docks = new ArrayList<>();
    // ArrayList to hold ships waiting to dock
    ArrayList<Ship> que = new ArrayList<>();
    // ArrayList to hold all ships at this port
    ArrayList<Ship> ships = new ArrayList<>();
    // ArrayList to hold people with skills at this port
    ArrayList<Person> persons = new ArrayList<>();

    // Scanner constructor; adds Thing object (parent)
    public SeaPort(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
    }// end SeaPort Scanner constructor

    // Method used by subclasses to print object information; replaces old toString() method
    public String infoString() {
        String string = "\n*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*~*\nSea Port: " + super.infoString() + "\n";
        for (Dock md : docks) {
            string += "\n>" + md.infoString() + "\n";
        }
        string += "\n\n *List of all ships in que:\n";
        if (que.isEmpty()) {
            string += "*EMPTY*\n";
        } else {
            for (Ship ms : que) {
                string += "\n>" + ms.infoString() + "\n";
            }
        }
        string += "\n\n *List of all ships:\n";
        for (Ship ms : ships) {
            string += "\n>" + ms.infoString() + "\n";
        }
        string += "\n\n *List of all persons:\n";
        for (Person mp : persons) {
            string += "\n>" + mp.infoString() + "\n";
        }
        return string;
    } // end infoString method

    // Method to sort People by Name; uses Collections.sort() method
    public void sortPeopleByName() {
        Collections.sort(persons);
    } // end sortPeopleByName() method

    // Method to sort People by Skill; uses Collections.sort() method; creates new comparator
    public void sortPeopleBySkill() {
        Collections.sort(persons, new PersonSkillComparator());
    } // end sortPeopleBySkill() method

    // Method to sort Jobs by Name; calls sortJobsByName() method
    public void sortJobsNameAllShips() {
        ships.forEach(vs -> vs.sortJobsByName());
    } // end sortJobsNameAllShips() method

    // Method to sort Jobs by Duartion; calls sortJobsByDuration() method
    public void sortJobsDurationAllShips() {
        ships.forEach(vs -> vs.sortJobsByDuration());
    } // end sortJobsDurationAllShips() method

    // Method to sort Ships in Que by Name; uses Collections.sort() method
    public void sortQueByName() {
        Collections.sort(que);
    } // end sortQueByName() method 

    // Method to sort Ships in Que by Draft; uses Collections.sort() method; creates new comparator
    public void sortQueByDraft() {
        Collections.sort(que, new ShipDraftComparator());
    } // end sortQueByDraft() method  

    // Method to sort Ships in Que by Length; uses Collections.sort() method; creates new comparator 
    public void sortQueByLength() {
        Collections.sort(que, new ShipLengthComparator());
    } // end sortQueByLength() method

    // Method to sort Ships in Que by Weight; uses Collections.sort() method; creates new comparator 
    public void sortQueByWeight() {
        Collections.sort(que, new ShipWeightComparator());
    } // end sortQueByWeight() method

    // Method to sort Ships in Que by Width; uses Collections.sort() method; creates new comparator 
    public void sortQueByWidth() {
        Collections.sort(que, new ShipWidthComparator());
    } // end sortQueByWidth() method

    // Method to sort all Ships by Name; uses Collections.sort() method; creates new comparator 
    public void sortAllShipsByName() {
        Collections.sort(ships);
    } // end sortAllShipsByName() method

    // Method to sort all Ships by Draft; uses Collections.sort() method; creates new comparator 
    public void sortAllShipsByDraft() {
        Collections.sort(ships, new ShipDraftComparator());
    } // end sortAllShipsByDraft() method

    // Method to sort all Ships by Length; uses Collections.sort() method; creates new comparator 
    public void sortAllShipsByLength() {
        Collections.sort(ships, new ShipLengthComparator());
    } // end sortAllShipsByLength() method

    // Method to sort all Ships by Weight; uses Collections.sort() method; creates new comparator 
    public void sortAllShipsByWeight() {
        Collections.sort(ships, new ShipWeightComparator());
    } // end sortAllShipsByWeight() method

    // Method to sort all Ships by Width; uses Collections.sort() method; creates new comparator 
    public void sortAllShipsByWidth() {
        Collections.sort(ships, new ShipWidthComparator());
    } // end sortAllShipsByWidth() method

    // Method to sort Docks by Name; uses Collections.sort() method; creates new comparator 
    public void sortDocksByName() {
        Collections.sort(docks);
    } // end sortDocksByName() method

    // Method to return Docks
    public ArrayList<Dock> getDocks() {
        return docks;
    } // end getDocks() method

    // Method to return Ships in Queue
    public ArrayList<Ship> getQue() {
        return que;
    } // end getQue() method

    // Method to return Ships
    public ArrayList<Ship> getShips() {
        return ships;
    } // end getShips() method

    // Method to return Persons
    public ArrayList<Person> getPersons() {
        return persons;
    } // end getPersons() method

    // Method to return the next Ship in Queue
    public Ship getNextShipInQue() {
        if (que.isEmpty()) {
            return null;
        }
        Ship ship = que.get(0);
        que.remove(0);
        return ship;
    } // end getNextShipInQue() method

    // Method to find person available for job
    public Person findAvailablePerson(String skill) {
        // ArrayList to hold list of people
        Person mPerson = null;
        // 'For' loop to check person's skill and if they are working
        for (Person mp : persons) {
            if ((mp.getSkill().equalsIgnoreCase(skill)) && (!mp.getIsWorking())) {
                mPerson = mp;
            }
        }
        return mPerson;
    } // end findPersonBySkill() method
    
    // Method to get people for job requirements
    public ArrayList<Person> getPeopleForRequirements(ArrayList<String> requirements) {
        // ArrayList of people
        ArrayList<Person> listPersons = new ArrayList<>();
        // boolean array; length is the number of requirements
        boolean[] requirementFound = new boolean[requirements.size()];
        // 'For' loop to set all values in the array to false
        for (int i = 0; i < requirementFound.length; i++) {
            requirementFound[i] = false;
        }
        // Nested loop
        for (Person mp : persons) {
            for (int i = 0; i < requirements.size(); i++) {
                // If a person's skill matches the requirement and person is not working
                if ((mp.getSkill().equalsIgnoreCase(requirements.get(i))) && (!mp.getIsWorking()) && !requirementFound[i]) {
                    // Adds person to ArrayList
                    listPersons.add(mp);
                    // Sets value in array to true
                    requirementFound[i] = true;
                    break;
                }
            }
        }
        // boolean variable to indicate whether a job's requirements are all fulfilled
        boolean requirementsCovered = true;
        for (int i = 0; i < requirementFound.length; i++) {
            requirementsCovered &= requirementFound[i];
        }
        // If job requirements are all fulfilled
        if (requirementsCovered) {
            // Return ArrayList with people working on job
            return listPersons;
        } else {
            return null;
        }
    } // end getPeopleForRequirements() method

    // Method to check whether all requirements are covered for a job (i.e., that there are enough resources to perform the job)
    public boolean areRequirementsCovered(ArrayList<String> requirements) {
        // ArrayList of people
        ArrayList<Person> listPersons = new ArrayList<>();
        // boolean array; length is the number of requirements
        boolean[] requirementFound = new boolean[requirements.size()];
        // 'For' loop to set all values in the array to false
        for (int i = 0; i < requirementFound.length; i++) {
            requirementFound[i] = false;
        }
        // Nested loop
        for (Person mp : persons) {
            // If a person's skill matches the requirement and person is not working
            for (int i = 0; i < requirements.size(); i++) {
                if ((mp.getSkill().equalsIgnoreCase(requirements.get(i))) && !requirementFound[i]) {
                    // Adds person to ArrayList
                    listPersons.add(mp);
                    // Sets value in array to true
                    requirementFound[i] = true;
                    break;
                }
            }
        }
        // boolean variable to indicate whether a job's requirements are all fulfilled
        boolean requirementsCovered = true;
        for (int i = 0; i < requirementFound.length; i++) {
            requirementsCovered &= requirementFound[i];
        }
        return requirementsCovered;
    }
        
} // end SeaPort class

// Comparator class for Skill values 
class PersonSkillComparator implements Comparator<Person> {

    public int compare(Person p1, Person p2) {
        return p1.getSkill().compareTo(p2.getSkill());
    }
} // end PersonSkillComparator class

// Comparator class for Draft values
class ShipDraftComparator implements Comparator<Ship> {

    public int compare(Ship s1, Ship s2) {
        return Double.compare(s1.draft, s2.draft);
    }
} // end ShipDraftComparator class

// Comparator class for Length values
class ShipLengthComparator implements Comparator<Ship> {

    public int compare(Ship s1, Ship s2) {
        return Double.compare(s1.length, s2.length);
    }
} // end ShipLengthComparator class

// Comparator class for Weight values
class ShipWeightComparator implements Comparator<Ship> {

    public int compare(Ship s1, Ship s2) {
        return Double.compare(s1.weight, s2.weight);
    }
} // end ShipWeightComparator class

// Comparator class for Width values
class ShipWidthComparator implements Comparator<Ship> {

    public int compare(Ship s1, Ship s2) {
        return Double.compare(s1.width, s2.width);
    }
} // end ShipWidthComparator class
