import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import javax.swing.event.MouseInputListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class TransportationPnl extends JPanel {

    private static final int MAP_WIDTH = 700;
    private static final int MAP_HEIGHT = 800;
    private JXMapViewer mapViewer;
    private MapOverlayPainter painter;
    private Map<GeoPosition, String> labeledPoints;


    private JButton AirportBtn,MuseumBtn;

    public TransportationPnl(TravelApp app) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title + Back
        JButton backBtn = createBackButton(app);
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Available Transportation Options", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30));
        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // Side Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        btnPanel.setOpaque(false);

        AirportBtn = createButton("Train Station to Airport", "Images/Bus.png");
        MuseumBtn = createButton("Train Station to Museum", "Images/Taxi.png");

        btnPanel.add(AirportBtn);
        btnPanel.add(MuseumBtn);

        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // Map
        JXMapViewer mapViewer = createMapViewer();
        mapViewer.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        add(mapViewer, BorderLayout.CENTER);

        addActionListeners();
    }

    private JXMapViewer createMapViewer() {
         mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        mapViewer.setZoom(5);
        //mapViewer.setZoomEnabled(true);
        mapViewer.setAddressLocation(new GeoPosition(45.658, 25.601));

        // Locations
        labeledPoints = new HashMap<>();
        GeoPosition airport = new GeoPosition(45.691,25.516);
        GeoPosition train = new GeoPosition(45.66115,25.61353);
        GeoPosition museum = new GeoPosition(45.67, 25.62);
    
        labeledPoints.put(airport, "Airport");
        labeledPoints.put(train, "Train Station");
        labeledPoints.put(museum, "Museum");

        painter = new MapOverlayPainter(new ArrayList<>(), labeledPoints);
        mapViewer.setOverlayPainter(painter);
        
        PanMouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));

        
        // Click handler
        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point mousePoint = e.getPoint();
                for (Entry<GeoPosition, String> entry : labeledPoints.entrySet()) {
                    Point2D geoPoint = mapViewer.getTileFactory().geoToPixel(entry.getKey(), mapViewer.getZoom());
                    Rectangle viewport = mapViewer.getViewportBounds();
                    int x = (int) (geoPoint.getX() - viewport.getX());
                    int y = (int) (geoPoint.getY() - viewport.getY());

                    if (mousePoint.distance(x, y) < 10) {
                        JOptionPane.showMessageDialog(mapViewer, entry.getValue(), "Location Info", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
            }
        });

        return mapViewer;
    }
    private void updateRoute(List<GeoPosition> newRoute) {
        painter.setRoutePoints(newRoute);
        mapViewer.repaint();
    }    

    // Painter class: routes and labeled markers
    static class MapOverlayPainter implements Painter<JXMapViewer> {
        private final List<GeoPosition> routePoints = new ArrayList<>();
        private final Map<GeoPosition, String> labeledPoints;

        public MapOverlayPainter(List<GeoPosition> initialRoute, Map<GeoPosition, String> labeledPoints) {
            this.routePoints.addAll(initialRoute);  // Use existing list
            this.labeledPoints = labeledPoints;
        }
        
        public void setRoutePoints(List<GeoPosition> points) {
            this.routePoints.clear();
            this.routePoints.addAll(points);
        }
          

        @Override
        public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            Rectangle viewport = map.getViewportBounds();

            // Draw lines
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            Point2D prev = null;
            for (GeoPosition gp : routePoints) {
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());
                if (prev != null) {
                    g2.drawLine((int) prev.getX(), (int) prev.getY(), x, y);
                }
                prev = new Point(x, y);
            }

            // Draw markers + labels
            for (Entry<GeoPosition, String> entry : labeledPoints.entrySet()) {
                Point2D pt = map.getTileFactory().geoToPixel(entry.getKey(), map.getZoom());
                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());

                g2.setColor(Color.RED);
                g2.fillOval(x - 5, y - 5, 10, 10);
                g2.setColor(Color.BLACK);
                g2.drawString(entry.getValue(), x + 8, y);
            }

            g2.dispose();
        }
    }

    // Button creator
    private JButton createButton(String text, String iconPath) {
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
        backBtn.setBackground(new Color(173, 216, 230));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setFont(backBtn.getFont().deriveFont(Font.PLAIN, 22));
        backBtn.addActionListener(e -> app.showPanel("Home"));
        return backBtn;
    }

    private void addActionListeners() {
        AirportBtn.addActionListener(e -> {
            List<GeoPosition> route = Arrays.asList(
                new GeoPosition(45.66115, 25.61353), // Train Station
                new GeoPosition(45.691, 25.516)      // Airport
            );
            updateRoute(route);
        });
    
        MuseumBtn.addActionListener(e -> {
            List<GeoPosition> route = Arrays.asList(
                new GeoPosition(45.66115, 25.61353), // Train Station
                new GeoPosition(45.67, 25.62)        // Museum
            );
            updateRoute(route);
        });
    }
    

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}

        



