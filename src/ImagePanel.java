import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage backgroundImage;
    private boolean isImageChanging = false; // Flag to prevent overlapping image changes

    public ImagePanel() {
        setBackground(new Color(220, 220, 220)); // Light grey background
        setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/pms-modified.png"));
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

    public void updateImage(String imagePath) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    backgroundImage = ImageIO.read(new File(imagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                isImageChanging = false; // Allow future image changes after the current one is finished
                repaint(); // Repaint the image panel to reflect the new image
            }
        }.execute(); // Execute the SwingWorker in the background
    }

    public void resetImage() {
        try {
            backgroundImage = ImageIO.read(new File("E:/Subjects/Advance computer Programming/Operating System/photos/pms-modified.png"));
            repaint(); // Repaint the image panel to reflect the reset image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
