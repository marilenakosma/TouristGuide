import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class AttractionPnl extends JPanel{
    private final String[] attractionNames = {
        "Bran Castle","Council Square","First Romanian School",
        "Black Church","White Tower","Rope Street"
    };

    private final String[] descriptions = {
        "A historic castle located in Bran, Romania, often referred to as Dracula's Castle.",
        "The main square of Brașov, surrounded by beautiful architecture and vibrant cafes.",
        "The first Romanian school, showcasing the history of education in the region.",
        "A famous Gothic-style monument and one of the most important landmarks in Brașov.",
        "An iconic watchtower offering panoramic views of the city and surrounding mountains.",
        "One of the narrowest streets in Europe, known for its charming atmosphere."
    };
    private final String[] images = {
        "images/bran_castle.jpg","images/council_square.jpg",
        "images/first_school.jpg","images/black_church.jpg",
        "images/white_tower.jpg","images/rope_street.jpg"
    };
public AttractionPnl() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Explore Local Attractions",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 45));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        add(title,BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2,3,20,20   ));
        grid.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        //grid.setBackground(Color.decode(#f5f5f5));

        for(int i=0;i< 6; i++) {
            JPanel card = createAttractionCard(i);
            grid.add(card);
        }

        add(new JScrollPane(grid),BorderLayout.CENTER);
    }
    private JPanel createAttractionCard(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(200 + index * 7,200 - index * 5,220 - index * 4));
        //panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(5, 5, 5, 5),
        BorderFactory.createLineBorder(new Color(220, 220, 220), 1)
));
        panel.setPreferredSize(new Dimension(200,200));
    
        JLabel name = new JLabel(attractionNames[index],SwingConstants.CENTER);
        name.setFont(new Font("SansSerif",Font.BOLD,20));
        name.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JButton button = new JButton();
        try {
        ImageIcon image= new ImageIcon(getClass().getClassLoader().getResource(images[index]));
        Image scaledImage = image.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.putClientProperty("FlatLaf.styleClass", "rounded");
        }
        catch(Exception ex) {
            System.err.println("Could not load icon: " + images[index]);
        }

        JPanel overlay = new JPanel();
        overlay.setLayout(new BorderLayout());
        overlay.setBackground(new Color(0, 0, 0, 130)); // less harsh


        JLabel description = new JLabel("<html><center>"  +descriptions[index] + "</center></html>", SwingConstants.CENTER);
        description.setForeground(Color.WHITE);
        description.setFont(new Font("SansSerif",Font.PLAIN,16));
        overlay.add(description,BorderLayout.CENTER);
        overlay.setVisible(false);
        panel.setLayout(new OverlayLayout(panel));

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(button,BorderLayout.CENTER);
        content.add(name,BorderLayout.SOUTH);
        
        panel.add(overlay);
        panel.add(content);

        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                overlay.setVisible(true);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                overlay.setVisible(false);
                panel.setCursor(Cursor.getDefaultCursor());
            }

            public void mouseClicked(MouseEvent e) {
                showAttractionDetails(index);
            }
        });
        return panel;
    }

    private void showAttractionDetails(int index) {
        JDialog detailDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Attraction Details", true);
        detailDialog.setSize(400,300);
        detailDialog.setLocationRelativeTo(this);

        JLabel name = new JLabel(attractionNames[index],SwingConstants.CENTER);
        name.setFont(name.getFont().deriveFont(Font.PLAIN, 30));
        name.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JTextArea details = new JTextArea();
        details.setText(descriptions[index] +"\n\nOpening Hours:9-18");

        details.setEditable(false);
        details.setWrapStyleWord(true);
        details.setLineWrap(true);
        details.setMargin(new Insets(10,10,10,10));
        details.setBackground(UIManager.getColor("Panel.background"));
        
        JButton mapBtn = new JButton("View on Map");
        mapBtn.addActionListener(e -> {
            detailDialog.dispose();
            //JOptionPane.showMessageDialog(this, "Show attraction " + attractionNames[index] + " on the map.");
        });

        detailDialog.add(name,BorderLayout.NORTH);
        detailDialog.add(new JScrollPane(details), BorderLayout.CENTER);
        detailDialog.add(mapBtn, BorderLayout.SOUTH);

        detailDialog.setVisible(true);
    }
}
