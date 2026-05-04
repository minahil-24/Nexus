import javax.swing.*;
import javax.swing.border.Border; // Import Border class
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class pms extends JFrame {
    public pms() {
        setTitle("PMS Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open the window in full screen
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                "E:/Subjects/Advance computer Programming/Operating System/src/cp-modified.png", // Create Process
                "E:/Subjects/Advance computer Programming/Operating System/src/dp-modified.png",   // Destroy Process
                "E:/Subjects/Advance computer Programming/Operating System/src/sp-modified.png", // Suspend Process
                "E:/Subjects/Advance computer Programming/Operating System/src/rp-modified.png",      // Resume Process
                "E:/Subjects/Advance computer Programming/Operating System/src/bp-modified.png",   // Block Process
                "E:/Subjects/Advance computer Programming/Operating System/src/wp-modified.png", // Wakeup Process
                "E:/Subjects/Advance computer Programming/Operating System/src/dpp-modified.png",      // Dispatch Process
                "E:/Subjects/Advance computer Programming/Operating System/src/cpp-modified.png",      // Change Process Priority
                "E:/Subjects/Advance computer Programming/Operating System/src/pc-modified.png"      // Process Communication
        };

        // Names for each button
        String[] buttonNames = {
                "Create Process", "Destroy Process", "Suspend Process", "Resume Process", "Block Process",
                "Wakeup Process", "Dispatch Process", "Change Process Priority", "Process Communication"
        };

        // Create buttons with icons and labels
        for (int i = 0; i < iconPaths.length; i++) {
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

            // Add the button and label to the panel
            buttonWithLabel.add(iconButton);
            buttonWithLabel.add(label);

            // Add the combined button panel to the button panel
            buttonPanel.add(buttonWithLabel);
        }
// Add a panel with a background image positioned at the top-right corner
        JPanel imagePanel = new JPanel() {
            private BufferedImage backgroundImage;

            {
                try {
                    // Load the image for the background
                    backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/pms-modified.png"));
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
        textLabel.setBorder(BorderFactory.createEmptyBorder(0, 170, 140, 0)); // Top, Left, Bottom, Right margins

// Add the label to the bottom of the image panel using BorderLayout
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(textLabel, BorderLayout.SOUTH);

// Add the panel to the main wallpaper panel
        wallpaperPanel.add(imagePanel);



        // Add button panel to the left side of the wallpaper panel
        wallpaperPanel.add(buttonPanel, BorderLayout.WEST);


        // Add the wallpaper panel to the center of the frame
        add(wallpaperPanel, BorderLayout.CENTER);

        // Create Bottom Navigation Bar with custom color
        JPanel bottomNavBar = new JPanel();
        bottomNavBar.setLayout(new BorderLayout());
        bottomNavBar.setBackground(new Color(0, 0, 51));

        // Search Bar on the left with increased width
        JTextField searchBar = new JTextField("Search...");
        searchBar.setPreferredSize(new Dimension(250, 30));
        searchBar.setOpaque(false);
        searchBar.setForeground(Color.WHITE);

        // Create a compound border for rounded corners
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        searchBar.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        bottomNavBar.add(searchBar, BorderLayout.WEST);

        // Time and Icons on the right
        JPanel rightIconsPanel = new JPanel();
        rightIconsPanel.setBackground(new Color(0, 0, 51));
        rightIconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Battery Icon on the left
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
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm " + "/");
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
        SwingUtilities.invokeLater(pms::new);
    }
}
