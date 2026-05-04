import javax.swing.*;
import javax.swing.border.Border; // Import Border class
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.List;
import java.util.ArrayList;

public class pms extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/operatingsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    // Inside your pms class, update the image panel
    private BufferedImage backgroundImage;
    private boolean isImageChanging = false; // Flag to prevent overlapping image changes
private static Dashboard dashboardWindow;
    public pms() {
        setTitle("PMS Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open the window in full screen
        setSize(800, 600);
        // Do nothing when the close button is clicked
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Add window listener to handle the close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                dashboardWindow.toFront();  // Assuming PMS is the main class for the application
            }
        });
        setLayout(new BorderLayout());

        // Create a panel for the wallpaper with a light grey background
        JPanel wallpaperPanel = new JPanel();
        wallpaperPanel.setBackground(new Color(220, 220, 220)); // Light grey background
        wallpaperPanel.setLayout(new BorderLayout());

        // Create a panel for buttons aligned in two columns
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns with gaps
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image paths for each button
        String[] iconPaths = {
                "E:/Subjects/Advance computer Programming/Operating System/photos/cp-modified.png", // Create Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/dp-modified.png",   // Destroy Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/sp-modified.png", // Suspend Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/rp-modified.png",      // Resume Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/bp-modified.png",   // Block Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/wp-modified.png", // Wakeup Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/dpp-modified.png",      // Dispatch Process
                "E:/Subjects/Advance computer Programming/Operating System/photos/cpp-modified.png",      // Change Process Priority
                "E:/Subjects/Advance computer Programming/Operating System/photos/pc-modified.png",    // Process Communication
                "E:/Subjects/Advance computer Programming/Operating System/photos/pcbb-modified.png"
        };

// Add a panel with a background image positioned at the top-right corner
        JPanel imagePanel = new JPanel() {
            {
                try {
                        // Load the image for the background
                        backgroundImage = ImageIO.read(new File("D" +
                                "E:/Subjects/Advance computer Programming/Operating System/photos/pms-modified.png"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the image to cover the entire panel area
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 270, 55, 400, 400, this);
                }
            }
        };



// Set the size and position of the image panel with a top-right margin
        imagePanel.setBackground(new Color(220, 220, 220)); // Light grey background
        imagePanel.setPreferredSize(new Dimension(100, 50)); // Set the new, smaller size
        imagePanel.setBounds(getWidth() - 220, 20, 200, 50); // Adjust position for 20-pixel margin

// Add a text label at the bottom of the image panel
        JLabel textLabel = new JLabel("Process Management System", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font to bold with size 20
        textLabel.setForeground(Color.BLACK); // Set text color
// Add margin to the text label using an empty border
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 258, 120, 0)); // Top, Left, Bottom, Right margins

// Add the label to the bottom of the image panel using BorderLayout
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(textLabel, BorderLayout.SOUTH);

// Add the panel to the main wallpaper panel
        wallpaperPanel.add(imagePanel);

        // Names for each button
        String[] buttonNames = {
                "Create Process", "Destroy Process", "Suspend Process", "Resume Process", "Block Process",
                "Wakeup Process", "Dispatch Process", "Change Process Priority", "Process Communication","PCB"
        };

        // Create buttons with icons and labels
        for (int i = 0; i < iconPaths.length; i++) {
            // Create a final variable to capture the value of i
            final int index = i;
            JPanel buttonWithLabel = new JPanel();
            buttonWithLabel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Horizontal layout for icon and label
            buttonWithLabel.setOpaque(false);

            RoundedButton iconButton = new RoundedButton();
            // Load and resize icon image
            ImageIcon icon = new ImageIcon(iconPaths[i]);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Resize icon to fit button
            iconButton.setIcon(new ImageIcon(img));

            iconButton.setText(""); // Remove text for the button
            iconButton.setToolTipText(buttonNames[i]); // Tooltip for each icon
            iconButton.setMargin(new Insets(0, 0, 0, 0)); // Remove padding

            // Create label for the button
            JLabel label = new JLabel(buttonNames[i]);
            label.setForeground(Color.BLACK); // Label color
            label.setFont(new Font("Arial", Font.BOLD, 14)); // Font size
            // Handle "Configuration" button action

            if (buttonNames[i].equals("PCB")) {
                iconButton.addActionListener(e -> {
                    Configuration configWindow = new Configuration();
                    configWindow.setVisible(true);  // Show the configuration window
                });
            }

            if (buttonNames[i].equals("Destroy Process")) {
                iconButton.addActionListener(e -> {
                    PCB.destroyLastProcess();  // Call the destroy method when the button is clicked
                });
            }
            if (buttonNames[i].equals("Process Communication")) {
                iconButton.addActionListener(e -> {
                    // Get the process queue from PCB (assuming it's public)
                    List<PCB.Process> processQueue = PCB.processQueue;

                    // Call the showProcessQueueTable method to display the queue
                    ProcessQueueTable.showProcessQueueTable(processQueue); // Show the configuration window
                });
            }
            if (buttonNames[i].equals("Change Process Priority")) {
                iconButton.addActionListener(e -> {
              PCB.changeProcessPriority();
                });
            }


// Handle "Create Process" button action
            if (buttonNames[i].equals("Suspend Process")) {
                iconButton.addActionListener(e -> {
                    // Call the createProcess method from PCB class when button is clicked
                    PCB.suspendProcess();  // Static method call

                });
            }
            if (buttonNames[i].equals("Block Process")) {
                iconButton.addActionListener(e -> {
                    // Call the createProcess method from PCB class when button is clicked
                    PCB.BlockProcess();  // Static method call

                });
            }
            if (buttonNames[i].equals("Resume Process")) {
                iconButton.addActionListener(e -> {
                    // Call the createProcess method from PCB class when button is clicked
                    PCB.resumeProcess();  // Static method call

                });
            }
            if (buttonNames[i].equals("Wakeup Process")) {
                iconButton.addActionListener(e -> {
                    // Call the createProcess method from PCB class when button is clicked
                    PCB.wakeupProcess();  // Static method call

                });
            }
// Handle "Create Process" button action
            if (buttonNames[i].equals("Create Process")) {
                iconButton.addActionListener(e -> {
                    // Call the createProcess method from PCB class when button is clicked
                    PCB.createProcess();  // Static method call

                });
            }
            if (buttonNames[i].equals("Dispatch Process")) {
                iconButton.addActionListener(e -> showDispatchDialog());
            }

            iconButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Only change image if it's not already changing
                    if (!isImageChanging) {
                        isImageChanging = true; // Set flag to prevent further image changes
                        updateImage(iconPaths[index]); // Update the image using the provided path
                        textLabel.setText(buttonNames[index]);  // Set the text in the label
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Reset the image to the original PMS image when mouse exits
                    if (backgroundImage != null) {
                        isImageChanging = false; // Prevent further image changes
                        try {
                            backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/pms-modified.png"));
                            imagePanel.repaint(); // Repaint the image panel to reflect the reset image
                            textLabel.setText("Process Management System");  // Reset the label text
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                // Method to update the image on the imagePanel
                public void updateImage(String imagePath) {
                    new SwingWorker<Void, Void>() { // Use SwingWorker for loading image in background
                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                backgroundImage = ImageIO.read(new File(imagePath));  // Load the new image
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            isImageChanging = false; // Allow future image changes after the current one is finished
                            imagePanel.repaint(); // Repaint the image panel to reflect the new image
                        }
                    }.execute(); // Execute the SwingWorker in the background
                }

            });


            // Add the button and label to the panel
            buttonWithLabel.add(iconButton);
            buttonWithLabel.add(label);

            // Add the combined button panel to the button panel
            buttonPanel.add(buttonWithLabel);
        }

        // Add button panel to the left side of the wallpaper panel
        wallpaperPanel.add(buttonPanel, BorderLayout.WEST);


        // Add the wallpaper panel to the center of the frame
        add(wallpaperPanel, BorderLayout.CENTER);
        // Create and add the BottomNavBar
        BottomNavBar bottomNavBar = new BottomNavBar();
        add(bottomNavBar, BorderLayout.SOUTH);

        setVisible(true);

    }
    private void showDispatchDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Dispatch Process");
        dialog.setUndecorated(true);
        dialog.setSize(400, 250);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setOpacity(0.8f);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 200)); // Semi-transparent black background
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        dialog.setContentPane(contentPanel);
        contentPanel.setLayout(null);

        dialog.setShape(new RoundRectangle2D.Double(0, 0, 400, 250, 30, 30)); // Rounded corners

        JLabel titleLabel = new JLabel("Choose Scheduling Algorithm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 30, 300, 30);
        contentPanel.add(titleLabel);

        JButton fcfsButton = new JButton("FCFS");
        fcfsButton.setFont(new Font("Arial", Font.BOLD, 14));
        fcfsButton.setBounds(100, 100, 90, 40);
        fcfsButton.setOpaque(false);
        fcfsButton.setContentAreaFilled(false);
        fcfsButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        fcfsButton.setForeground(Color.WHITE);
        fcfsButton.setFocusPainted(false);
        contentPanel.add(fcfsButton);

        JButton sjfButton = new JButton("SJF");
        sjfButton.setFont(new Font("Arial", Font.BOLD, 14));
        sjfButton.setBounds(210, 100, 90, 40);
        sjfButton.setOpaque(false);
        sjfButton.setContentAreaFilled(false);
        sjfButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        sjfButton.setForeground(Color.WHITE);
        sjfButton.setFocusPainted(false);
        contentPanel.add(sjfButton);

        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(Color.RED);
        closeButton.setForeground(Color.WHITE);
        closeButton.setBounds(370, 10, 20, 20);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        contentPanel.add(closeButton);

        fcfsButton.addActionListener(e -> {
            // Add logic for FCFS scheduling
            dialog.dispose();
            //showCustomDialog("FCFS scheduling selected.");

            PCB.executeFCFS();
        });

        sjfButton.addActionListener(e -> {
            dialog.dispose();

            // Prompt the user to choose Preemptive or Non-Preemptive SJF
            String[] options = {"Preemptive", "Non-Preemptive"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Choose SJF Type:",
                    "SJF Scheduling",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == 0) {
                // Preemptive SJF (Shortest Remaining Time First)
                PCB.executePreemptiveSJF();
            } else if (choice == 1) {
                // Non-Preemptive SJF
                PCB.executeSJF();
            }
        });

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }


    // Custom rounded button class
    class RoundedButton extends JButton {
        public RoundedButton() {
            super();
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the icon filling the button completely
            Icon icon = getIcon();
            if (icon != null) {
                int width = getWidth();
                int height = getHeight();
                icon.paintIcon(this, g, (width - icon.getIconWidth()) / 2, (height - icon.getIconHeight()) / 2);
            }
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 80); // Set a preferred size for the button
        }
    }

    public static void main(String[] args) {
        PCB.resetProcessState();
        SwingUtilities.invokeLater(pms::new);

    }
}
