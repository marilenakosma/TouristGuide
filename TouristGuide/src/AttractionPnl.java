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
import javax.swing.event.MouseInputListener;
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
    private JXMapViewer mapViewer;
    private AttractionPainter painter;
    private List<Attraction> attractions = new ArrayList<>();

    public AttractionPnl(TravelApp app) {
        setLayout(new BorderLayout());

        mapViewer = createMapViewer();
        add(mapViewer, BorderLayout.CENTER);
        add(createBackButton(app), BorderLayout.SOUTH);
    }

    private JXMapViewer createMapViewer() {
        JXMapViewer viewer = new JXMapViewer();
        viewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        viewer.setZoom(5);
        viewer.setAddressLocation(new GeoPosition(45.658, 25.601));

        attractions.add(createAttraction("Bran Castle", 45.5156, 25.3676, "Historic castle with vampire legends", "9:00-18:00", 10.0, new String[]{"images/bran_castle.jpg"}));
        attractions.add(createAttraction("Black Church", 45.6419, 25.5882, "Gothic church in the old town", "10:00-17:00", 5.0, new String[]{"images/black_church.jpg"}));

        painter = new AttractionPainter(attractions);
        viewer.setOverlayPainter(painter);

        MouseInputListener mia = new PanMouseInputListener(viewer);
        viewer.addMouseListener(mia);
        viewer.addMouseMotionListener(mia);
        viewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(viewer));

        viewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point mousePoint = e.getPoint();
                for (Attraction attr : attractions) {
                    Point2D geoPoint = viewer.getTileFactory().geoToPixel(attr.position, viewer.getZoom());
                    Rectangle viewport = viewer.getViewportBounds();
                    int x = (int) (geoPoint.getX() - viewport.getX());
                    int y = (int) (geoPoint.getY() - viewport.getY());
                    if (mousePoint.distance(x, y) < 20) {
                        showAttractionDetails(attr);
                        break;
                    }
                }
            }
        });

        return viewer;
    }

    private Attraction createAttraction(String name, double lat, double lon, String desc, String hours, double price, String[] imagePaths) {
        List<BufferedImage> imgs = new ArrayList<>();
        for (String path : imagePaths) {
            try {
                URL url = getClass().getClassLoader().getResource(path);
                if (url != null) imgs.add(ImageIO.read(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Attraction(name, new GeoPosition(lat, lon), desc, hours, price, imgs);
    }

    private void showAttractionDetails(Attraction attr) {
        JDialog dialog = new JDialog((Frame) null, attr.name, true);
        dialog.setLayout(new BorderLayout());

        JPanel imagePanel = new SlideshowPanel(attr.images);

        JTextArea info = new JTextArea(attr.description + "\nOpen: " + attr.openingHours + "\nPrice: $" + attr.price);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        info.setEditable(false);
        info.setFont(new Font("SansSerif", Font.PLAIN, 14));
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton buyBtn = new JButton("Buy Ticket");
        buyBtn.addActionListener(e -> JOptionPane.showMessageDialog(dialog, "Ticket bought for " + attr.name));

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
        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.PLAIN, 18));
        back.addActionListener(e -> app.showPanel("Home"));
        return back;
    }

    static class Attraction {
        String name;
        GeoPosition position;
        String description;
        String openingHours;
        double price;
        List<BufferedImage> images;

        public Attraction(String name, GeoPosition pos, String desc, String hours, double price, List<BufferedImage> imgs) {
            this.name = name;
            this.position = pos;
            this.description = desc;
            this.openingHours = hours;
            this.price = price;
            this.images = imgs;
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

                if (markerIcon != null) {
                    int iw = markerIcon.getWidth();
                    int ih = markerIcon.getHeight();
                    g2.drawImage(markerIcon, x - iw / 2, y - ih / 2, null);
                } else {
                    g2.setColor(Color.RED);
                    g2.fillOval(x - 5, y - 5, 10, 10);
                }

                g2.setColor(Color.BLACK);
                g2.drawString(attr.name, x + 10, y);
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