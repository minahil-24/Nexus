import javax.swing.*;
import javax.swing.border.Border; // Import Border class
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class BottomNavBar extends JPanel {
    public BottomNavBar() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 51));

        // Search Bar on the left with increased width
        JTextField searchBar = new JTextField("Search...");
        searchBar.setPreferredSize(new Dimension(250, 30));
        searchBar.setOpaque(false);
        searchBar.setForeground(Color.WHITE);

        // Create a compound border for rounded corners
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        searchBar.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        add(searchBar, BorderLayout.WEST);

        // Time and Icons on the right
        JPanel rightIconsPanel = new JPanel();
        rightIconsPanel.setBackground(new Color(0, 0, 51));
        rightIconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Battery Icon on the left
        try {
            BufferedImage batteryImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/battery-modified.png"));
            Image scaledBatteryImage = batteryImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel batteryIcon = new JLabel(new ImageIcon(scaledBatteryImage));
            rightIconsPanel.add(batteryIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wi-Fi Icon
        try {
            BufferedImage wifiImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/wifi1.png"));
            Image scaledWifiImage = wifiImage.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            JLabel wifiIcon = new JLabel(new ImageIcon(scaledWifiImage));
            rightIconsPanel.add(wifiIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Volume Icon
        try {
            BufferedImage volumeImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/volumne-modified.png"));
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

        add(rightIconsPanel, BorderLayout.EAST);
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
}
