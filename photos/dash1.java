import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class dash1 extends JFrame {

    public dash1() {
        setTitle("OS");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        setSize(screenWidth, screenHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        getContentPane().setLayout(null); // Use null layout for absolute positioning

        // Create a new panel with a specified size
        JPanel imagePanel = new JPanel() {
            private BufferedImage backgroundImage;

            {
                try {
                    // Load the image
                    backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/src/pms-modified.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // Set the size of the panel
        imagePanel.setPreferredSize(new Dimension(550, 550));
        imagePanel.setBounds(screenWidth - 570, 20, 550, 550); // Position it at the top right with margin

        getContentPane().add(imagePanel); // Add the panel to the JFrame

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(dash1::new);
    }
}
