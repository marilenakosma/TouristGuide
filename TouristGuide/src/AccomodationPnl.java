import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class AccomodationPnl extends JPanel {

    public AccomodationPnl(TravelApp app) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 250)); // soft background color
        

        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backBtn = createBackButton(app);
        JLabel title = new JLabel("Available Accommodations", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 36f));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        
        topPanel.add(backBtn,BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);

        add(topPanel,BorderLayout.NORTH);

        JPanel hotelsPanel = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 per row
        hotelsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        hotelsPanel.setBackground(new Color(245, 245, 250)); // match background

        // Add hotel cards
        hotelsPanel.add(createHotelCard("Grand Palace Hotel", "Images/hotel1.png", "Luxury stay in the heart of the city."));
        hotelsPanel.add(createHotelCard("Ocean View Resort", "Images/hostel.jpg", "Relax by the beach with premium service."));
        hotelsPanel.add(createHotelCard("Mountain Escape Lodge", "Images/hotel2.jpg", "Cozy mountain views and fresh air."));
        hotelsPanel.add(createHotelCard("City Budget Inn", "Images/hostel.jpg", "Affordable and close to transport."));

        JScrollPane scrollPane = new JScrollPane(hotelsPanel);
        scrollPane.setBorder(null); // clean look
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // smoother scroll

        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createBackButton(TravelApp app) {
        JButton backBtn = new JButton("Back");
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setBackground(new Color(173,216,230));
        backBtn.setMaximumSize(new Dimension(50,50));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.setFont(backBtn.getFont().deriveFont(Font.PLAIN, 22));
        backBtn.addActionListener(e-> app.showPanel("Home"));
        return backBtn;
    }

    private JPanel createHotelCard(String name, String imagePath, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(400, 300));

        // Load image
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(imageUrl));
                Image scaled = icon.getImage().getScaledInstance(350, 150, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(imgLabel);
            } else {
                card.add(new JLabel("Image not found"));
            }
        } catch (IOException e) {
            card.add(new JLabel("Error loading image"));
        }

        // Hotel name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 20f));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        card.add(nameLabel);

        // Description
        JTextArea desc = new JTextArea(description);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        desc.setEditable(false);
        desc.setFont(desc.getFont().deriveFont(14f));
        desc.setBackground(Color.WHITE);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(desc);

        // Book Now button
        JButton bookBtn = new JButton("Book Now");
        bookBtn.putClientProperty("JButton.buttonType", "roundRect");
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setBackground(new Color(0, 120, 215));
        bookBtn.setForeground(Color.WHITE);
        card.add(Box.createVerticalStrut(10));
        card.add(bookBtn);
        
        bookBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(card, "Booking for " + name + " is not yet implemented.", "Booking", JOptionPane.INFORMATION_MESSAGE);
        });
        
        return card;
    }
}

