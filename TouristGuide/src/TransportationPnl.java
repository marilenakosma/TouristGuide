import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;

public class TransportationPnl extends JPanel{

    private static final int MAP_WIDTH =400;
    private static final int MAP_HEIGHT = 300;

    private JTable schedule;
    private JList<String> route;
    private JButton metroBtn,busBtn,taxiBtn;

    public TransportationPnl() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel title = new JLabel("Available Transportation Options",SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 30));
        add(title,BorderLayout.NORTH);

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
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRoundRect(0, 0, getWidth(),getHeight(),20,20); // Placeholder for map
                g.setColor(Color.BLACK);
                g.drawString("Map Placeholder", 10, 20);
            }
        };

        mapPanel.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(mapPanel,BorderLayout.EAST);
        
        addActionListeners();
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
         
         private void addActionListeners() {
            metroBtn.addActionListener(e -> showMessage("Metro button clicked!"));
            busBtn.addActionListener(e -> showMessage("Bus button clicked!"));
            taxiBtn.addActionListener(e -> showMessage("Taxi button clicked!"));
         }

         private void showMessage(String message) {
            JOptionPane.showMessageDialog(this,message,"Information",JOptionPane.INFORMATION_MESSAGE);
         }
        
    }


