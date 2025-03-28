import javax.swing.*;
import java.awt.*;

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
        contentPnl.add(new TransportationPnl(),"Transportation");
        contentPnl.add(new AccomodationPnl(),"Accomodation");
        contentPnl.add(new AttractionPnl(),"Attractions");
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
        SwingUtilities.invokeLater(TravelApp::new);
    }
}
