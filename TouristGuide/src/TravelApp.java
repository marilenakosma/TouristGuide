import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class TravelApp {
  private JFrame frame;
  private JPanel contentPnl;
  private CardLayout cardLayout;

    public TravelApp() {
        frame = new JFrame("Tourism App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentPnl = new JPanel(cardLayout);

        contentPnl.add(new HomePanel(this), "Home");
        contentPnl.add(new TransportationPnl(this),"Transportation");
        contentPnl.add(new AccomodationPnl(this),"Accomodation");
        contentPnl.add(new AttractionPnl(this),"Attractions");
        contentPnl.add(new MapPnl(),"City Map");
        
        cardLayout.show(contentPnl, "Home");

        frame.add(contentPnl, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    // Switches content when a button is clicked
    public void showPanel(String panelName) {
        cardLayout.show(contentPnl, panelName);
    }
    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
        } catch(Exception e) {
            System.err.println("Failed to initialize LaF: " + e.getMessage());
        }
         try {
            // Use class loader to find the font resource
            URL fontURL = TravelApp.class.getClassLoader().getResource("fonts/Roboto-Regular.ttf");

            if (fontURL != null) {
                // Create the font using the input stream
                InputStream fontStream = fontURL.openStream();
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                customFont = customFont.deriveFont(25f); // Set the font size

                // Apply font globally
                UIManager.put("Button.font", customFont);
                UIManager.put("Label.font", customFont);
                UIManager.put("TextField.font", customFont);
                UIManager.put("TextArea.font", customFont);
                UIManager.put("defaultFont", new Font("Inter", Font.PLAIN, 14)); // or any nice sans-serif
                UIManager.put("Button.arc", 20);       // Rounder buttons
                UIManager.put("Component.arc", 10);    // Rounder scroll panes, fields
                UIManager.put("TextComponent.arc", 10);

            } else {
                System.out.println("Font file not found!");
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        //Font customFont = new Font("Helvetica", Font.PLAIN, 16);
        //UIManager.put("Button.font", customFont);
       // UIManager.put("Label.font", customFont);

        SwingUtilities.invokeLater(TravelApp::new);
    }
}
