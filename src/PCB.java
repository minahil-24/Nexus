import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;


public class PCB {
    // List to hold the processes
    // Database connection details
    public static List<PCB.Process> processQueue = new ArrayList<>();
    private static final String DEFAULT_PROCESSOR = "CPU-1";
    // Enumeration for process states
    private enum State {
        READY, RUNNING, BLOCKED, TERMINATED
    }
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/operatingsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Static counter to track the process ID (initialized once)
    private static int processCounter = 1;

    // Method to generate a unique process ID
    private static int generateUniqueProcessID() {
        int processID = processCounter; // Start with the current counter value
        boolean isUnique = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT COUNT(*) FROM pcb WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                while (!isUnique) {
                    stmt.setInt(1, processID);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            // ID is unique
                            isUnique = true;
                        } else {
                            // ID already exists, increment and check again
                            processID++;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        processCounter = processID + 1; // Update the counter for the next process
        return processID;
    }

    // Method to destroy (delete) the last process from the database
    public static void destroyLastProcess() {
        int lastProcessID = 0;

        // Retrieve the ID of the last inserted process
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb ORDER BY ID DESC LIMIT 1";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    lastProcessID = rs.getInt("ID");
                }
            }

            if (lastProcessID > 0) {
                // Delete the last process
                String deleteSql = "DELETE FROM pcb WHERE ID = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, lastProcessID);
                    deleteStmt.executeUpdate();
                    processCounter--;
                    showCustomDialog("P " + lastProcessID + " has been deleted from the database.");
                }
            } else {
                showCustomDialog("No process found to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error deleting process from database.");
        }
    }

    // Method to show a custom dialog for process attributes
    private static ProcessAttributes showProcessInputDialog(int processID) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Process P" + processID + " Details");
        dialog.setUndecorated(true);
        dialog.setSize(500, 400);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setOpacity(0.8f);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        dialog.setContentPane(contentPanel);
        contentPanel.setLayout(null);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, 500, 400, 30, 30));

        JLabel titleLabel = new JLabel("Enter Details for Process P" + processID, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 20, 400, 30);
        contentPanel.add(titleLabel);

        JLabel ownerLabel = new JLabel("Owner (User/System):");
        ownerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ownerLabel.setForeground(Color.WHITE);
        ownerLabel.setBounds(50, 70, 150, 30);
        contentPanel.add(ownerLabel);

        JComboBox<String> ownerComboBox = new JComboBox<>(new String[]{"User", "System"});
        ownerComboBox.setBounds(200, 70, 200, 30);
        contentPanel.add(ownerComboBox);

        JLabel priorityLabel = new JLabel("Priority (1-10):");
        priorityLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priorityLabel.setForeground(Color.WHITE);
        priorityLabel.setBounds(50, 120, 150, 30);
        contentPanel.add(priorityLabel);

        JTextField priorityField = new JTextField("5");
        priorityField.setBounds(200, 120, 200, 30);
        contentPanel.add(priorityField);

        JLabel memoryLabel = new JLabel("AllocatedMemory Size:");
        memoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        memoryLabel.setForeground(Color.WHITE);
        memoryLabel.setBounds(50, 170, 150, 30);
        contentPanel.add(memoryLabel);

        JTextField memoryField = new JTextField("");
        memoryField.setBounds(200, 170, 200, 30);
        contentPanel.add(memoryField);

        JLabel burstLabel = new JLabel("Burst Time:");
        burstLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        burstLabel.setForeground(Color.WHITE);
        burstLabel.setBounds(50, 220, 150, 30);
        contentPanel.add(burstLabel);

        JTextField burstField = new JTextField();
        burstField.setBounds(200, 220, 200, 30);
        contentPanel.add(burstField);

        JLabel parentLabel = new JLabel("Parent Process ID:");
        parentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        parentLabel.setForeground(Color.WHITE);
        parentLabel.setBounds(50, 270, 150, 30);
        contentPanel.add(parentLabel);

        JTextField parentField = new JTextField();
        parentField.setBounds(200, 270, 200, 30);
        contentPanel.add(parentField);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBounds(150, 340, 90, 30);
        okButton.setOpaque(false); // Make background transparent
        okButton.setContentAreaFilled(false); // Remove filled background
        okButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Add white border
        okButton.setForeground(Color.WHITE); // Set text color to white
        okButton.setFocusPainted(false); // Remove focus outline
        contentPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBounds(270, 340, 90, 30);
        cancelButton.setOpaque(false); // Make background transparent
        cancelButton.setContentAreaFilled(false); // Remove filled background
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Add white border
        cancelButton.setForeground(Color.WHITE); // Set text color to white
        cancelButton.setFocusPainted(false); // Remove focus outline
        contentPanel.add(cancelButton);

        final ProcessAttributes[] attributes = {null};

        // Add Processor Label and TextField
        JLabel processorLabel = new JLabel("Processor:");
        processorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        processorLabel.setForeground(Color.WHITE);
        processorLabel.setBounds(50, 310, 150, 30);
        contentPanel.add(processorLabel);

        JTextField processorField = new JTextField("CPU-1"); // Default value
        processorField.setBounds(200, 310, 200, 30);
        contentPanel.add(processorField);

// OK Button Action Listener
        okButton.addActionListener(e -> {
            try {
                String owner = ownerComboBox.getSelectedItem().toString();
                int priority = Integer.parseInt(priorityField.getText());
                int memory = Integer.parseInt(memoryField.getText());
                int burst = Integer.parseInt(burstField.getText());
                Integer parent = parentField.getText().isEmpty() ? null : Integer.parseInt(parentField.getText());

                if (priority < 1 || priority > 10) throw new NumberFormatException("Priority out of range");

                // Check processor input
                String processor = processorField.getText().trim().isEmpty() ? null : processorField.getText().trim();

                attributes[0] = new ProcessAttributes(owner, priority, memory, parent, burst, processor);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showCustomDialog("Invalid input. Please check your entries.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return attributes[0];
    }

    // Method to create a new process with user input
    public static void createProcess() {
        int processID = generateUniqueProcessID();

        ProcessAttributes attributes = showProcessInputDialog(processID);
        if (attributes == null) {
            showCustomDialog("Process creation canceled.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO pcb (ID, State, Owner, Priority, Parent, Memory, AllocatedMemory, CPURegisters, Processor, BurstTime, ArrivalTime) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, processID);
                stmt.setString(2, State.READY.name()); // Ensure the state is READY
                stmt.setString(3, attributes.owner);
                stmt.setInt(4, attributes.priority);
                stmt.setObject(5, attributes.parentProcess);
                stmt.setInt(6, 40);
                stmt.setObject(7, attributes.memory); // No memory allocated initially
                stmt.setString(8, new ArrayList<>(List.of(0, 0, 0, 0, 0, 0, 0, 0)).toString());
                stmt.setString(9, attributes.processor); // Store processor value or null if empty
                stmt.setInt(10, attributes.burst);

                // Add the current timestamp for ArrivalTime
                java.sql.Timestamp currentTime = new java.sql.Timestamp(System.currentTimeMillis());
                stmt.setTimestamp(11, currentTime);

                stmt.executeUpdate();
                showCustomDialog("P " + processID + " created and saved to database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error creating process in database.");
        }
    }

    public static void resetProcessState() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE pcb SET State = ? WHERE State != ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, State.READY.name()); // Set all processes to READY state
                stmt.setString(2, State.READY.name()); // Don't update already READY processes
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to execute processes based on FCFS scheduling
    public static void executeFCFS() {
        List<Process> processList = getAllProcesses();
        // Sort processes by ArrivalTime (First Come First Serve)
        processList.sort(Comparator.comparing(Process::getArrivalTime));
        PCB.showExecutionTable(processList, "FCFS Scheduling");
    }

    // Method to execute processes based on SJF scheduling
    public static void executeSJF() {
        List<Process> processList = getAllProcesses();
        // Sort processes by BurstTime (Shortest Job First)
        processList.sort(Comparator.comparingInt(Process::getBurstTime));
        showExecutionTable(processList, "SJF Scheduling");
    }

public static void executePreemptiveSJF() {
    // Fetch all processes from the database or source
    List<Process> processList = getAllProcesses();

    // If no processes are available, show a custom dialog and return
    if (processList.isEmpty()) {
        showCustomDialog("No processes available to execute.");
        return;
    }

    // Sort processes by their arrival time initially
    processList.sort(Comparator.comparing(Process::getArrivalTime));

    // Create a JFrame to display the scheduling table
    JFrame frame = new JFrame("Preemptive SJF Scheduling");
    frame.setSize(800, 400); // Set frame size
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close operation on window close
    frame.setLayout(new BorderLayout()); // Set layout to BorderLayout

    // Create a table model with column names: Process ID, Arrival Time, Burst Time, Remaining Time, State
    DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Process ID", "Arrival Time", "Burst Time", "Remaining Time", "State"}, 0);
    // Create a JTable with the table model
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table); // Add scroll pane to the table
    frame.add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the frame

    // Populate the table with the initial process data (Process ID, Arrival Time, Burst Time, Remaining Time, State)
    for (Process process : processList) {
        tableModel.addRow(new Object[] {
                process.getId(), // Process ID
                process.getArrivalTime(), // Arrival Time
                process.getBurstTime(), // Burst Time
                process.getBurstTime(), // Initial Remaining Time (same as Burst Time)
                "READY" // Initial state as READY
        });
    }

    // Make the frame visible
    frame.setVisible(true);

    // Start a new thread to simulate the process execution
    new Thread(() -> {
        int timeElapsed = 0; // Initialize the time elapsed to 0
        Process runningProcess = null; // Variable to keep track of the currently running process
        System.out.println("Starting simulation...");

        // Loop until all processes are executed
        while (!processList.isEmpty()) {
            Process currentProcess = null;
            long simulatedTimeMillis = System.currentTimeMillis() + (timeElapsed * 1000L);
            Date simulatedTime = new Date(simulatedTimeMillis);

            // Select a process based on arrival time first, then burst time
            if (runningProcess == null) {
                // The first time, pick the process with the earliest arrival time
                for (Process process : processList) {
                    if (!process.getArrivalTime().after(simulatedTime)) { // If process has arrived
                        if (currentProcess == null || process.getArrivalTime().before(currentProcess.getArrivalTime())) {
                            currentProcess = process;
                        }
                    }
                }
            } else {
                // After the first process, choose based on shortest remaining burst time
                for (Process process : processList) {
                    if (!process.getArrivalTime().after(simulatedTime) && process.getBurstTime() > 0) {
                        if (currentProcess == null || process.getBurstTime() < currentProcess.getBurstTime()) {
                            currentProcess = process;
                        }
                    }
                }
            }

            // If no process is ready to execute, increment time and wait for 1 second
            if (currentProcess == null) {
                timeElapsed++;
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue; // Skip the current loop iteration
            }

            // Check if the current process is different from the running process
            if (runningProcess != null && runningProcess != currentProcess) {
                // Preempt the running process if the current process has a shorter burst time
                if (currentProcess.getBurstTime() < runningProcess.getBurstTime()) {
                    // Update running process state to "PREEMPTED"
                    final Process finalRunningProcess = runningProcess;
                    SwingUtilities.invokeLater(() -> {
                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableModel.getValueAt(i, 0).equals(finalRunningProcess.getId()) &&
                                    tableModel.getValueAt(i, 4).equals("RUNNING")) {
                                tableModel.setValueAt("PREEMPTED", i, 4); // Change state to PREEMPTED
                                break;
                            }
                        }
                    });

                    // Add the preempted process back to the ready queue if its burst time is not finished
                    if (finalRunningProcess.getBurstTime() > 0) {

                        processList.add(finalRunningProcess);
                        System.out.println("process add");// Ensure preempted process gets added back to the list
                    }
                }
            }

            // Set the state of the new process to "RUNNING"
            runningProcess = currentProcess;
            final Process finalCurrentProcess = currentProcess;

            // Update the state of the current process to "RUNNING"
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(finalCurrentProcess.getId()) &&
                            tableModel.getValueAt(i, 4).equals("READY")) {
                        tableModel.setValueAt("RUNNING", i, 4); // Change state to RUNNING
                        break;
                    }
                }
            });
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(finalCurrentProcess.getId()) &&
                            tableModel.getValueAt(i, 4).equals("PREEMPTED")) {
                        tableModel.setValueAt("RUNNING", i, 4); // Change state to RUNNING
                        break;
                    }
                }
            });

            // Simulate 1 second of execution (execute for 1 second)
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Decrease the remaining burst time of the current process by 1
            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);

            // Update the remaining time in the table for the current process
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 0).equals(finalCurrentProcess.getId()) && tableModel.getValueAt(i, 4).equals("RUNNING")) {
                        tableModel.setValueAt(finalCurrentProcess.getBurstTime(), i, 3); // Update Remaining Time
                        break;
                    }
                }
            });

            // Check if the process has completed its burst time
            if (currentProcess.getBurstTime() == 0) {
                processList.remove(currentProcess); // Remove the process from the list once it's finished

                // Update the last row of the process to "TERMINATED"
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (tableModel.getValueAt(i, 0).equals(finalCurrentProcess.getId()) && tableModel.getValueAt(i, 4).equals("RUNNING")) {
                            tableModel.setValueAt("TERMINATED", i, 4); // Set the state of the last "RUNNING" row to "TERMINATED"
                            break;
                        }
                    }
                });
            }

            // Increment time elapsed by 1 second
            timeElapsed++;
        }
    }).start(); // Start the thread
}
    // Method to change the priority of a process
    public static void changeProcessPriority() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Change Process Priority");
        dialog.setSize(400, 300);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel selectProcessLabel = new JLabel("Select Process:");
        selectProcessLabel.setBounds(50, 30, 100, 30);
        dialog.add(selectProcessLabel);

        JComboBox<String> processDropdown = new JComboBox<>();
        processDropdown.setBounds(160, 30, 180, 30);
        dialog.add(processDropdown);

        JLabel newPriorityLabel = new JLabel("New Priority (0-10):");
        newPriorityLabel.setBounds(50, 80, 150, 30);
        dialog.add(newPriorityLabel);

        JTextField newPriorityField = new JTextField();
        newPriorityField.setBounds(160, 80, 180, 30);
        dialog.add(newPriorityField);

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 150, 80, 30);
        dialog.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(200, 150, 80, 30);
        dialog.add(cancelButton);

        // Load all processes from the database into the dropdown
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID, Priority FROM pcb";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    int priority = rs.getInt("Priority");
                    processDropdown.addItem("P" + id + " (Priority: " + priority + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error loading processes.");
        }

        okButton.addActionListener(e -> {
            String selectedProcess = (String) processDropdown.getSelectedItem();
            if (selectedProcess == null || newPriorityField.getText().isEmpty()) {
                showCustomDialog("Please select a process and enter a new priority.");
                return;
            }

            try {
                int newPriority = Integer.parseInt(newPriorityField.getText());
                if (newPriority < 0 || newPriority > 10) {
                    throw new NumberFormatException("Priority out of range.");
                }

                int processID = Integer.parseInt(selectedProcess.substring(1, selectedProcess.indexOf(" ")));

                // Update the priority in the database
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                    String updateSql = "UPDATE pcb SET Priority = ? WHERE ID = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                        stmt.setInt(1, newPriority);
                        stmt.setInt(2, processID);
                        stmt.executeUpdate();
                        showCustomDialog("Priority of P" + processID + " updated to " + newPriority + ".");
                        dialog.dispose();
                    }
                }
            } catch (NumberFormatException ex) {
                showCustomDialog("Invalid priority. Please enter a number between 0 and 10.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                showCustomDialog("Error updating priority.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // Method to resume a process (change state from SUSPENDED to READY)
    public static void wakeupProcess() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Resume Process");
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel selectProcessLabel = new JLabel("Select Process (BLOCKED):");
        selectProcessLabel.setBounds(50, 30, 150, 30);
        dialog.add(selectProcessLabel);

        JComboBox<String> processDropdown = new JComboBox<>();
        processDropdown.setBounds(160, 30, 180, 30);
        dialog.add(processDropdown);

        JButton resumeButton = new JButton("Wakeup");
        resumeButton.setBounds(150, 100, 100, 30);
        dialog.add(resumeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 150, 100, 30);
        dialog.add(cancelButton);

        // Load all processes with the SUSPENDED state from the database into the dropdown
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb WHERE State = 'BLOCKED'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    processDropdown.addItem("P" + id); // Only show process ID in the dropdown
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error loading BLOCKED processes.");
        }

        resumeButton.addActionListener(e -> {
            String selectedProcess = (String) processDropdown.getSelectedItem();
            if (selectedProcess == null) {
                showCustomDialog("Please select a process.");
                return;
            }

            int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID

            // Update the state to READY in the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String updateSql = "UPDATE pcb SET State = 'READY' WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, processID);
                    stmt.executeUpdate();
                    showCustomDialog("Process P" + processID + " is now WAKEUP and in READY.");
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showCustomDialog("Error WAKEUPKING the process.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // Method to resume a process (change state from SUSPENDED to READY)
    public static void resumeProcess() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Resume Process");
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel selectProcessLabel = new JLabel("Select Process (SUSPENDED):");
        selectProcessLabel.setBounds(50, 30, 150, 30);
        dialog.add(selectProcessLabel);

        JComboBox<String> processDropdown = new JComboBox<>();
        processDropdown.setBounds(160, 30, 180, 30);
        dialog.add(processDropdown);

        JButton resumeButton = new JButton("Resume");
        resumeButton.setBounds(150, 100, 100, 30);
        dialog.add(resumeButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 150, 100, 30);
        dialog.add(cancelButton);

        // Load all processes with the SUSPENDED state from the database into the dropdown
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb WHERE State = 'SUSPENDED'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    processDropdown.addItem("P" + id); // Only show process ID in the dropdown
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error loading suspended processes.");
        }

        resumeButton.addActionListener(e -> {
            String selectedProcess = (String) processDropdown.getSelectedItem();
            if (selectedProcess == null) {
                showCustomDialog("Please select a process.");
                return;
            }

            int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID

            // Check if the process has a processor assigned
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String checkProcessorSql = "SELECT Processor FROM pcb WHERE ID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkProcessorSql)) {
                    checkStmt.setInt(1, processID);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            String processor = rs.getString("Processor");

                            if (processor == null || processor.trim().isEmpty()) {
                                showCustomDialog("Resource unavailable");
                                return;
                            }
                        }
                    }
                }

                // If the process has a processor, resume it
                String updateSql = "UPDATE pcb SET State = 'READY' WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, processID);
                    stmt.executeUpdate();
                    showCustomDialog("Process P" + processID + " is now RESUMED and in READY.");
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showCustomDialog("Error resuming the process.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }


    public static void suspendProcess() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Suspend Process");
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel selectProcessLabel = new JLabel("Select Process (READY):");
        selectProcessLabel.setBounds(50, 30, 150, 30);
        dialog.add(selectProcessLabel);

        JComboBox<String> processDropdown = new JComboBox<>();
        processDropdown.setBounds(160, 30, 180, 30);
        dialog.add(processDropdown);
////
        JButton suspendButton = new JButton("Suspend");
        suspendButton.setBounds(150, 100, 100, 30);
        dialog.add(suspendButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 150, 100, 30);
        dialog.add(cancelButton);

        // Load all processes with the READY state from the database into the dropdown
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb WHERE State = 'READY'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    processDropdown.addItem("P" + id); // Only show process ID in the dropdown
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error loading processes.");
        }

        suspendButton.addActionListener(e -> {
            String selectedProcess = (String) processDropdown.getSelectedItem();
            if (selectedProcess == null) {
                showCustomDialog("Please select a process.");
                return;
            }

            int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID

            // Update the state to SUSPENDED in the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String updateSql = "UPDATE pcb SET State = 'Ready' WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, processID);
                    stmt.executeUpdate();
                    showCustomDialog("Process P" + processID + "  it is in Ready State");
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showCustomDialog("Error suspending the process.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // Method to suspend a process (change state to SUSPENDED)
    public static void BlockProcess() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Block Process");
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel selectProcessLabel = new JLabel("Select Process (READY):");
        selectProcessLabel.setBounds(50, 30, 150, 30);
        dialog.add(selectProcessLabel);

        JComboBox<String> processDropdown = new JComboBox<>();
        processDropdown.setBounds(160, 30, 180, 30);
        dialog.add(processDropdown);

        JButton suspendButton = new JButton("Block");
        suspendButton.setBounds(150, 100, 100, 30);
        dialog.add(suspendButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 150, 100, 30);
        dialog.add(cancelButton);

        // Load all processes with the READY state from the database into the dropdown
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb WHERE State = 'READY'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    processDropdown.addItem("P" + id); // Only show process ID in the dropdown
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showCustomDialog("Error loading processes.");
        }

        suspendButton.addActionListener(e -> {
            String selectedProcess = (String) processDropdown.getSelectedItem();
            if (selectedProcess == null) {
                showCustomDialog("Please select a process.");
                return;
            }

            int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID

            // Update the state to SUSPENDED in the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String updateSql = "UPDATE pcb SET State = 'Ready' WHERE ID = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setInt(1, processID);
                    stmt.executeUpdate();
                    showCustomDialog("Process P" + processID + " is in ready State");
                    dialog.dispose();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showCustomDialog("Error suspending the process.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }


    // Helper class to represent the execution state of a process
    private static class ProcessExecution {
        private int processID;
        private int burstTime;
        private int remainingBurstTime;

        ProcessExecution(int processID, int burstTime) {
            this.processID = processID;
            this.burstTime = burstTime;
            this.remainingBurstTime = burstTime;
        }

        public int getProcessID() {
            return processID;
        }

        public int getBurstTime() {
            return burstTime;
        }

        public int getRemainingBurstTime() {
            return remainingBurstTime;
        }

        public void decreaseRemainingBurstTime() {
            this.remainingBurstTime--;
        }
    }

    private static void showExecutionTable(List<Process> processList, String schedulingType) {
        // Create a JFrame for the table
        JFrame frame = new JFrame(schedulingType);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table model and JTable setup with "Remaining Time" column
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Process ID", "State", "Burst Time", "Remaining Time", "Arrival Time"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add all processes to the table with initial state READY and remaining time equal to burst time
        for (Process process : processList) {
            tableModel.addRow(new Object[]{process.getId(), "READY", process.getBurstTime(),  process.getBurstTime(),process.getArrivalTime()});
        }

        frame.setVisible(true);

        // Simulate process execution
        new Thread(() -> {
            for (int i = 0; i < processList.size(); i++) {
                Process currentProcess = processList.get(i);
                int rowIndex = i; // Store the index separately to be used in the lambda

                // Update the state of the current process to RUNNING
                SwingUtilities.invokeLater(() -> tableModel.setValueAt("RUNNING", rowIndex, 1));

                int remainingTime = currentProcess.getBurstTime();

                // Decrement the remaining time every second until it reaches 0
                while (remainingTime > 0) {
                    int finalRemainingTime = remainingTime; // Capture value for lambda expression

                    // Update the remaining time in the table
                    SwingUtilities.invokeLater(() -> tableModel.setValueAt(finalRemainingTime, rowIndex, 3));

                    // Simulate 1 second execution
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Decrement remaining time
                    remainingTime--;
                }

                // Set process to TERMINATED when remaining time reaches 0
                SwingUtilities.invokeLater(() -> {
                    tableModel.setValueAt(0, rowIndex, 3); // Ensure remaining time is shown as 0
                    tableModel.setValueAt("TERMINATED", rowIndex, 1); // Change state to TERMINATED
                });
            }
        }).start();
    }
    // Method to update the state of a process in the database
    private static void updateProcessState(int processID, State state) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE pcb SET State = ? WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, state.name());
                stmt.setInt(2, processID);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all processes from the database
    private static List<Process> getAllProcesses() {
        List<Process> processList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM pcb WHERE State = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, State.READY.name());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("ID");
                        int burstTime = rs.getInt("BurstTime");
                        Timestamp arrivalTime = rs.getTimestamp("ArrivalTime");
                        processList.add(new Process(id, burstTime, arrivalTime));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return processList;
    }

    private static void showCustomDialog(String message) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Process Information");
        dialog.setUndecorated(true);
        dialog.setSize(500, 200);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setOpacity(0.8f);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        dialog.setContentPane(contentPanel);
        contentPanel.setLayout(null);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, 500, 200, 30, 30));

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(Color.WHITE);

        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(470, 10, 20, 20);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        contentPanel.add(label);
        contentPanel.add(closeButton);

        label.setBounds(100, 70, 300, 60);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Helper class to store process attributes
    private static class ProcessAttributes {
        String owner;
        int priority;
        int memory;
        Integer parentProcess;
        int burst;
        String processor; // Add processor field
        ProcessAttributes(String owner, int priority, int memory, Integer parent, int burst, String processor)
        {
            this.owner = owner;
            this.priority = priority;
            this.memory = memory;
            this.parentProcess = parentProcess;
            this.burst = burst;
            this.processor = processor; // Assign processor field
        }
    }

    // Helper class to represent a process
    public static class Process {
        private int id;
        private int burstTime;
        private Timestamp arrivalTime;

        Process(int id, int burstTime, Timestamp arrivalTime) {
            this.id = id;
            this.burstTime = burstTime;
            this.arrivalTime = arrivalTime;
        }

        public int getId() {
            return id;
        }

        public int getBurstTime() {
            return burstTime;
        }

        // This is the setter method you need
        public void setBurstTime(int burstTime) {
            this.burstTime = burstTime;
        }

        public Timestamp getArrivalTime() {
            return arrivalTime;
        }
    }
    public static void main(String[] args) {
resetProcessState();
executePreemptiveSJF();
    }
}
