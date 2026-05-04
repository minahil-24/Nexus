import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("NexusOS Simulation Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the wallpaper with null layout for absolute positioning
        JPanel wallpaperPanel = new JPanel(null) {
            private BufferedImage backgroundImage;

            {
                try {
                    backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/wallpaper.jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Panel for buttons on the left with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(20, 20, 200, 400); // Set bounds to position it manually

        // Define button icons and labels
        String[] iconPaths = {
                "E:/Subjects/Advance computer Programming/Operating System/photos/pms-modified.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/chrome-modified.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/mms-modified.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/io-modified.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/cms-modified.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/scheduler.png",
                "E:/Subjects/Advance computer Programming/Operating System/photos/" +
                        "" +
                        "comm.png"
        };

        String[] buttonNames = {"Process", "Chrome", "Memory", "I/O", "Messages", "", ""};

        for (int i = 0; i < iconPaths.length; i++) {
            JPanel buttonWithLabel = new JPanel(new BorderLayout());
            buttonWithLabel.setOpaque(false);

            RoundedButton iconButton = new RoundedButton();
            ImageIcon icon = new ImageIcon(iconPaths[i]);
            Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconButton.setIcon(new ImageIcon(img));
            iconButton.setToolTipText(buttonNames[i]);

            // Action listeners for buttons
            if ("Process".equals(buttonNames[i])) {
                iconButton.addActionListener(e -> new pms().setVisible(true));
            }  else if ("Memory".equals(buttonNames[i])) {
                iconButton.addActionListener(e -> new Memorytable().setVisible(true));
            } else if ("Chrome".equals(buttonNames[i])) {
                iconButton.addActionListener(e -> {
                    try {
                        Runtime.getRuntime().exec("C:/Program Files/Google/Chrome/Application/chrome.exe");
                        setExtendedState(JFrame.MAXIMIZED_BOTH);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }


            JLabel label = new JLabel(buttonNames[i], SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.BOLD, 13));

            buttonWithLabel.add(iconButton, BorderLayout.CENTER);
            buttonWithLabel.add(label, BorderLayout.SOUTH);
            buttonPanel.add(buttonWithLabel);
        }

        wallpaperPanel.add(buttonPanel);

        // SemiCirclePanel at the bottom-right
        SemiCirclePanel semiCirclePanel = new SemiCirclePanel();
        semiCirclePanel.setBounds(0, getHeight() - 45, getWidth() + 565, 150);
        wallpaperPanel.add(semiCirclePanel);

        // Weather and Spotify Panel in the center
        WeatherSpotifyPanel weatherSpotifyPanel = new WeatherSpotifyPanel();
        weatherSpotifyPanel.setBounds(899, 60, 280, 160); // Set desired position and size
        wallpaperPanel.add(weatherSpotifyPanel);

        add(wallpaperPanel);
        setVisible(true);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm /");
        return formatter.format(new Date());
    }

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

    // Custom panel for semi-circle bottom-right info display
    class SemiCirclePanel extends JPanel {
        public SemiCirclePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(300, 150));
            setBounds(200, 140, 300, 150); // Move 10 pixels up

            setLayout(null);

            // Search Bar positioned at the bottom-left
            JTextField searchBar = new JTextField("Search...");
            searchBar.setPreferredSize(new Dimension(250, 30));
            searchBar.setOpaque(false);
            Border paddingBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);
            searchBar.setForeground(Color.WHITE);
            Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 1);
            searchBar.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
            searchBar.setBounds(70, 110, 250, 30); // Position the search bar at the bottom left
            add(searchBar);


            addIconLabel("E:/Subjects/Advance computer Programming/Operating System/photos/battery-modified.png", 30, 30, 950, 110);
            addIconLabel("E:/Subjects/Advance computer Programming/Operating System/photos/wifi1.png", 40, 30, 1000, 110);
            addIconLabel("E:/Subjects/Advance computer Programming/Operating System/photos/volumne-modified.png", 25, 25, 1050, 110);

            // Time label
            JLabel timeLabel = new JLabel(getCurrentTime());
            timeLabel.setForeground(Color.WHITE);
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            timeLabel.setBounds(1105, 110, 80, 20);
            add(timeLabel);

            // Date label
            JLabel dateLabel = new JLabel(getCurrentDate());
            dateLabel.setForeground(Color.WHITE);
            dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            dateLabel.setBounds(1155
                    , 110, 120, 20);
            add(dateLabel);
        }

        private void addIconLabel(String path, int width, int height, int x, int y) {
            try {
                BufferedImage iconImage = ImageIO.read(new File(path));
                Image scaledImage = iconImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
                iconLabel.setBounds(x, y, width, height);
                add(iconLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillArc(0, 0, getWidth() * 2, getHeight() * 2, 0, 180);

        }
    }

    class WeatherSpotifyPanel extends JPanel {
        private JLabel temperatureLabel;
        private JLabel conditionLabel;

        public WeatherSpotifyPanel() {

            setOpaque(false);
            setLayout(null);

            // Temperature label
            temperatureLabel = new JLabel();
            temperatureLabel.setForeground(Color.WHITE);
            temperatureLabel.setFont(new Font("Arial", Font.BOLD, 20));
            temperatureLabel.setBounds(20, 50, 160, 30); // Adjust position
            add(temperatureLabel);

            // Weather condition label
            conditionLabel = new JLabel();
            conditionLabel.setForeground(Color.WHITE);
            conditionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            conditionLabel.setBounds(20, 80, 160, 30); // Adjust position
            add(conditionLabel);

            JButton spotifyButton = new JButton();
            ImageIcon spotifyIcon = new ImageIcon("E:/Subjects/Advance computer Programming/Operating System/photos/spotify-logo.png");
            Image img = spotifyIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            spotifyButton.setIcon(new ImageIcon(img));
            spotifyButton.setContentAreaFilled(false);
            spotifyButton.setOpaque(false);
            spotifyButton.setFocusPainted(false);
            spotifyButton.setBorderPainted(false);
            spotifyButton.setBounds(150, 40, 100, 95);

            spotifyButton.addActionListener(e -> {
                try {
                    Desktop.getDesktop().browse(new URI("https://open.spotify.com/"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });
            add(spotifyButton);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateWeather();
                    repaint();
                }
            }, 0, 10000); // Update every 10 seconds
        }

        private void updateWeather() {
            String[] weatherConditions = {"☀️ Sunny", "☁️ Cloudy", "🌧️ Rainy", "🌩️ Stormy"};
            int temperature = 20 + (int) (Math.random() * 10);
            String condition = weatherConditions[(int) (Math.random() * weatherConditions.length)];

            // Set font that supports emojis
            Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 16); // Use "Segoe UI Emoji" for Windows or "Noto Color Emoji" for Linux
            temperatureLabel.setFont(emojiFont);
            conditionLabel.setFont(emojiFont);

            temperatureLabel.setText("Temperature: " + temperature + "°C");
            conditionLabel.setText( condition);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Dashboard::new);
    }
}
