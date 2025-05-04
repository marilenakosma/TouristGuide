import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import javax.swing.event.MouseInputListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;


public class TransportationPnl extends JPanel {

    private static final int MAP_WIDTH = 700;
    private static final int MAP_HEIGHT = 800;
    private JXMapViewer mapViewer;
    private MapOverlayPainter painter;
    private Map<GeoPosition, String> labeledPoints;

    private TransportationControls controlsPnl;
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
        
        controlsPnl = new TransportationControls();
        controlsPnl.getSearchButton().addActionListener(e -> handleSearch());
        add(controlsPnl,BorderLayout.WEST);

        // Side Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        btnPanel.setOpaque(false);

        AirportBtn = createButton("Images/Metro.png","to","Images/airport.png");
        MuseumBtn = createButton("Images/Metro.png","to","Images/museum.png");

        btnPanel.add(AirportBtn);
        btnPanel.add(MuseumBtn);

        topPanel.add(btnPanel, BorderLayout.SOUTH);

        // Map
        mapViewer = createMapViewer();
        mapViewer.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        add(mapViewer, BorderLayout.CENTER);

        addActionListeners();
    }

    private void handleSearch() {
        String from = (String) controlsPnl.getFromComboBox().getSelectedItem();
        String to = (String) controlsPnl.getToComboBox().getSelectedItem();
        String mode = controlsPnl.getSelectedMode();

        if (from == null || to == null || mode.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please select all options!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
         List<GeoPosition> route = new ArrayList<>();
        if (from.equals("Brasov") && to.equals("Bucharest")) {
            route.add(new GeoPosition(45.658, 25.601)); // Brasov
            route.add(new GeoPosition(44.4268, 26.1025)); // Bucharest
        }
        // Add more routes as needed...

        updateRoute(route);

        JOptionPane.showMessageDialog(this, "Trip planned from " + from + " to " + to + " by " + mode + "!", "Trip Planned", JOptionPane.INFORMATION_MESSAGE);
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
    private JButton createButton(String iconPath_to,String text,String iconPath_from) {
        JButton button = new JButton(text);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setMaximumSize(new Dimension(200, 60));

        try {

            URL to = getClass().getClassLoader().getResource(iconPath_to);
            URL from = getClass().getClassLoader().getResource(iconPath_from);

            if(to == null || from == null ) {
                throw new IOException("Icon not found: " +iconPath_to + "or" +iconPath_from);
            }

            BufferedImage imgTo = ImageIO.read(to);
            BufferedImage imgFrom = ImageIO.read(from);
            
            int iconWidth = 40;
            int iconHeight = 40;

            Image scaledTo = imgTo.getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH);
            Image scaledFrom = imgFrom.getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH);
            
            Font font = new Font("SansSerif",Font.PLAIN,14);
            FontMetrics fm = button.getFontMetrics(font);
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int width = iconWidth + 8 + textWidth + 8 + iconWidth;
            int height = Math.max(iconHeight,textHeight);
           //Using g2 instead of g (parent) because it can handle anti-aliasing 
            BufferedImage combinedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = combinedImage.createGraphics();

            g2.setFont(font);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw first icon
            g2.drawImage(scaledTo, 0, (height - iconHeight) / 2, null);
        // Draw text
            int textX = iconWidth + 8;
            int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(Color.BLACK);
            g2.drawString(text, textX, textY);
        // Draw second icon
            g2.drawImage(scaledFrom, textX + textWidth + 8, (height - iconHeight) / 2, null);

            g2.dispose();

        
            button.setIcon(new ImageIcon(combinedImage));
            button.setText("");
        } catch (Exception e) {
            System.err.println("Could not load icons: " + iconPath_to + " or " + iconPath_from);
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

        



