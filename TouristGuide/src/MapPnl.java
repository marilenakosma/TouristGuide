
/*import javax.swing.*;
import java.awt.*;

    import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.awt.geom.Rectangle2D;

public class MapPnl extends JPanel{

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Map Painter Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new MapPnl().createMapViewer());
            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }

    public JXMapViewer createMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(new DefaultTileFactory(new OSMTileFactoryInfo()));
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(new GeoPosition(45.658, 25.601));

        // Define labeled positions
        Map<GeoPosition, String> labeledPoints = new HashMap<>();
        GeoPosition airport = new GeoPosition(45.7, 25.6);
        GeoPosition train = new GeoPosition(45.66, 25.6);
        GeoPosition metro = new GeoPosition(45.65, 25.58);
        GeoPosition museum = new GeoPosition(45.67, 25.62);

        labeledPoints.put(airport, "Airport");
        labeledPoints.put(train, "Train Station");
        labeledPoints.put(metro, "Metro Station");
        labeledPoints.put(museum, "Museum");

        // Define route path (line)
        List<GeoPosition> routePoints = Arrays.asList(
            airport, train, museum
        );

        mapViewer.setOverlayPainter(new MapOverlayPainter(routePoints, labeledPoints));

        // Optional: Add click detection
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

    // 👇 Custom painter class for both route and labels
    static class MapOverlayPainter implements Painter<JXMapViewer> {
        private final List<GeoPosition> routePoints;
        private final Map<GeoPosition, String> labeledPoints;

        public MapOverlayPainter(List<GeoPosition> routePoints, Map<GeoPosition, String> labeledPoints) {
            this.routePoints = routePoints;
            this.labeledPoints = labeledPoints;
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            Rectangle viewport = map.getViewportBounds();

            // Draw route lines
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

            // Draw labeled waypoints
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
}

*/