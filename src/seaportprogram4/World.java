/*
* File:  World.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The World class is outlined in this file.
 */
// Package
package seaportprogram4;

// Imports
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.JTable;

/**
 *
 * @author rebec
 */
// World class; extends Thing class
class World extends Thing {

    // Variable declaration
    // ArrayList to hold sea ports
    ArrayList<SeaPort> ports = new ArrayList<>();
    // JTable to display job threads information in the 'People' table
    JTable peopleTable;
    // JTable to display job threads information in the 'Jobs' table
    JTable jobsTable;
    // 2D array that holds data for the 'People' table
    Object[][] peopleTableData;
    // 2D array that holds data for the 'Jobs' table
    Object[][] jobsTableData;
    // Object of inner class used for table
    TableModelClass peopleModel;
    // Object of inner class used for table
    TableModelClass jobsModel;

    // Scanner constructor; adds Thing object (parent)
    public World(Scanner scanner, String name, Thing parent) {
        super(scanner, name, parent);
    }// end World Scanner constructor

    // Empty, no-argument constructor
    public World() {
    }// end World constructor

    // Method that processes lines from scanner and creates the HashMaps
    public void fillWorld(Scanner scanner) {
        // Declaration of HashMaps
        HashMap<Integer, SeaPort> portHashMap = new HashMap<>();
        HashMap<Integer, Dock> dockHashMap = new HashMap<>();
        HashMap<Integer, Ship> shipHashMap = new HashMap<>();
        HashMap<Integer, Person> personHashMap = new HashMap<>();
        HashMap<Integer, Job> jobHashMap = new HashMap<>();
        // Int variable to hold index
        int lineIndex = 0;
        // Int variable to hold parent
        int lineParent = 0;
        // String variable to hold name
        String lineName;
        // String variable to hold type
        String lineType;

        // 'While' loop; while scanner has another line to process
        while (scanner.hasNextLine()) {
            // Creation of Scanner object
            Scanner scanLine = new Scanner(scanner.nextLine());
            // If there are no lines to process
            if (!scanLine.hasNext()) {
                continue;
            }
            // Type
            lineType = scanLine.next();
            // Name
            lineName = scanLine.next();
            if (scanLine.hasNextInt()) {
                // Index value
                lineIndex = scanLine.nextInt();
            }
            if (scanLine.hasNextInt()) {
                // Parent value
                lineParent = scanLine.nextInt();
            } // end 'while' loop

            // Switch statement used to handle the lines from the file and create HashMaps
            switch (lineType) {
                // For:  Port
                case "port":
                    // Adding to portHashMap
                    portHashMap.put(lineIndex, new SeaPort(scanLine, lineName, this));
                    break;
                // For:  Dock
                case "dock":
                    // Adding to dockHashMap
                    dockHashMap.put(lineIndex, new Dock(scanLine, lineName, portHashMap.get(lineParent)));
                    // Getting parent from portHashMap
                    portHashMap.get(lineParent).docks.add(dockHashMap.get(lineIndex));
                    break;
                // For:  Passenger Ship (pship)
                case "pship":

                    // If dockHashMap contains parent key
                    if (dockHashMap.containsKey(lineParent)) {
                        // Adding to shipHashMap
                        shipHashMap.put(lineIndex, new PassengerShip(scanLine, lineName, dockHashMap.get(lineParent)));
                        // Grabbing parent from dockHashMap
                        dockHashMap.get(lineParent).ship = shipHashMap.get(lineIndex);
                    } else {
                        // Adding to shipHashMap
                        shipHashMap.put(lineIndex, new PassengerShip(scanLine, lineName, portHashMap.get(lineParent)));
                        // Grabbing parent from portHashMap
                        portHashMap.get(lineParent).que.add(shipHashMap.get(lineIndex));
                        // Grabbing parent from portHashMap
                        portHashMap.get(lineParent).ships.add(shipHashMap.get(lineIndex));
                    }
                    break;
                // For:  Cargo Ship (cship)
                case "cship":
                    // If dockHashMap contains parent key
                    if (dockHashMap.containsKey(lineParent)) {
                        // Adding to shipHashMap
                        shipHashMap.put(lineIndex, new CargoShip(scanLine, lineName, dockHashMap.get(lineParent)));
                        // Grabbing parent from dockHashMap
                        dockHashMap.get(lineParent).ship = shipHashMap.get(lineIndex);
                    } else {
                        // Adding to shipHashMap
                        shipHashMap.put(lineIndex, new CargoShip(scanLine, lineName, portHashMap.get(lineParent)));
                        // Grabbing parent from portHashMap
                        portHashMap.get(lineParent).que.add(shipHashMap.get(lineIndex));
                        // Grabbing parent from portHashMap
                        portHashMap.get(lineParent).ships.add(shipHashMap.get(lineIndex));
                    }
                    break;
                // For:  Person
                case "person":
                    // Adding to personHashMap
                    personHashMap.put(lineIndex, new Person(scanLine, lineName, portHashMap.get(lineParent)));
                    // Grabbing parent from portHashMap
                    portHashMap.get(lineParent).persons.add(personHashMap.get(lineIndex));
                    break;
                // For:  Job
                case "job":
                    // Adding to jobHashMap

                    jobHashMap.put(lineIndex, new Job(scanLine, lineName, shipHashMap.get(lineParent)));
                    // Grabbing parent from shipHashMap
                    shipHashMap.get(lineParent).jobs.add(jobHashMap.get(lineIndex));
                    break;
                default:
                    break;
            } // end switch statement
        } // end 'while' loop

        // Adds Ships in Dock to list of Ships in Port
        portHashMap.forEach((k, v) -> {
            v.docks.forEach(vd -> v.ships.add(vd.ship));
        });
        ports.addAll(portHashMap.values());

    } // end fillWorld method

    // Method to start the threading process of all Jobs in the text file
    public void startJobs(JPanel progressPanel) {
        // Variable to hold # of jobs
        int numJobs = 0;
        // Nested 'for' loop to increment numJobs
        for (SeaPort mp : ports) {
            for (Ship ms : mp.ships) {
                for (Job mj : ms.jobs) {
                    numJobs++;
                }
            }
        }
        // Variable to hold # of persons
        int numPersons = 0;
        // Nested 'for' loop to increment numPersons
        for (SeaPort mp : ports) {
            for (Person mpp : mp.persons) {
                numPersons++;
            }
        }
        // jobsTableData - new 2D object array
        jobsTableData = new Object[numJobs][7];
        // peopleTableData = new 2D object array
        peopleTableData = new Object[numPersons][4];
        // Incrementer
        int i = 0;
        // Fills peopleTableData
        for (SeaPort mp : ports) {
            for (Person mpp : mp.persons) {
                mpp.setRow(i);
                peopleTableData[i][0] = mpp.getParent().getName();
                peopleTableData[i][1] = mpp;
                peopleTableData[i][2] = mpp.getSkill();
                peopleTableData[i][3] = mpp.getShipName();
                i++;
            }
        }
        // Incrementer
        i = 0;
        // Fills jobsTableData
        for (SeaPort mp : ports) {
            for (Ship ms : mp.ships) {
                for (Job mj : ms.jobs) {
                    mj.setTableObject(jobsTableData[i], i);
                    i++;
                }
            }
        }
        // String array holding table columns
        String[] stringArrayJobsTable = {"Port", "Ship", "Requirements", "Progress", "Status", "Cancel", "Resources"};
        // String array holding table columns
        String[] stringArrayPeopleTable = {"Port", "Person", "Skill", "Ship"};
        // Creation of new TableModelClass object
        peopleModel = new TableModelClass(jobsTableData, stringArrayJobsTable);
        // Creation of new TableModelClass object
        jobsModel = new TableModelClass(peopleTableData, stringArrayPeopleTable);
        // Creation of JTable to hold information (for 'People' table)
        peopleTable = new JTable(peopleModel);
        // Adds a Mouse Listener to the table
        peopleTable.addMouseListener(new JTableButtonMouseListener(peopleTable));
        // Creation of JTable to hold information (for 'Jobs' table)
        jobsTable = new JTable(jobsModel);
        // Disables reordering of table columns
        peopleTable.getTableHeader().setReorderingAllowed(false);
        jobsTable.getTableHeader().setReorderingAllowed(false);
        // Sets whether or not table is always made large enough to fill the height of an enclosing viewport
        peopleTable.setFillsViewportHeight(true);
        jobsTable.setFillsViewportHeight(true);
        // Sets font of text in table
        peopleTable.setFont(new java.awt.Font("Monospaced", 0, 12));
        jobsTable.setFont(new java.awt.Font("Monospaced", 0, 12));

        // Connects custom renderer CustomContainerRenderer to JButton and JProgressBar classes
        peopleTable.setDefaultRenderer(JProgressBar.class, new CustomContainerRenderer());
        peopleTable.setDefaultRenderer(JButton.class, new CustomContainerRenderer());

        // Creation of JPanel
        JPanel outputPanel = new JPanel();
        // Sets layout of outputPanel to BorderLayour
        outputPanel.setLayout(new BorderLayout());
        // Sets layout of progressPanel to BoxLayout
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        // JLabel for 'Jobs' table header
        JLabel jobsLabel = new JLabel("Jobs");
        // JPanel for each 'Jobs' table header
        JPanel jobLabelPanel = new JPanel();
        // Adds jobsLabel to jobLabelPanel
        jobLabelPanel.add(jobsLabel);
        // JLabel for 'People' table header
        JLabel peopleLabel = new JLabel("People");
        // JPanel for each 'People' table header
        JPanel peopleLabelPanel = new JPanel();
        // Adds peopleLabel to peopleLabelPanel
        peopleLabelPanel.add(peopleLabel);
        // Creation of JScrollPane; adds personTable
        JScrollPane scrollPane = new JScrollPane(peopleTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Adds scrollPane to outputPanel
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        // Adds jobsLabelPanel to progressPanel
        progressPanel.add(jobLabelPanel);
        // Adds outputPanel to progressPanel
        progressPanel.add(outputPanel);
        // Creation of JScrollPane; adds jobTable
        JScrollPane jobScrollPane = new JScrollPane(jobsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // JPanel for 'People' table
        JPanel peoplePanel = new JPanel();
        // Sets layout of peoplePanel to BorderLayout
        peoplePanel.setLayout(new BorderLayout());
        // Adds jobScrollPane to peoplePanel
        peoplePanel.add(jobScrollPane, BorderLayout.CENTER);
        // Adds peopleLanelPanel to progressPanel
        progressPanel.add(peopleLabelPanel);
        // Adds peoplePanel to progressPanel
        progressPanel.add(peoplePanel);

        // Nested 'for' loop
        for (SeaPort mp : ports) {
            for (Dock md : mp.docks) {
                // Checks if the Ship's job list is empty
                while (md.ship.jobs.isEmpty()) {
                    // processNextShip() called here to process the next Ship in the Queue
                    md.processNextShip();
                }
            }
            // Nested 'for' loop
            for (Ship ms : mp.ships) {
                for (Job mj : ms.jobs) {
                    // startJob() method called here to begin the job
                    mj.startJob();
                }
            }
        }
        // validate() method called to display everything
        progressPanel.validate();
        // Sets visibility of progressPanel to true
        progressPanel.setVisible(true);

    } // end startJobs() method

    // Method used by subclasses to print object information; replaces old toString() method
    @Override
    public String infoString() {
        String string = "";
        for (SeaPort mPort : ports) {
            string += mPort.infoString();
        }
        return string;
    } // end infoString() method

    // Method to find Person by Name; contains nested loops
    public ArrayList<Person> findPersonByName(String name) {
        // ArrayList to hold list of people
        ArrayList<Person> listPersons = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Person mPerson : mPort.persons) {
                if (mPerson.name.equalsIgnoreCase(name)) {
                    listPersons.add(mPerson);
                }
            }
        }
        return listPersons;
    } // end findPersonByName method

    // Method to find Person By Skill; contains nested loops
    public ArrayList<Person> findPersonBySkill(String skill) {
        // ArrayList to hold list of people
        ArrayList<Person> listPersons = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Person mPerson : mPort.persons) {
                if (mPerson.getSkill().equalsIgnoreCase(skill)) {
                    listPersons.add(mPerson);
                }
            }
        }
        return listPersons;
    } // end findPersonBySkill

    // Method to find Job By Name; contains nested loops
    public ArrayList<Job> findJobByName(String name) {
        // ArrayList to hold list of jobs
        ArrayList<Job> listJobs = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Ship mShip : mPort.ships) {
                for (Job mJob : mShip.jobs) {
                    if (mJob.name.equalsIgnoreCase(name)) {
                        listJobs.add(mJob);
                    }
                }
            }
        }
        return listJobs;
    }// end findJobByName

    // Method to find Job By Requirement; contains nested loops
    public ArrayList<Job> findJobByRequirement(String requirement) {
        // ArrayList to hold list of jobs
        ArrayList<Job> listJobs = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Ship mShip : mPort.ships) {
                for (Job mJob : mShip.jobs) {
                    for (String mRequirement : mJob.requirements) {
                        if (mRequirement.equalsIgnoreCase(requirement)) {
                            listJobs.add(mJob);
                        }
                    }
                }
            }
        }
        return listJobs;
    } // end findJobByRequirement 

    // Method to find Ship By Name; contains nested loops
    public ArrayList<Ship> findShipByName(String name) {
        // ArrayList to hold list of ships
        ArrayList<Ship> listShips = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Ship mShip : mPort.ships) {
                if (mShip.name.equalsIgnoreCase(name)) {
                    listShips.add(mShip);
                }
            }
        }
        return listShips;
    } // end findShipByName method

    // Method to find Sea Port By Name; contains nested loops
    public ArrayList<SeaPort> findPortByName(String name) {
        // ArrayList to hold list of ports
        ArrayList<SeaPort> listPorts = new ArrayList<>();
        for (SeaPort mPort : ports) {
            if (mPort.name.equalsIgnoreCase(name)) {
                listPorts.add(mPort);
            }
        }
        return listPorts;
    } // end findPortByName method

    // Method to find Sea Port By Ship Name; contains nested loops
    public ArrayList<SeaPort> findPortByShip(String ship) {
        // ArrayList to hold list of ports
        ArrayList<SeaPort> listPorts = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Ship mShip : mPort.ships) {
                if (mShip.name.equalsIgnoreCase(ship)) {
                    listPorts.add(mPort);
                }
            }
        }
        return listPorts;
    } // end findPortByShip method

    // Method to find Sea Port By Person Name; contains nested loops
    public ArrayList<SeaPort> findPortByPerson(String person) {
        // ArrayList to hold list of ports
        ArrayList<SeaPort> listPorts = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Ship mShip : mPort.ships) {
                for (Person mPerson : mPort.persons) {
                    if (mPerson.name.equalsIgnoreCase(person)) {
                        listPorts.add(mPort);
                    }
                }
            }
        }
        return listPorts;
    } // end findPortByPerson method

    // Method to find Dock By Name; contains nested loops
    public ArrayList<Dock> findDockByName(String name) {
        // ArrayList to hold list of docks
        ArrayList<Dock> listDocks = new ArrayList<>();
        for (SeaPort mPort : ports) {
            for (Dock mDock : mPort.docks) {
                if (mDock.name.equalsIgnoreCase(name)) {
                    listDocks.add(mDock);
                }
            }
        }
        return listDocks;
    } // end findDockByName method

    // Method to sort Ports by Name; uses Collections.sort() method
    public void sortPortsByName() {
        Collections.sort(ports);
    } // end sortPortsByName() method

    // Method to sort Docks by Name; calls sortDocksByName() method
    public void sortDocksByNamePorts() {
        ports.forEach(vp -> vp.sortDocksByName());
    } // end sortDocksByNamePorts() method

    // Method to sort People by Name; calls sortPeopleByName() method
    public void sortPeopleByNamePorts() {
        ports.forEach(vp -> vp.sortPeopleByName());
    } // end sortPeopleByNamePorts() method

    // Method to sort People by Skill; calls sortPeopleBySkill() method
    public void sortPeopleBySkillPorts() {
        ports.forEach(vp -> vp.sortPeopleBySkill());
    } // end sortPeopleBySkillPorts() method

    // Method to sort Jobs by Name; calls sortJobsNameAllShips() method
    public void sortJobsByNameShips() {
        ports.forEach(vp -> vp.sortJobsNameAllShips());
    } // end sortJobsByNameShips() method

    // Method to sort Jobs by Duration; calls sortJobsDurationAllShips() method
    public void sortJobsByDurationShips() {
        ports.forEach(vp -> vp.sortJobsDurationAllShips());
    } // end sortJobsByDurationShips() method

    // Method to sort Ships in Que by Name; calls sortQueByName() method
    public void sortQueByNamePorts() {
        ports.forEach(vp -> vp.sortQueByName());
    } // end sortQueByNamePorts() method

    // Method to sort Ships in Que by Draft; calls sortQueByDraft() method
    public void sortQueByDraftPorts() {
        ports.forEach(vp -> vp.sortQueByDraft());
    } // end sortQueByDraftPorts() method

    // Method to sort Ships in Que by Length; calls sortQueByLength() method
    public void sortQueByLengthPorts() {
        ports.forEach(vp -> vp.sortQueByLength());
    } // end sortQueByLengthPorts() method

    // Method to sort Ships in Que by Weight; calls sortQueByWeight() method
    public void sortQueByWeightPorts() {
        ports.forEach(vp -> vp.sortQueByWeight());
    } // end sortQueByWeightPorts() method

    // Method to sort Ships in Que by Width; calls sortQueByWidth() method
    public void sortQueByWidthPorts() {
        ports.forEach(vp -> vp.sortQueByWidth());
    } // end sortQueByWidthPorts() method

    // Method to sort all Ships by Name; calls sortAllShipsByName() method
    public void sortAllShipsByNamePorts() {
        ports.forEach(vp -> vp.sortAllShipsByName());
    } // end sortAllShipsByNamePorts() method

    // Method to sort all Ships by Draft; calls sortAllShipsByDraft() method
    public void sortAllShipsByDraftPorts() {
        ports.forEach(vp -> vp.sortAllShipsByDraft());
    } // end sortAllShipsByDraftPorts() method

    // Method to sort all Ships by Length; calls sortAllShipsByLength() method
    public void sortAllShipsByLengthPorts() {
        ports.forEach(vp -> vp.sortAllShipsByLength());
    } // end sortAllShipsByLengthPorts() method

    // Method to sort all Ships by Weight; calls sortAllShipsByWeight() method
    public void sortAllShipsByWeightPorts() {
        ports.forEach(vp -> vp.sortAllShipsByWeight());
    } // end sortAllShipsByWeightPorts() method

    // Method to sort all Ships by Width; calls sortAllShipsByWidth() method
    public void sortAllShipsByWidthPorts() {
        ports.forEach(vp -> vp.sortAllShipsByWidth());
    } // end sortAllShipsByWidthPorts() method

    // Method to return Ports
    public ArrayList<SeaPort> getPorts() {
        return ports;
    } // end getPorts() method

    public void updateTable(int row, int column) {
        peopleModel.setValueAt(jobsTableData[row][column], row, column);
        peopleModel.fireTableCellUpdated(row, column);
        peopleTable.repaint();
    } // end getPorts() method

    public void updatePersonTable(int row) {
        jobsModel.setValueAt(((Person) peopleTableData[row][1]).getShipName(), row, 3);
        jobsModel.fireTableCellUpdated(row, 3);
        jobsTable.repaint();
    } // end getPorts() method

}// end World class

// Class for table model; extends DefaultTableModel
class TableModelClass extends DefaultTableModel {

    // Used only to remove compiler warning
    public static final long serialVersionUID = 345;

    // Class constructor
    public TableModelClass(Object[][] objectArray, String[] stringArray) {
        super(objectArray, stringArray);
    } // end TableModelClass(Object[][] objectArray, String[] stringArray) constructor

    // Class constructor - empty, no argument constructor
    public TableModelClass() {
        super();
    } // end TableModelClass() constructor

    // Method used for JTable so that information is displayed appropriately; determines default renderer/editor for each cell
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    } // end getColumnClass() method

    // Method used to make table cells uneditable
    public boolean isCellEditable(int row, int column) {
        return false;
    } // end isCellEditable() method

} // end TableModelClass

// Class used to return the component in the cell; implements TableCellRenderer
class CustomContainerRenderer implements TableCellRenderer {

    // Used only to remove compiler warning
    public static final long serialVersionUID = 1204;

    // Method that returns component
    public Component getTableCellRendererComponent(
        JTable t,
        Object value,
        boolean isSelected,
        boolean hasFocus,
        int row,
        int col) {
        return (Component) value;
    } //end getTableCellRendererComponent() method

} // end CustomContainerRenderer class

// Class that implements a MouseListener; extends MouseAdapter
class JTableButtonMouseListener extends MouseAdapter {
    
    // JTable
    private final JTable table;
    
    // Class constructor; JTable is argument
    public JTableButtonMouseListener(JTable table) {
        this.table = table;
    } // end JTableButtonMouseListener(JTable table) constructor
    
    // Method to handle mouse clicks; forwards mouse clicks to JButtons
    @Override
    public void mouseClicked(MouseEvent e) {
        int column = table.getColumnModel().getColumnIndexAtX(e.getX());
        int row = e.getY() / table.getRowHeight();
        // If row and columns are valid
        if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
            Object value = table.getValueAt(row, column);
            if (value instanceof JButton) {
                // doClick() called; triggers event handlers for JButtons
                ((JButton) value).doClick();
            }
        }
    }
}
