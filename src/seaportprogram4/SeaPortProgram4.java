/*
* File:  SeaPortProgram4.java
* Developer:  Rebecca Davis
* Date:  February 28, 2018
* Purpose:  This program uses a GUI that allows the user to choose a text file using JFileChooser. The 
* text file has been previously created using the appropriate format specifications. The program is
* designed to create an internal structure based on the information found in the text file. The GUI displays
* the data structure in a JTree format. It also allows the user to search or sort the internal structure based
* on various criteria. Finally, the user can begin processing all jobs to demonstrate the multithreading
* functionality of the program, and the GUI displays the progress of these jobs. The user can choose to suspend
* or cancel any jobs that are currently running. Jobs can only begin when the ship to which it belongs is at a 
* dock and all of the people with required skills for the job are available. The SeaPortProgram class is outlined in this file.
 */
// Package
package seaportprogram4;

// Imports
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 *
 * @author rebec
 */
// Main class; extends JFrame
public class SeaPortProgram4 extends JFrame {
    
    // Used only to remove compiler warning
    private static final long serialVersionUID = 0;
    
    // Variable declaration - instance variables representing GUI components
    // JLabel for inputTextField
    JLabel inputLabel = new JLabel("Search For:");
    // JTextField for user input
    JTextField inputTextField = new JTextField();
    // JLabel for Search/Sort Type
    JLabel searchTypeLabel = new JLabel("Type:");
    // String arrays holding sort and search types for JComboBoxes
    String[] sortTypes = {"People", "Jobs", "Ports", "Docks", "All Ships", "Ships in Que"};
    String[] searchTypes = {"Person", "Job", "Port", "Dock", "Ships"};
    // JComboBox for Search/Sort Type options
    JComboBox<String> typeComboBox = new JComboBox<String>(searchTypes);
    // JLabel for operation to perform (Search or Sort)
    JLabel operationLabel = new JLabel("Operation:");
    // String arrays holding types for optionsComboBox when Search operation is selected
    String[] searchPersonOptions = {"Name", "Skill"};
    String[] searchJobOptions = {"Name", "Requirement"};
    String[] searchShipOptions = {"Name"};
    String[] searchPassengerShipOptions = {"Name"};
    String[] searchCargoShipOptions = {"Name"};
    String[] searchPortOptions = {"Name", "Ship Name", "Person Name"};
    String[] searchDockOptions = {"Name"};
    // JRadioButtons for Search and Sort options
    JRadioButton searchRadioButton = new JRadioButton("Search");
    JRadioButton sortRadioButton = new JRadioButton("Sort");
    // ButtonGroup for the radio buttons
    ButtonGroup radioButtonGroup = new ButtonGroup();
    // JLabel for optionsComboBox
    JLabel optionsLabel = new JLabel("Options:");
    // String arrays holding types for optionsComboBox when Sort operation is selected
    String[] sortPersonOptions = {"Name", "Skill"};
    String[] sortJobOptions = {"Name", "Duration"};
    String[] sortShipOptions = {"Name", "Draft", "Length", "Weight", "Width"};
    String[] sortPortOptions = {"Name"};
    String[] sortDockOptions = {"Name"};
    // JComboBox for Search and Sort options
    JComboBox<String> optionsComboBox = new JComboBox<String>(sortPersonOptions);
    // JButton for Search function
    JButton searchButton = new JButton("Search");
    // JTextArea to display requested information
    static JTextArea textArea = new JTextArea();
    // Creation of a JTree ti display data structure
    private JTree tree = new JTree();
    // JPanel for JTree
    private JPanel treePanel;
    // JScrollPane for output
    JScrollPane scrollPane;
    // JButton for Progress operation
    JButton progressButton = new JButton("Start");
    // JPanel for bottom (output) panel
    JPanel outputPanel = new JPanel();
    // JSplitPane used to separate JTree from text area
    JSplitPane splitPane = new JSplitPane();
    // JPanel for jobs
    JPanel jobPanel = new JPanel();
    // JTabbedPane for output; to separate 'Data Structure' from 'Jobs Progress'
    JTabbedPane tabbedPane = new JTabbedPane();
    
    // Instance variables used for overall size of GUI frame; declared private
    final int WIDTH = 925;
    final int HEIGHT = 825;

    // Creation of a JFrame for the messages being displayed to the user
    JFrame frame = new JFrame("JOptionPane showMessageDialog example");

    // Creation of a class object of World; contains information pertaining to World
    static World world = new World();

    // Class constructor for the GUI
    public SeaPortProgram4() {
        // GUI title
        super("SeaPort Application");
        // Calls setFrame() method to set the GUI frame size
        setFrame(WIDTH, HEIGHT);
        // JScrollPane for output; adds textArea
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // Sets the layout of the overall/main frame to GridBagLayout
        this.setLayout(new GridBagLayout());
        // Creation of a JPanel for top (input) panel
        JPanel inputPanel = new JPanel();
        // Sets border for top panel (inputPanel)
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        // Sets layout of top panel (inputPanel) to GridBagLayout
        inputPanel.setLayout(new GridBagLayout());
        // Sets layout of treePanel to BorderLayout
        treePanel = new JPanel(new BorderLayout());
        // Adds treePanel and scrollPane to splitPane
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                treePanel, scrollPane);
        // Properties for splitPane
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(200);
        // Provides minimum sizes for the components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        treePanel.setMinimumSize(minimumSize);
        scrollPane.setMinimumSize(minimumSize);
        // Sets visibility of scrollPane to true
        scrollPane.setVisible(true);
        // Sets font of textArea - Monospaced, size 12
        textArea.setFont(new java.awt.Font("Monospaced", 0, 12));
        // Sets horizonal alignment of labels in input panel to the right
        inputLabel.setHorizontalAlignment(JLabel.RIGHT);
        searchTypeLabel.setHorizontalAlignment(JLabel.RIGHT);
        operationLabel.setHorizontalAlignment(JLabel.RIGHT);
        optionsLabel.setHorizontalAlignment(JLabel.RIGHT);
        
        // Top panel (inputPanel) constraints for GridBagLayout; 1/3 the size of frame
        GridBagConstraints cInputPanel = new GridBagConstraints();
        cInputPanel.fill = GridBagConstraints.HORIZONTAL;
        cInputPanel.weighty = 0.0;
        cInputPanel.weightx = 1.0;
        cInputPanel.gridx = 0;
        cInputPanel.gridy = 0;
        cInputPanel.gridheight = 1;
        
        // GridBagLayout constraints for inputLabel 
        GridBagConstraints cGridBag = new GridBagConstraints();
        cGridBag.weightx = 0.0;
        cGridBag.gridx = 0;
        cGridBag.gridy = 3;
        cGridBag.gridwidth = 1;
        inputPanel.add(inputLabel, cGridBag);

         // GridBagLayout constraints for searchTypeLabel 
        cGridBag.insets = new Insets(10, 0, 10, 0);
        cGridBag.ipady = 0;
        cGridBag.gridx = 0;
        cGridBag.gridy = 1;
        inputPanel.add(searchTypeLabel, cGridBag);

        // GridBagLayout constraints for operationLabel 
        cGridBag.gridx = 0;
        cGridBag.gridwidth = 1;
        cGridBag.gridy = 0;
        inputPanel.add(operationLabel, cGridBag);

        // GridBagLayout constraints for optionsLabel 
        cGridBag.gridwidth = 1;
        cGridBag.gridy = 2;
        inputPanel.add(optionsLabel, cGridBag);

        // GridBagLayout constraints for inputTextField 
        cGridBag.fill = GridBagConstraints.HORIZONTAL;
        cGridBag.ipadx = 0;
        cGridBag.weightx = 1.0;
        cGridBag.gridx = 1;
        cGridBag.gridwidth = 2; // changed to 1
        cGridBag.gridy = 3;
        inputPanel.add(inputTextField, cGridBag);

        // GridBagLayout constraints for searchRadioButton 
        cGridBag.ipadx = 0;
        cGridBag.weightx = 1.0;
        cGridBag.gridx = 1;
        cGridBag.gridwidth = 1;
        cGridBag.gridy = 0;
        inputPanel.add(searchRadioButton, cGridBag);

        // GridBagLayout constraints for sortRadioButton 
        cGridBag.gridx = 2;
        inputPanel.add(sortRadioButton, cGridBag);

        // GridBagLayout constraints for typeComboBox    
        cGridBag.ipadx = 0;
        cGridBag.weightx = 1.0;
        cGridBag.gridx = 1;
        cGridBag.gridwidth = 2; // changed to 1
        cGridBag.gridy = 1;
        inputPanel.add(typeComboBox, cGridBag);

        // GridBagLayout constraints for optionsComboBox  
        cGridBag.ipadx = 0;
        cGridBag.weightx = 1.0;
        cGridBag.gridx = 1;
        cGridBag.gridwidth = 2; // changed to 1
        cGridBag.gridy = 2;
        inputPanel.add(optionsComboBox, cGridBag);

        // GridBagLayout constraints for searchButton 
        cGridBag.ipadx = 0;
        cGridBag.weighty = 1.0;
        cGridBag.weightx = 2.0;
        cGridBag.gridy = 4;
        cGridBag.fill = GridBagConstraints.NONE;
        cGridBag.ipady = 0;
        cGridBag.gridx = 1;
        cGridBag.gridwidth = 1;
        inputPanel.add(searchButton, cGridBag);

        // GridBagLayout constraints for progressButton  
        cGridBag.ipady = 0;
        cGridBag.gridx = 2;
        cGridBag.gridwidth = 1;
        inputPanel.add(progressButton, cGridBag);

        // Bottom panel (outputPanel) constraints for GridBagLayout of main frame; 2/3 the size of frame
        GridBagConstraints cOutputPanel = new GridBagConstraints();
        cOutputPanel.fill = GridBagConstraints.BOTH;
        cOutputPanel.weighty = 1.0;
        cOutputPanel.weightx = 1.0;
        cOutputPanel.gridx = 0;
        cOutputPanel.gridy = 1;
        cOutputPanel.gridheight = 2;
        cOutputPanel.anchor = GridBagConstraints.PAGE_END;
        
        // Adds top panel (inputPanel) to the frame
        this.add(inputPanel, cInputPanel);
        // Adds tabbed pane to the frame
        this.add(tabbedPane, cOutputPanel);
        // Adds a tab to the tabbed pane (to display JTree and text from search/sort operations)
        tabbedPane.addTab("Data Structure", null, splitPane,
                "Data Structure");
        // Adds a tab to the tabbed pane (to display job progress)
        tabbedPane.addTab("Job Progress", null, jobPanel,
                "Job Progress");
        // Adds ChangeListener to tabbed pane
        tabbedPane.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            // Redraws JTree
            drawTree();
        }
    });
        // Adds Search and Sort radio buttons to the radio button group; binds the radio buttons together
        radioButtonGroup.add(searchRadioButton);
        radioButtonGroup.add(sortRadioButton);
        // Search radio button set to selected (default setting)
        searchRadioButton.setSelected(true);
        // Sets textArea to be uneditable
        textArea.setEditable(false);
        // Sets inputTextField to be editable
        inputTextField.setEditable(true);
        // Sets horizontal alignment of the radio buttons; centers them
        searchRadioButton.setHorizontalAlignment(SwingConstants.CENTER);
        sortRadioButton.setHorizontalAlignment(SwingConstants.CENTER);

        // Creation of an Action Listener for the 'Search' button
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        }); // end Action Listener for 'Search' button

        // Creation of an Action Listener for typeComboBox
        typeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeComboBoxActionPerformed(evt);
            }
        }); // end Action Listener for typeComboBox

        // Creation of an Action Listener for the 'Sort' radio button
        sortRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortRadioButtonActionPerformed(evt);
            }
        }); // end Action Listener for the 'Sort' radio button

        // Creation of an Action Listener for the 'Search' radio button
        searchRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchRadioButtonActionPerformed(evt);
            }
        }); // end Action Listener for the 'Search' radio button

        // Creation of an Action Listener for the 'Progress' button
        progressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                progressButtonActionPerformed(evt);
            }
        }); // end Action Listener for 'Progress' button

    } // end SeaPortProgram4 constructor

    // Method to set the frame and other features of the GUI
    private void setFrame(int width, int height) {
        // Sets the size of the GUI frame - width & height
        setSize(width, height);
        // Sets the GUI to the center of the screen
        setLocationRelativeTo(null);
        // Instructs the GUI to exit upon closing window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } // end setFrame method

    // Event Handler for the 'Sort' radio button
    private void sortRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (sortRadioButton.isSelected()) {
            inputTextField.setEditable(false);
            inputTextField.setText("");
            searchButton.setText("Sort");
            typeComboBox.removeAllItems();
            for (String typeOption : sortTypes) {
                typeComboBox.addItem(typeOption);
            }
        }
        updateOptionList();
    } // end Event Handler for 'Sort' radio button

    // Event Handler for the 'Search' radio button
    private void searchRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (searchRadioButton.isSelected()) {
            inputTextField.setEditable(true);
            searchButton.setText("Search");
            typeComboBox.removeAllItems();
            for (String typeOption : searchTypes) {
                typeComboBox.addItem(typeOption);
            }
        }
        updateOptionList();
    } // end Event Handler for 'Search' radio button

    // Event Handler for 'Type' combo box
    private void typeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        updateOptionList();
    } // end Event handler for 'Type' combo box

    // Method to update options in combo boxes for user selection based on radio button (Search vs. Sort) and Type selected
    private void updateOptionList() {
        // Removes all options from optionsComboBox
        optionsComboBox.removeAllItems();
        // Switch statement to add menu options to optionsComboBox
        switch (typeComboBox.getSelectedIndex()) {
            // For type:  Person
            case 0:
                // If Search is selected
                if (searchRadioButton.isSelected()) {
                    for (String searchPersonOption : searchPersonOptions) {
                        optionsComboBox.addItem(searchPersonOption);
                    }
                    // Else Sort is selected
                } else {
                    for (String sortPersonOption : sortPersonOptions) {
                        optionsComboBox.addItem(sortPersonOption);
                    }
                }
                break;
            // For type:  Job
            case 1:
                // If Search is selected
                if (searchRadioButton.isSelected()) {
                    for (String searchJobOption : searchJobOptions) {
                        optionsComboBox.addItem(searchJobOption);
                    }
                    // If Sort is selected
                } else {
                    for (String sortJobOption : sortJobOptions) {
                        optionsComboBox.addItem(sortJobOption);
                    }
                }
                break;
            // For type:  Ship
            case 2:
                // If Search is selected
                if (searchRadioButton.isSelected()) {
                    for (String searchPortOption : searchPortOptions) {
                        optionsComboBox.addItem(searchPortOption);
                    }
                    // If Sort is selected
                } else {
                    for (String sortPortOption : sortPortOptions) {
                        optionsComboBox.addItem(sortPortOption);
                    }
                }
                break;
            // For type:  Dock
            case 3:
                // If Search is selected
                if (searchRadioButton.isSelected()) {
                    for (String searchDockOption : searchDockOptions) {
                        optionsComboBox.addItem(searchDockOption);
                    }
                    // If Sort is selected
                } else {
                    for (String sortDockOption : sortDockOptions) {
                        optionsComboBox.addItem(sortDockOption);
                    }
                }
                break;
            case 4:
                // If Search is selected
                if (searchRadioButton.isSelected()) {
                    for (String searchShipOption : searchShipOptions) {
                        optionsComboBox.addItem(searchShipOption);
                    }
                    // If Sort is selected
                } else {
                    for (String sortShipOption : sortShipOptions) {
                        optionsComboBox.addItem(sortShipOption);
                    }
                }
                break;
            case 5:
                for (String sortShipOption : sortShipOptions) {
                    optionsComboBox.addItem(sortShipOption);
                }
                break;
            default:
                break;

        }
    } // end typeComboBox event handler

    // Event Handler for the 'Search' button
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Sets selected component of tabbedPane to splitPane
        tabbedPane.setSelectedComponent(splitPane);
        // Creation of a String that will be used to print information to user
        String textStr = "";
        // Try & Catch - NumberFormatException
        try {
            // Switch statement to handle various menu selection options
            switch (typeComboBox.getSelectedIndex()) {
                // For type:  Person
                case 0:
                    // If Name option is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortPeopleByNamePorts() method
                            world.sortPeopleByNamePorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of people found; findPersonByName method called here
                            ArrayList<Person> listPersons = world.findPersonByName(inputTextField.getText());
                            // Adding information to textStr
                            for (Person mPerson : listPersons) {
                                textStr += mPerson.infoString() + "\n\n";
                            }
                            // If listPersons ArrayList is empty (user input not found or blank)
                            if (listPersons.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                        // Else Skill option is selected
                    } else {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            world.sortPeopleBySkillPorts();
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of people found; findPersonBySkill method called here
                            ArrayList<Person> listPersons = world.findPersonBySkill(inputTextField.getText());
                            // Adding information to textStr
                            for (Person mPerson : listPersons) {
                                textStr += mPerson.infoString() + "\n\n";
                            }
                            // If listPersons ArrayList is empty (user input not found or blank)
                            if (listPersons.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                    }
                    break;
                // For type:  Job
                case 1:
                    // If Name option is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortJobsByNameShips() method
                            world.sortJobsByNameShips();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of jobs found; findJobByName method called here
                            ArrayList<Job> listJobs = world.findJobByName(inputTextField.getText());
                            // Adding information to textStr
                            for (Job mJob : listJobs) {
                                textStr += mJob.infoString() + "\n\n";
                            }
                            // If listJobs ArrayList is empty (user input not found or blank)
                            if (listJobs.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                        // Else Duration option is selected
                    } else {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortJobsByDurationShips() method
                            world.sortJobsByDurationShips();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of jobs found; findJobByRequirement method called here
                            ArrayList<Job> listJobs = world.findJobByRequirement(inputTextField.getText());
                            // Adding information to textStr
                            for (Job mJob : listJobs) {
                                textStr += mJob.infoString() + "\n\n";
                            }
                            // If listJobs ArrayList is empty (user input not found or blank)
                            if (listJobs.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                            if (optionsComboBox.getSelectedIndex() == 0) {
                                // Sorts jobs
                                Collections.sort(listJobs);
                            }
                        }
                    }
                    break;
                // For type:  Port                    
                case 2:
                    // If Name option is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortPortsByName() method
                            world.sortPortsByName();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of ships found; findShipByName method called here
                            ArrayList<SeaPort> listPorts = world.findPortByName(inputTextField.getText());
                            // Adding information to textStr
                            for (SeaPort mPort : listPorts) {
                                textStr += mPort.infoString() + "\n\n";
                            }
                            // If listShips ArrayList is empty (user input not found or blank)
                            if (listPorts.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                        // Else if Ship Name is selected
                    } else if (optionsComboBox.getSelectedIndex() == 1) {
                        // ArrayList to hold list of ports found; findPortByShip method called here
                        ArrayList<SeaPort> listPorts = world.findPortByShip(inputTextField.getText());
                        // Adding information to textStr
                        for (SeaPort mPort : listPorts) {
                            textStr += mPort.infoString() + "\n\n";
                        }
                        // If listPorts ArrayList is empty (user input not found or blank)
                        if (listPorts.isEmpty()) {
                            // Error message
                            JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                        }
                        // Else if Person Name is selected
                    } else {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortPeopleBySkillPorts() method
                            world.sortPeopleBySkillPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of ports found; findPortByPerson method called here
                            ArrayList<SeaPort> listPorts = world.findPortByPerson(inputTextField.getText());
                            // Adding information to textStr
                            for (SeaPort mPort : listPorts) {
                                textStr += mPort.infoString() + "\n\n";
                            }
                            // If listPorts ArrayList is empty (user input not found or blank)
                            if (listPorts.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                    }
                    break;
                // For type:  Dock
                case 3:
                    // If Name option is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortDocksByNamePorts() method
                            world.sortDocksByNamePorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of docks found; findDockByName method called here
                            ArrayList<Dock> listDocks = world.findDockByName(inputTextField.getText());
                            // Adding information to textStr
                            for (Dock mDock : listDocks) {
                                textStr += mDock.infoString() + "\n\n";
                            }
                            // If listDocks ArrayList is empty (user input not found or blank)
                            if (listDocks.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                    }
                    break;
                // For type:  Ship
                case 4:
                    // If Name option is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortAllShipsByNamePorts() method
                            world.sortAllShipsByNamePorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                            // Else Search is selected
                        } else {
                            // ArrayList to hold list of docks found; findDockByName method called here
                            ArrayList<Ship> listShips = world.findShipByName(inputTextField.getText());
                            // Adding information to textStr
                            for (Ship mShip : listShips) {
                                textStr += mShip.infoString() + "\n\n";
                            }
                            // If listDocks ArrayList is empty (user input not found or blank)
                            if (listShips.isEmpty()) {
                                // Error message
                                JOptionPane.showMessageDialog(frame, "Sorry, unfortunately your search criteria could not be found!");
                            }
                        }
                        // Else if Draft is selected
                    } else if (optionsComboBox.getSelectedIndex() == 1) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortAllShipsByDraftPorts() method
                            world.sortAllShipsByDraftPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Length is selected
                    } else if (optionsComboBox.getSelectedIndex() == 2) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortAllShipsByLengthPorts() method
                            world.sortAllShipsByLengthPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Weight is selected
                    } else if (optionsComboBox.getSelectedIndex() == 3) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortAllShipsByWeightPorts() method
                            world.sortAllShipsByWeightPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        //  Else Width is selected
                    } else if (optionsComboBox.getSelectedIndex() == 4) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortAllShipsByWidthPorts() method
                            world.sortAllShipsByWidthPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                    }
                    break;
                // For type:  Ships in Que  
                case 5:
                    // If Name is selected
                    if (optionsComboBox.getSelectedIndex() == 0) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortQueByNamePorts() method
                            world.sortQueByNamePorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Draft is selected
                    } else if (optionsComboBox.getSelectedIndex() == 1) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortQueByDraftPorts() method
                            world.sortQueByDraftPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Length is selected
                    } else if (optionsComboBox.getSelectedIndex() == 2) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortQueByLengthPorts() method
                            world.sortQueByLengthPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Weight is selected
                    } else if (optionsComboBox.getSelectedIndex() == 3) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortQueByWeightPorts() method
                            world.sortQueByWeightPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                        // Else if Width is selected
                    } else if (optionsComboBox.getSelectedIndex() == 4) {
                        // If Sort is selected
                        if (sortRadioButton.isSelected()) {
                            // Calls sortQueByWidthPorts() method
                            world.sortQueByWidthPorts();
                            // Sets text area
                            textArea.setText(world.infoString());
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (final NumberFormatException ex) {
            // Error message - NumberFormatException
            JOptionPane.showMessageDialog(frame, "Please enter a numerical value!");
        }
        // If Search is selected
        if (searchRadioButton.isSelected()) {
            // Sets text area
            textArea.setText(textStr);
        } else {
            // Creates JTree if 'Sort' button is selected
            drawTree();
        }
    } // end searchButtonActionPerformed event handler

    // Event Handler for 'Progress' button
    private void progressButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // Calls startJobs() method to begin threading process
        world.startJobs(jobPanel);
        // Sets visible tab of tabbedPane to jobPanel ('Job Progress')
        tabbedPane.setSelectedComponent(jobPanel);
        // Disables progressButton (so that it can not be activated twice)
        progressButton.setEnabled(false);
    } //end progressButton event handler

    // Method that uses objects to create JTree to display text file data structure
    private void drawTree() {
        // Creation of new JTree
        tree = new JTree(createTreeNodes("World"));
        // Empties treePanel
        treePanel.removeAll();
        // Creation of JScrollPane; adds JTree (tree) to this
        JScrollPane viewTree = new JScrollPane(tree);
        // Adds viewTree scroll pane to treePanel
        treePanel.add(viewTree);
        // Sets visibility of treePanel to true
        treePanel.setVisible(true);
        // Creates and adds TreeSelectionListener to tree
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                // Node
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                // If nothing is selected
                if (node == null) {
                    return;
                }
                // Retrieves the node that was selected
                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof Thing) {
                    // Sets text of textArea with information of object pertaining to the node that was selected
                    textArea.setText(((Thing) nodeInfo).infoString());
                } else {
                    textArea.setText("");
                }
            }
        });
        // Method used to create JTree
        validate();
    } // end drawTree() method

    // Method to create the nodes that are used to populate the JTree
    private DefaultMutableTreeNode createTreeNodes(String title) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(title);
        // Parent node - each Sea Port
        DefaultMutableTreeNode parentNode;
        for (SeaPort port : world.getPorts()) {
            parentNode = new DefaultMutableTreeNode(port);
            // Adds parent node
            top.add(parentNode);
            // Adds branches to JTree; addNewTreeBranch() method called here
            parentNode.add(addNewTreeBranch(port.getDocks(), "Docks"));
            parentNode.add(addNewTreeBranch(port.getShips(), "Ships"));
            parentNode.add(addNewTreeBranch(port.getQue(), "Ships in Que"));
            parentNode.add(addNewTreeBranch(port.getPersons(), "Persons"));
        }
        return top;
    } // end createTreeNodes() method

    // Method to add a new branch to the JTree
    private <T extends Thing> DefaultMutableTreeNode addNewTreeBranch(ArrayList<T> thingList, String name) {
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(name);
        if (thingList.isEmpty()) {
            // If there are no objects
            parentNode.add(new DefaultMutableTreeNode("*EMPTY*"));
        } else {
            thingList.forEach((t) -> {
                String txt = t.getName();
                if (t instanceof Dock) {
                    // Creation of Dock node
                    DefaultMutableTreeNode dockNode = new DefaultMutableTreeNode(t);
                    // Adds Dock node
                    parentNode.add(dockNode);
                    if (((Dock) t).getShip() != null) {
                        // Creation of Ship node
                        DefaultMutableTreeNode shipNode = new DefaultMutableTreeNode(((Dock) t).getShip());
                        // Adds Ship node
                        dockNode.add(shipNode);
                    } else {
                        // If there are no objects (e.g., Ships at a Dock)
                        dockNode.add(new DefaultMutableTreeNode("*EMPTY*"));
                    }
                } else {
                    parentNode.add(new DefaultMutableTreeNode(t));
                }
            });
        }
        return parentNode;
    } // end addNewTreeBranch() method

    //Main method
    public static void main(String[] args) {
        // Runs program
        SeaPortProgram4 seaPortProgram4 = new SeaPortProgram4();
        seaPortProgram4.setVisible(true);
        // Welcome message
        JOptionPane.showMessageDialog(null, "Welcome to the SeaPort program!\nAuthor: Rebecca Davis\nMarch 2018");
        // Use of JFileChooser to allow user to select text file; "." means the search will start in the current directory
        JFileChooser jfc = new JFileChooser(".");
        int value = jfc.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            // Confirmation message of chosen file
            JOptionPane.showMessageDialog(null, "You have selected the following file: " + jfc.getSelectedFile().getName());
            // Try & Catch to open the file and read it (for FileNotFoundException)
            try {
                // Creation of Scanner object; getSelectedFile() method used 
                Scanner scanner = new Scanner(jfc.getSelectedFile());
                // fillWorld method declared here
                world.fillWorld(scanner);
                seaPortProgram4.drawTree();
                // FileNotFoundException
            } catch (FileNotFoundException e) {
                // Error message
                JOptionPane.showMessageDialog(null, "Error! File not found.");
                return;
            }
        }
    } // end main method

} // end SeaPortProgram4 class

