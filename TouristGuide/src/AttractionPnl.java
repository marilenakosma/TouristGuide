import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class AttractionPnl extends JPanel {

    private static final int MAP_WIDTH = 700;
    private static final int MAP_HEIGHT = 800;
    private JXMapViewer mapViewer;
    private AttractionPainter painter;
    private List<Attraction> attractions = new ArrayList<>();

    public AttractionPnl(TravelApp app) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backBtn = createBackButton(app);
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Popular Attraction Destinations", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30));
        topPanel.add(backBtn, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(0, 20)); 
        spacer.setOpaque(false);
        wrapperPanel.add(spacer, BorderLayout.SOUTH);

        add(wrapperPanel, BorderLayout.NORTH);

        mapViewer = createMapViewer();
        mapViewer.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        add(mapViewer, BorderLayout.CENTER);
    }

    private JXMapViewer createMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(45.642, 25.58));

        attractions.add(createAttraction("Bran Castle", 45.5156, 25.3676, "Historic castle with vampire legends", "9:00-18:00", 10.0, new String[]{"images/bran_1.jpg","images/bran_2.jpg","images/bran_3.jpeg"},"images/bran_castle_marker.png"));
        attractions.add(createAttraction("Black Church", 45.6419, 25.5882, "Gothic church in the old town", "10:00-17:00", 5.0, new String[]{"images/church_1.jpg","images/church_2.jpg","images/church_3.jpg"},"images/black_church_marker.png"));
        attractions.add(createAttraction("White Tower", 45.642833, 25.586466, "An iconic watchtower offering panoramic views of the city and surrounding mountains.", "10:00-17:00", 5.0, new String[]{"images/tower_1.jpg","images/tower_2.jpg","images/tower_3.jpg"},"images/white_tower_marker.png"));
        attractions.add(createAttraction("Tampa", 45.6333308, 25.583331, "A mountain, part of the and almost entirely surrounded by the city of Braşov.", "All day", 0.0, new String[]{"images/tampa_1.jpg","images/tampa_2.jpg","images/tampa_3.jpg"},"images/tampa_marker.png"));
        attractions.add(createAttraction("First Romanian School", 45.63586 , 25.58117 , "The first Romanian school, showcasing the history of education in the region.", "9.00-17.00", 0.0, new String[]{"images/first_1.jpg","images/first_2.jpg","images/first_3.jpg"},"images/first_school_marker.png"));

        painter = new AttractionPainter(attractions);
        mapViewer.setOverlayPainter(painter);

        PanMouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
        mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point mousePoint = e.getPoint();
                for (Attraction attr : attractions) {
                    Point2D geoPoint = mapViewer.getTileFactory().geoToPixel(attr.position, mapViewer.getZoom());
                    Rectangle viewport = mapViewer.getViewportBounds();
                    int x = (int) (geoPoint.getX() - viewport.getX());
                    int y = (int) (geoPoint.getY() - viewport.getY());
                    if (mousePoint.distance(x, y) < 20) {
                        showAttractionDetails(attr);
                        break;
                    }
                }
            }
        });

        return mapViewer;
    }

    private Attraction createAttraction(String name, double lat,
     double lon, String desc, String hours, double price,
     String[] imagePaths,String markerPath) {
        List<BufferedImage> imgs = new ArrayList<>();
        BufferedImage marker = null;

        try {
            URL markerUrl = getClass().getClassLoader().getResource(markerPath);
            if(markerUrl != null) {
                BufferedImage original = ImageIO.read(markerUrl);
                Image scaled = original.getScaledInstance(32,32,Image.SCALE_SMOOTH);
                marker = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = marker.createGraphics();
                g2.drawImage(scaled,0,0,null);
                g2.dispose();
            }
        } catch(IOException e) {
          e.printStackTrace();
        }

        for (String path : imagePaths) {
            try {
                URL url = getClass().getClassLoader().getResource(path);
                if (url != null) imgs.add(ImageIO.read(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Attraction(name, 
        new GeoPosition(lat, lon), desc, hours, price, imgs,marker);
    }

    private void showAttractionDetails(Attraction attr) {
        JDialog dialog = new JDialog((Frame) null, attr.name, true);
        dialog.setLayout(new BorderLayout());

        JPanel imagePanel = new SlideshowPanel(attr.images);

        JTextArea info = new JTextArea(attr.description + "\nOpen: " + attr.openingHours + "\nPrice: $" + attr.price);
        info.setWrapStyleWord(true);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        JButton buyBtn = new JButton("Buy Ticket");
        buyBtn.setFont(new Font("Segoe UI",Font.BOLD,14));
        buyBtn.setBackground(new Color(46,204,113));
        buyBtn.setForeground(Color.WHITE);
        buyBtn.setFocusPainted(false);
        buyBtn.setBorderPainted(false);
        buyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Ticket purchased successfully for " + attr.name,
                    "Purchase Confirmation",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose(); });

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(info, BorderLayout.CENTER);
        southPanel.add(buyBtn, BorderLayout.SOUTH);

        dialog.add(imagePanel, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);

        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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

    static class Attraction {
        String name;
        GeoPosition position;
        String description;
        String openingHours;
        double price;
        List<BufferedImage> images;
        BufferedImage markerIcon;

        public Attraction(String name, GeoPosition pos, String desc, String hours,
         double price, List<BufferedImage> imgs,BufferedImage markerIcon) {
            this.name = name;
            this.position = pos;
            this.description = desc;
            this.openingHours = hours;
            this.price = price;
            this.images = imgs;
            this.markerIcon = markerIcon;
        }
    }

    static class AttractionPainter implements Painter<JXMapViewer> {
        private List<Attraction> attractions;
        private BufferedImage markerIcon;

        public AttractionPainter(List<Attraction> attractions) {
            this.attractions = attractions;
            try {
                URL url = getClass().getClassLoader().getResource("images/marker-icon.png");
                if (url != null) {
                    BufferedImage original = ImageIO.read(url);
                    Image scaled = original.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                    markerIcon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = markerIcon.createGraphics();
                    g2.drawImage(scaled, 0, 0, null);
                    g2.dispose();
                } else {
                    System.err.println("Marker icon not found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }

        public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            Rectangle viewport = map.getViewportBounds();

            for (Attraction attr : attractions) {
                Point2D pt = map.getTileFactory().geoToPixel(attr.position, map.getZoom());
                int x = (int) (pt.getX() - viewport.getX());
                int y = (int) (pt.getY() - viewport.getY());

                BufferedImage icon = attr.markerIcon != null ? attr.markerIcon : markerIcon;
                if (icon != null) {
                 int iw = icon.getWidth();
                 int ih = icon.getHeight();
                 g2.drawImage(icon, x - iw / 2, y - ih / 2, null);
                } else {
                  g2.setColor(Color.RED);
                  g2.fillOval(x - 5, y - 5, 10, 10);
                }

                Font labelFont = new Font("Segoe UI", Font.BOLD, 14); // Customize font type, style, and size
                g2.setFont(labelFont);
                g2.setColor(Color.BLACK);

                int textY = y + (icon != null ? icon.getHeight() / 2 + 15 : 20);
                int textX = x - g2.getFontMetrics().stringWidth(attr.name) / 2;
                g2.drawString(attr.name,textX,textY);
            }

            g2.dispose();
        }
    }

    static class SlideshowPanel extends JPanel {
        private JLabel imageLabel = new JLabel();
        private List<BufferedImage> images;
        private int currentIndex = 0;

        public SlideshowPanel(List<BufferedImage> images) {
            this.images = images;
            setLayout(new BorderLayout());
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(imageLabel, BorderLayout.CENTER);
            if (!images.isEmpty()) {
                showImage(0);
                Timer timer = new Timer(3000, e -> nextImage());
                timer.start();
            }
        }

        private void showImage(int index) {
            BufferedImage img = images.get(index);
            ImageIcon icon = new ImageIcon(img.getScaledInstance(400, 250, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        }

        private void nextImage() {
            currentIndex = (currentIndex + 1) % images.size();
            showImage(currentIndex);
        }
    }
} 