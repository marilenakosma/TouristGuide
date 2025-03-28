import javax.swing.*;

public class MapPnl extends JPanel{
    public MapPnl() {
     JLabel label = new JLabel("Interactive City Map");
     add(label);

     ImageIcon globeIcon = new ImageIcon("Images/globe.png");
     JLabel globeLabel = new JLabel(globeIcon);
     add(globeLabel);
    }
}
