import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.io.*;
import java.net.*;
public class Memorytable extends JFrame {
    private DefaultTableModel tableModel;
    private JTable processTable;
    private static pms pmsWindow;  // Static reference to the PMS class to track its state

    public Memorytable() {
        // Frame settings
        setTitle("Memorytable");
        setSize(800, 600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // Do nothing when the close button is clicked
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Add window listener to handle the close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Handle close event and return to the PMS class
                dispose();
                pmsWindow.toFront();  // Ensure PMS window is opened if not already
            }
        });

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Background panel for the table with a background image
        JPanel tableBackgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image for table (adjust path as needed)
                ImageIcon logoIcon = new ImageIcon("E:/Subjects/Advance computer Programming/Operating System/photos/mms-modified.png");
                g.drawImage(logoIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        tableBackgroundPanel.setLayout(new BorderLayout());
        tableBackgroundPanel.setOpaque(true);

        // Create table model and table
        String[] columnNames = {"Process ID", "Memory", "AllocatedMemory"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;  // Only make AllocatedMemory column editable
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.WHITE);
                c.setBackground(new Color(0, 0, 0, 150));
                return c;
            }
        };

        // Center align text in table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < processTable.getColumnCount(); i++) {
            processTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        processTable.setOpaque(false);
        processTable.setForeground(Color.WHITE);
        processTable.setFillsViewportHeight(true);
        processTable.setRowHeight(40);

        JScrollPane scrollPane = new JScrollPane(processTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Customize table headers
        JTableHeader tableHeader = processTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 20));  // Larger font for header cells
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(new Color(0, 0, 0, 150));
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));  // Increase header height

        // Add scroll pane and background setup
        tableBackgroundPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tableBackgroundPanel, BorderLayout.CENTER);

        // Add Apply Paging Button at the bottom of the window
        JButton applyPagingButton = new JButton("Apply Paging");
        applyPagingButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyPagingButton.setBounds(350, 500, 150, 40);
        applyPagingButton.setOpaque(false); // Make background transparent
        applyPagingButton.setContentAreaFilled(false); // Remove filled background
        applyPagingButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Add white border
        applyPagingButton.setForeground(Color.black); // Set text color to white
        applyPagingButton.setFocusPainted(false); // Remove focus outline
        applyPagingButton.addActionListener(e -> applyPaging());


        // Position the button at the bottom-center of the window
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the button panel transparent
        buttonPanel.add(applyPagingButton, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set main panel as the content pane
        setContentPane(mainPanel);

        // Load data from database
        loadDataFromDatabase();

        setVisible(true);
    }

    private void applyPaging() {
        String URL = "jdbc:mysql://localhost:3306/operatingsystem";
        String USER = "root";
        String PASSWORD = "";
        int pageSize = 4;  // 4 KB per frame
        final int[] globalPageNumber = {1}; // Use array to make it effectively final
        final int[] pageReplacementStart = {11}; // Start page replacement from page 11 onwards

        List<Page> pages = new ArrayList<>(); // To track all pages for LRU replacement

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID, AllocatedMemory FROM pcb";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                // Modified the table model to exclude the "Process ID" column
                DefaultTableModel pagingTableModel = new DefaultTableModel(new String[]{"Page Number", "Address"}, 0);
                JTable pagingTable = new JTable(pagingTableModel);

                while (rs.next()) { // Loop through each process in the table
                    int processID = rs.getInt("ID");
                    int allocatedMemory = rs.getInt("AllocatedMemory");
                    int totalFrames = allocatedMemory / pageSize; // Calculate total frames for each process

                    for (int i = 0; i < totalFrames; i++) {
                        String address = "0x" + Integer.toHexString(globalPageNumber[0] * pageSize);

                        // Add page to the list (for LRU tracking)
                        pages.add(new Page(processID, globalPageNumber[0], address));

                        if (globalPageNumber[0] <= 10) {
                            pagingTableModel.addRow(new Object[]{globalPageNumber[0], address}); // Add only page number and address
                        }

                        globalPageNumber[0]++; // Increment globally for each page
                    }
                }

                JScrollPane pagingScrollPane = new JScrollPane(pagingTable);
                JPanel pagingPanel = new JPanel(new BorderLayout());
                pagingPanel.add(pagingScrollPane, BorderLayout.CENTER);

                // Apply LRU Button
                JButton applyLRUButton = new JButton("Apply LRU");
                applyLRUButton.addActionListener(e -> {
                    // Check if total pages exceed 10

                    if (pages.size() > 10) {
                        // Find the page with the smallest page number (Least Page Number)
                        Page leastPageNumberPage = pages.stream()
                                .min(Comparator.comparingInt(Page::getPageNumber))
                                .orElse(null);

                        if (leastPageNumberPage != null) {
                            // Use pageReplacementStart to get the next available page number for replacement
                            int newPageNumber = pageReplacementStart[0];  // Use page number starting from 11
                            pageReplacementStart[0]++; // Increment for the next page number

                            // Update the Process ID for the replaced page (it should match the current process)
                            leastPageNumberPage.setProcessID(leastPageNumberPage.getProcessID());

                            // Update the address for the new page
                            String newAddress = "0x" + Integer.toHexString(newPageNumber * pageSize);
                            leastPageNumberPage.setAddress(newAddress);
                            leastPageNumberPage.setPageNumber(newPageNumber);  // Assign the new page number

                            // Update the table to reflect changes
                            pagingTableModel.setRowCount(0); // Clear current rows
                            // Display updated pages, only showing the first 10 pages
                            for (int i = 0; i < Math.min(pages.size(), 10); i++) {
                                Page page = pages.get(i);
                                pagingTableModel.addRow(new Object[]{page.getPageNumber(), page.getAddress()});
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No pages left to replace", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(applyLRUButton);
                pagingPanel.add(buttonPanel, BorderLayout.SOUTH);

                JDialog pagingDialog = new JDialog(this, "Paging Table", true);
                pagingDialog.getContentPane().add(pagingPanel);
                pagingDialog.setSize(600, 400);
                pagingDialog.setLocationRelativeTo(this);
                pagingDialog.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying paging.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper class to track pages
    private class Page {
        private int processID;
        private int pageNumber;
        private String address;

        public Page(int processID, int pageNumber, String address) {
            this.processID = processID;
            this.pageNumber = pageNumber;
            this.address = address;
        }

        public int getProcessID() {
            return processID;
        }
        public void setProcessID(int id) {
            this.processID = id;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public String getAddress() {
            return address;
        }
        public void setAddress(String newAddress) {
            this.address = newAddress;
        }
    }

    private void loadProcessDataToComboBox(JComboBox<String> comboBox) {
        String URL = "jdbc:mysql://localhost:3306/operatingsystem";
        String USER = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID FROM pcb";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String processID = rs.getString("ID");
                    comboBox.addItem(processID); // Add process ID to ComboBox
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading process data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase() {
        String URL = "jdbc:mysql://localhost:3306/operatingsystem";
        String USER = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT ID, Memory, AllocatedMemory FROM pcb";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);
                 Socket socket = new Socket("localhost", 12345);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                while (rs.next()) {
                    int processID = rs.getInt("ID");
                    String memory = rs.getString("Memory");
                    String allocatedMemory = rs.getString("AllocatedMemory");

                    // Add row to table
                    tableModel.addRow(new Object[]{processID, memory, allocatedMemory});

                    // Send process info to the server
                    String message = "Process ID: " + processID + ", Memory: " + memory + ", Allocated Memory: " + allocatedMemory + " KB";
                    out.println(message);
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Memorytable memoryWindow = new Memorytable();
            memoryWindow.setVisible(true);
        });
    }
}
