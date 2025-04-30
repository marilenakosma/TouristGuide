import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class TransportationPnl extends JPanel{

    private static final int MAP_WIDTH =700;
    private static final int MAP_HEIGHT = 800;

    //private JTable schedule;
    private JList<String> route;
    private JButton metroBtn,busBtn,taxiBtn;

    public TransportationPnl(TravelApp app) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        JButton backBtn = createBackButton(app);
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Available Transportation Options",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30));

        topPanel.add(backBtn,BorderLayout.WEST);
        topPanel.add(title,BorderLayout.CENTER);

        add(topPanel,BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(3,1,5,5));
        btnPanel.setLayout(new BoxLayout(btnPanel,BoxLayout.Y_AXIS));
        btnPanel.setOpaque(false);
        
        metroBtn = createButton("Metro", "Images/Metro.png");
        //metroBtn = createButton("Metro",new FlatSVGIcon("Images/metro.svg"));
        busBtn = createButton("Bus", "Images/Bus.png");
        taxiBtn = createButton("Taxi",  "Images/Taxi.png");

        btnPanel.add(metroBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(busBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(taxiBtn);

        add(btnPanel,BorderLayout.WEST);


        // Optional: Map image (can be placed in the center or a different section)
        JXMapViewer mapViewer = createMapViewer();
        mapViewer.setPreferredSize(new Dimension(MAP_WIDTH,MAP_HEIGHT));
        add(mapViewer,BorderLayout.EAST);
        
        
        addActionListeners();
    }  

    public class TransportWaypoint extends DefaultWaypoint {
        private final String label;

        public TransportWaypoint(GeoPosition pos,String label) {
            super(pos);
            this.label = label;
        }
        public String getLabel() {
            return label;
        }
    }

        private JXMapViewer createMapViewer() {
                JXMapViewer mapViewer = new JXMapViewer();
                mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
                mapViewer.setZoom(5);
                mapViewer.setAddressLocation(new GeoPosition(45.658,25.601));
                
                Set<TransportWaypoint> waypoints = new HashSet<>();
                waypoints.add(new TransportWaypoint(new GeoPosition(45.7, 25.6), "Airport"));
                waypoints.add(new TransportWaypoint(new GeoPosition(45.66, 25.6), "Train Station"));
                waypoints.add(new TransportWaypoint(new GeoPosition(45.65, 25.58), "Metro Station"));
                waypoints.add(new TransportWaypoint(new GeoPosition(45.67, 25.62), "Museum"));
                
                WaypointPainter<TransportWaypoint> waypointPainter = new WaypointPainter<TransportWaypoint>() {
                  protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
                   for(TransportWaypoint w : getWaypoints()) {
                    Point2D point = map.getTileFactory().geoToPixel(w.getPosition(),map.getZoom());
                    Rectangle viewport = map.getViewportBounds();
                    int x = (int) (point.getX() - viewport.getX());
                    int y = (int) (point.getY() - viewport.getY());

                    g.setColor(Color.RED);
                    g.fillOval(x - 5,y-5,10,10);
                    g.setColor(Color.BLACK);
                    g.drawString(w.getLabel(),x+6,y);
                   }
                 }
              };
              waypointPainter.setWaypoints(waypoints);

              mapViewer.setOverlayPainter(new CompoundPainter<>(waypointPainter));
              
              mapViewer.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                Point mousePoint = e.getPoint();
                for (TransportWaypoint w : waypoints) {
                    Point2D point = mapViewer.getTileFactory().geoToPixel(w.getPosition(),mapViewer.getZoom());
                    Rectangle viewport = mapViewer.getViewportBounds();
                    int x = (int) (point.getX() - viewport.getX());
                    int y = (int) (point.getY() - viewport.getY());

                    if(mousePoint.distance(x,y) < 10) {
                        JOptionPane.showMessageDialog(mapViewer,w.getLabel(),"Location Info",JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
               }
              });
                return mapViewer;
        }
        private JButton createButton(String text,String iconPath)  {
            JButton button = new JButton(text);
            button.putClientProperty("JButton.buttonType", "roundRect");
            button.setMaximumSize(new Dimension(200, 60));

            try {
                ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
                Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
                
            } catch (Exception e) {
                System.err.println("Could not load icon: " + iconPath);
            }
            return button;
        }

        private JButton createBackButton(TravelApp app) {
            JButton backBtn = new JButton("Back");
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setBackground(new Color(173,216,230));
            backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            backBtn.setFont(backBtn.getFont().deriveFont(Font.PLAIN, 22));
            backBtn.addActionListener(e-> app.showPanel("Home"));
            return backBtn;
        }

         
         private void addActionListeners() {
            metroBtn.addActionListener(e -> showMessage("Metro button clicked!"));
            busBtn.addActionListener(e -> showMessage("Bus button clicked!"));
            taxiBtn.addActionListener(e -> showMessage("Taxi button clicked!"));
         }

         private void showMessage(String message) {
            JOptionPane.showMessageDialog(this,message,"Information",JOptionPane.INFORMATION_MESSAGE);
         }
        
    }


