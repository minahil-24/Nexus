//////import javax.swing.*;
//////import java.sql.*;//import javax.swing.*;
////import java.sql.*;
// Method to suspend a process (change state to SUSPENDED)
//public static void suspendProcess() {
//    JDialog dialog = new JDialog();
//    dialog.setTitle("Suspend Process");
//    dialog.setSize(400, 250);
//    dialog.setResizable(false);
//    dialog.setModal(true);
//    dialog.setLocationRelativeTo(null);
//    dialog.setLayout(null);
//
//    JLabel selectProcessLabel = new JLabel("Select Process (READY):");
//    selectProcessLabel.setBounds(50, 30, 150, 30);
//    dialog.add(selectProcessLabel);
//
//    JComboBox<String> processDropdown = new JComboBox<>();
//    processDropdown.setBounds(160, 30, 180, 30);
//    dialog.add(processDropdown);
//////
//    JButton suspendButton = new JButton("Suspend");
//    suspendButton.setBounds(150, 100, 100, 30);
//    dialog.add(suspendButton);
//
//    JButton cancelButton = new JButton("Cancel");
//    cancelButton.setBounds(150, 150, 100, 30);
//    dialog.add(cancelButton);
//
//    // Load all processes with the READY state from the database into the dropdown
//    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
//        String sql = "SELECT ID FROM pcb WHERE State = 'READY'";
//        try (PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                int id = rs.getInt("ID");
//                processDropdown.addItem("P" + id); // Only show process ID in the dropdown
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        showCustomDialog("Error loading processes.");
//    }
//
//    suspendButton.addActionListener(e -> {
//        String selectedProcess = (String) processDropdown.getSelectedItem();
//        if (selectedProcess == null) {
//            showCustomDialog("Please select a process.");
//            return;
//        }
//
//        int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID
//
//        // Update the state to SUSPENDED in the database
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            String updateSql = "UPDATE pcb SET State = 'SUSPENDED' WHERE ID = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
//                stmt.setInt(1, processID);
//                stmt.executeUpdate();
//                showCustomDialog("Process P" + processID + " is now SUSPENDED.");
//                dialog.dispose();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            showCustomDialog("Error suspending the process.");
//        }
//    });
//
//    cancelButton.addActionListener(e -> dialog.dispose());
//
//    dialog.setVisible(true);
//}
//// Method to suspend a process (change state to SUSPENDED)
//public static void BlockProcess() {
//    JDialog dialog = new JDialog();
//    dialog.setTitle("Block Process");
//    dialog.setSize(400, 250);
//    dialog.setResizable(false);
//    dialog.setModal(true);
//    dialog.setLocationRelativeTo(null);
//    dialog.setLayout(null);
//
//    JLabel selectProcessLabel = new JLabel("Select Process (READY):");
//    selectProcessLabel.setBounds(50, 30, 150, 30);
//    dialog.add(selectProcessLabel);
//
//    JComboBox<String> processDropdown = new JComboBox<>();
//    processDropdown.setBounds(160, 30, 180, 30);
//    dialog.add(processDropdown);
//
//    JButton suspendButton = new JButton("Block");
//    suspendButton.setBounds(150, 100, 100, 30);
//    dialog.add(suspendButton);
//
//    JButton cancelButton = new JButton("Cancel");
//    cancelButton.setBounds(150, 150, 100, 30);
//    dialog.add(cancelButton);
//
//    // Load all processes with the READY state from the database into the dropdown
//    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
//        String sql = "SELECT ID FROM pcb WHERE State = 'READY'";
//        try (PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                int id = rs.getInt("ID");
//                processDropdown.addItem("P" + id); // Only show process ID in the dropdown
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        showCustomDialog("Error loading processes.");
//    }
//
//    suspendButton.addActionListener(e -> {
//        String selectedProcess = (String) processDropdown.getSelectedItem();
//        if (selectedProcess == null) {
//            showCustomDialog("Please select a process.");
//            return;
//        }
//
//        int processID = Integer.parseInt(selectedProcess.substring(1)); // Extract process ID
//
//        // Update the state to SUSPENDED in the database
//        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            String updateSql = "UPDATE pcb SET State = 'BLOCKED' WHERE ID = ?";
//            try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
//                stmt.setInt(1, processID);
//                stmt.executeUpdate();
//                showCustomDialog("Process P" + processID + " is now BLOCKED.");
//                dialog.dispose();
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            showCustomDialog("Error suspending the process.");
//        }
//    });
//
//    cancelButton.addActionListener(e -> dialog.dispose());
//
//    dialog.setVisible(true);
//}
