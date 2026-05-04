import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("NexusOS Simulation Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open the window in full screen
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the wallpaper
        JPanel wallpaperPanel = new JPanel() {
            private BufferedImage backgroundImage;

            {
                try {
                    // Load the wallpaper image
                    backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/wallpaer3.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Draw the image to cover the entire panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        wallpaperPanel.setLayout(new BorderLayout());

        // Create a panel for buttons aligned to the left with GridLayout for 4 rows and 2 columns
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 20, 0)); // 4 rows, 2 columns, 20px horizontal and 0 vertical gaps
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Set margin for the button panel

        // Image paths for each button
        String[] iconPaths = {
                "E:/Subjects/Advance computer Programming/Operating System/src/pms-modified.png", // Process
                "E:/Subjects/Advance computer Programming/Operating System/src/chrome-modified.png",   // Config
                "E:/Subjects/Advance computer Programming/Operating System/src/mms-modified.png", // Memory
                "E:/Subjects/Advance computer Programming/Operating System/src/io-modified.png",      // I/O
                "E:/Subjects/Advance computer Programming/Operating System/src/cms-modified.png",   // Other
                "E:/Subjects/Advance computer Programming/Operating System/src/scheduler.png", // Scheduler
                "E:/Subjects/Advance computer Programming/Operating System/src/comm.png"      // Comm
        };

        // Names for each button
        String[] buttonNames = {
                "Process", "Chrome", "Memory", "I/O", "Massages", "", ""
        };

        // Create buttons with different icons and labels
        for (int i = 0; i < iconPaths.length; i++) {
            JPanel buttonWithLabel = new JPanel();
            buttonWithLabel.setLayout(new BorderLayout());
            buttonWithLabel.setOpaque(false);

            RoundedButton iconButton = new RoundedButton();
            // Load and resize icon image
            ImageIcon icon = new ImageIcon(iconPaths[i]);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Resize icon to fit button
            iconButton.setIcon(new ImageIcon(img));

            iconButton.setText("");
            iconButton.setToolTipText(buttonNames[i]);
            iconButton.setMargin(new Insets(0, 0, 0, 0));

            // Add action listener to the "Process" button
            if ("Process".equals(buttonNames[i])) {
                iconButton.addActionListener(e -> {
                    // Open the pms window when the Process button is clicked
                    new pms().setVisible(true);
                });
            }

            // Create label for the button
            JLabel label = new JLabel(buttonNames[i], SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 13));
            label.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

            // Add the button and label to the panel
            buttonWithLabel.add(iconButton, BorderLayout.CENTER);
            buttonWithLabel.add(label, BorderLayout.SOUTH);

            // Add the combined button panel to the button panel
            buttonPanel.add(buttonWithLabel);
        }

        // Add button panel to the left side of the wallpaper panel
        wallpaperPanel.add(buttonPanel, BorderLayout.WEST);

        // Add the wallpaper panel to the center of the frame
        add(wallpaperPanel, BorderLayout.CENTER);

        // Create Bottom Navigation Bar with custom color
        JPanel bottomNavBar = new JPanel();
        bottomNavBar.setLayout(new BorderLayout());
        bottomNavBar.setBackground(new Color(0, 0, 51));

        // Search Bar on the left
        JTextField searchBar = new JTextField("Search...");
        searchBar.setPreferredSize(new Dimension(250, 30));
        searchBar.setOpaque(false);
        searchBar.setForeground(Color.WHITE);

        // Create a compound border for rounded corners
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);

        // Set the compound border with padding and line border
        searchBar.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        bottomNavBar.add(searchBar, BorderLayout.WEST);

        // Time and Icons on the right
        JPanel rightIconsPanel = new JPanel();
        rightIconsPanel.setBackground(new Color(0, 0, 51));
        rightIconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Battery Icon
        try {
            BufferedImage batteryImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/battery-modified.png"));
            Image scaledBatteryImage = batteryImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel batteryIcon = new JLabel(new ImageIcon(scaledBatteryImage));
            rightIconsPanel.add(batteryIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wi-Fi Icon
        try {
            BufferedImage wifiImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/wifi1.png"));
            Image scaledWifiImage = wifiImage.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            JLabel wifiIcon = new JLabel(new ImageIcon(scaledWifiImage));
            rightIconsPanel.add(wifiIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Volume Icon
        try {
            BufferedImage volumeImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/volumne-modified.png"));
            Image scaledVolumeImage = volumeImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            JLabel volumeIcon = new JLabel(new ImageIcon(scaledVolumeImage));
            rightIconsPanel.add(volumeIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Time Label
        JLabel timeLabel = new JLabel(getCurrentTime());
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Date Label
        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Add time and date labels to the right panel
        rightIconsPanel.add(timeLabel);
        rightIconsPanel.add(dateLabel);

        bottomNavBar.add(rightIconsPanel, BorderLayout.EAST);

        // Add bottom navigation bar to the frame
        add(bottomNavBar, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Method to get the current time in HH:mm format
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm /");
        return formatter.format(new Date());
    }

    // Method to get the current date in "dd MMMM yyyy" format
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.format(new Date());
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
            Icon icon = getIcon();
            if (icon != null) {
                int width = getWidth();
                int height = getHeight();
                icon.paintIcon(this, g, (width - icon.getIconWidth()) / 2, (height - icon.getIconHeight()) / 2);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 80);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
