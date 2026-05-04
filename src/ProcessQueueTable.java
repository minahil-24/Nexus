import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ProcessQueueTable {

    // Method to create and show the JTable for processes in the queue
    public static void showProcessQueueTable(List<PCB.Process> processQueue) {
        // Define column names for the table
        String[] columnNames = {"Process ID", "Burst Time", "Arrival Time"};

        // Create a DefaultTableModel to hold the data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add process data to the model
        for (PCB.Process process : processQueue) {
            Object[] rowData = {process.getId(), process.getBurstTime(), process.getArrivalTime()};
            model.addRow(rowData);
        }

        // Create a JTable using the model
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing
            }

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));  // Transparent background
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.WHITE);  // Text color
                c.setBackground(new Color(0, 0, 0, 150));  // Transparent background
                return c;
            }
        };

        // Center align text in table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set table styles
        table.setOpaque(false);
        table.setForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setRowHeight(40);

        // Add scrollable pane to the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // Customize table headers
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 20));  // Larger font for header cells
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(new Color(0, 0, 0, 150));
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));  // Increase header height

        // Create a background panel for the table with an image
        JPanel tableBackgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load the background image for the table
                ImageIcon logoIcon = new ImageIcon("E:/Subjects/Advance computer Programming/Operating System/photos/pc-modifiedD.png");
                g.drawImage(logoIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        tableBackgroundPanel.setLayout(new BorderLayout());
        tableBackgroundPanel.setOpaque(true);
        tableBackgroundPanel.add(scrollPane, BorderLayout.CENTER);
        // Create a JFrame to display the table
        JFrame frame = new JFrame("Process Queue");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setSize(800, 600); // Set window size
        frame.setLocationRelativeTo(null); // Center the window
        frame.add(tableBackgroundPanel, BorderLayout.CENTER); // Add table to the frame

        // Display the frame
        frame.setVisible(true);
        // Create and add the BottomNavBar
        BottomNavBar bottomNavBar = new BottomNavBar();
        frame.add(bottomNavBar, BorderLayout.SOUTH);
    }
}
