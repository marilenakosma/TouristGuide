import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class TransportationPnl extends JPanel{

    private static final int MAP_WIDTH =400;
    private static final int MAP_HEIGHT = 300;

    private JTable schedule;
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

        JPanel routesPanel = new JPanel(new BorderLayout());

        DefaultListModel<String> routeListModel = new DefaultListModel<>();
        routeListModel.addElement("Route 1: City Center - Airport");
        routeListModel.addElement("Route 2: City Center - Train Station");
        routeListModel.addElement("Route 3: City Center - Museum");

        route = new JList<>(routeListModel);
        route.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JScrollPane routeScrollPane = new JScrollPane(route);
        routeScrollPane.setBorder(BorderFactory.createTitledBorder("Available Routes"));
        add(routeScrollPane,BorderLayout.CENTER);
        // Optional: Map image (can be placed in the center or a different section)
        JXMapViewer mapViewer = createMapViewer();
        mapViewer.setPreferredSize(new Dimension(400,300));
        add(mapViewer,BorderLayout.EAST);
        
        
        addActionListeners();
    }  

        private JXMapViewer createMapViewer() {
            TileFactoryInfo info = new TileFactoryInfo(1,17,17,256,true,true,"https://tile.openstreetmap.org",
                "x", "y", "z") {
                    public String getTileUrl(int x,int y,int zoom) {
                     int z = 17 - zoom;
                     return this.baseURL + "/" + z + "/" + x +"/" + y + ".png";
                    }
                };

                DefaultTileFactory tileFactory = new DefaultTileFactory(info);
                JXMapViewer viewer = new JXMapViewer();
                viewer.setTileFactory(tileFactory);

                GeoPosition brasov = new GeoPosition(45.658,25.601);
                viewer.setZoom(5);
                viewer.setAddressLocation(brasov);

                Set<Waypoint> waypoints = new HashSet<>();
                waypoints.add(new DefaultWaypoint(brasov));
                waypoints.add(new DefaultWaypoint(new GeoPosition(45.66,25.59)));

                WaypointPainter<Waypoint> painter = new WaypointPainter<>();
                painter.setWaypoints(waypoints);
                viewer.setOverlayPainter(painter);

                return viewer;
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


