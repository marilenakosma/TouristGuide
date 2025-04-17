import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;

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
 
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBackground(Color.WHITE); // White background

        JLabel title = new JLabel("Welcome to Smart Travel Guide",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton TranspBtn = createButton("Transportation","Images/Metro.png");
        TranspBtn.addActionListener(e -> app.showPanel("Transportation"));

        JButton AccBtn = createButton("Accommodation","Images/Accomodation.png");
        AccBtn.addActionListener(e -> app.showPanel("Accommodation"));

        JButton AttrBtn = createButton("Attractions","Images/Attractions.png");
        AttrBtn.addActionListener(e -> app.showPanel("Attractions"));

        JButton MapBtn = createButton("City Map","Images/Map.png");
        MapBtn.addActionListener(e -> app.showPanel("City Map"));

        add(Box.createVerticalStrut(50));
        add(title);
        add(Box.createVerticalStrut(40));
        add(TranspBtn);
        add(Box.createVerticalStrut(40));
        add(AccBtn);
        add(Box.createVerticalStrut(40));
        add(AttrBtn);
        add(Box.createVerticalStrut(40));
        add(MapBtn);
    }

    private JButton createButton(String text,String iconPath)  {
        JButton button = new JButton(text);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.putClientProperty("JComponent.roundRect", true);
        button.setMaximumSize(new Dimension(500, 80));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center button
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            
        } catch (Exception e) {
            System.err.println("Could not load icon: " + iconPath);
        }
        return button;
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
    


