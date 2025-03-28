import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

//C:/Users/Marilena/Documents/Uni/5o etos/10o/Erasmus/Prog.IV(MAE)/Images/back.png
public class HomePanel extends JPanel {
    private Image backgroundImage;
    public HomePanel(TravelApp app) {

      // Load the background image using ClassLoader
      try {
        // Load the image from the resources folder
        URL imageURL = getClass().getClassLoader().getResource("Images/back.png");
        
        if (imageURL != null) {
            backgroundImage = ImageIO.read(imageURL); // Read the image
        } else {
            System.out.println("Image not found!");  // If the image is not found, print a message
        }
    } catch (IOException e) {
        e.printStackTrace();  // Handle any IO exceptions
    }

    // If you want to do any other setup, like layout, do it here...


        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBackground(Color.WHITE); // White background

        JLabel title = new JLabel("Welcome to Smart Travel Guide",SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton TranspBtn = new ButtonStyles("Transportation");
        TranspBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        TranspBtn.addActionListener(e -> app.showPanel("Transportation"));

        JButton AccBtn = new ButtonStyles("Accommodation");
        AccBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        AccBtn.addActionListener(e -> app.showPanel("Accommodation"));

        JButton AttrBtn = new ButtonStyles("Attractions");
        AttrBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        AttrBtn.addActionListener(e -> app.showPanel("Attractions"));

        JButton MapBtn = new ButtonStyles("City Map");
        MapBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        MapBtn.addActionListener(e -> app.showPanel("City Map"));

        
        add(title);
        add(Box.createVerticalStrut(20));
        add(TranspBtn);
        add(Box.createVerticalStrut(10));
        add(AccBtn);
        add(Box.createVerticalStrut(10));
        add(AttrBtn);
        add(Box.createVerticalStrut(10));
        add(MapBtn);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // If the background image is loaded, draw it
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
    


