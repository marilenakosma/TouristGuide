import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MapPnl extends JPanel{
    private Image mapImage;

    public MapPnl() {
      setLayout(null);

      try {
        mapImage = new ImageIcon(getClass().getClassLoader().getResource("Images/Map.jpg")).getImage();
      } catch(Exception e) {
        System.err.println("Map image not found!");
      }

      setPreferredSize(new Dimension(800,600));

      addMarker("Museum",250,150);
      addMarker("Airport",300,100);
    }

    private void addMarker(String name,int x,int y) {
        JButton marker = new JButton("⚫");
        marker.setToolTipText(name);
        marker.setBounds(x,y,40,30);
        marker.setFocusPainted(false);
        marker.setContentAreaFilled(false);
        marker.setBorderPainted(false);

        marker.addActionListener(e ->
            JOptionPane.showMessageDialog(this, name + " clicked!", "Location", JOptionPane.INFORMATION_MESSAGE)
        );

        add(marker);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(mapImage != null) {
            g.drawImage(mapImage,0,0,getWidth(),getHeight(),this);
        }
    }
}
