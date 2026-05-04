import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Configuration extends JFrame {

    private DefaultTableModel tableModel;
    private JTable processTable;
    private static pms pmsWindow;  // Static reference to the PMS class to track its state

    public Configuration() {
        // Frame settings
        setTitle("Configuration");
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
                ImageIcon logoIcon = new ImageIcon("D:/Subjects/Advance computer Programming/Operating System/photos/pcbb.jpeg");
                g.drawImage(logoIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        tableBackgroundPanel.setLayout(new BorderLayout());
        tableBackgroundPanel.setOpaque(true);

        // Create table model and table
        String[] columnNames = {"Process ID", "State", "Owner", "Priority", "Parent", "Memory",
                "Allocated Memory", "CPU Registers", "Processor", "BurstTime","ArrivalTime"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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

        // Set main panel as the content pane
        setContentPane(mainPanel);

        // Load data from database
        loadDataFromDatabase();
        // Create and add the BottomNavBar
        BottomNavBar bottomNavBar = new BottomNavBar();
        add(bottomNavBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadDataFromDatabase() {
        String URL = "jdbc:mysql://localhost:3306/operatingsystem";
        String USER = "root";
        String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM pcb";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int processID = rs.getInt("ID");
                    String state = rs.getString("State");
                    String owner = rs.getString("Owner");
                    int priority = rs.getInt("Priority");
                    String parent = rs.getString("Parent");
                    String memory = rs.getString("Memory");
                    String allocatedMemory = rs.getString("AllocatedMemory");
                    String cpuRegisters = rs.getString("CPURegisters");
                    String processor = rs.getString("Processor");
                    int burstTime = rs.getInt("BurstTime");
                    Timestamp arrivalTime = rs.getTimestamp("ArrivalTime"); // Fetch ArrivalTime
                    String[] row = {
                            String.valueOf(processID),
                            state,
                            owner,
                            String.valueOf(priority),
                            parent,
                            memory,
                            allocatedMemory,
                            cpuRegisters,
                            processor,
                            String.valueOf(burstTime),
                            arrivalTime != null ? arrivalTime.toString() : "N/A" // Format ArrivalTime
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Configuration configWindow = new Configuration();
            configWindow.setVisible(true);
        });
    }

    // New method to insert data into the database
    public void insertData(int processID, String state, String owner, int priority, String parent, String memory,
                           String allocatedMemory, String cpuRegisters, String processor,int burstTime,Timestamp arrivalTime) {
        String URL = "jdbc:mysql://localhost:3306/operatingsystem";
        String USER = "root";
        String PASSWORD = "";

        String sql = "INSERT INTO pcb (ID, State, Owner, Priority, Parent, Memory, AllocatedMemory, CPURegisters, Processor,BurstTime,ArrivalTime) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, processID);
            stmt.setString(2, state);
            stmt.setString(3, owner);
            stmt.setInt(4, priority);
            stmt.setString(5, parent);
            stmt.setString(6, memory);
            stmt.setString(7, allocatedMemory);
            stmt.setString(8, cpuRegisters);
            stmt.setString(9, processor);
            stmt.setInt(10, burstTime);
            stmt.setTimestamp(11, arrivalTime); // Set ArrivalTime

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error inserting data into database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
