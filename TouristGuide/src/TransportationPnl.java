import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TransportationPnl extends JPanel{

    private static final int MAP_WIDTH =400;
    private static final int MAP_HEIGHT = 300;

    private JTable schedule;
    private JList<String> route;
    private JButton metroBtn,busBtn,taxiBtn;

    public TransportationPnl() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Available Transportation Options",SwingConstants.CENTER);
        //title.setFont(new Font(""))
        add(title,BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(3,1,5,5));
        

        metroBtn = createButton("Metro");
        busBtn = createButton("Bus");
        taxiBtn = createButton("Taxi");

        btnPanel.add(metroBtn);
        btnPanel.add(busBtn);
        btnPanel.add(taxiBtn);

        add(btnPanel,BorderLayout.WEST);

        JPanel routesPanel = new JPanel(new BorderLayout());

        DefaultListModel<String> routeListModel = new DefaultListModel<>();
        routeListModel.addElement("Route 1: City Center - Airport");
        routeListModel.addElement("Route 2: City Center - Train Station");
        routeListModel.addElement("Route 3: City Center - Museum");

        route = new JList<>(routeListModel);
        route.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane routeScrollPane = new JScrollPane(route);
        routesPanel.add(routeScrollPane,BorderLayout.CENTER);
        add(routesPanel,BorderLayout.CENTER);
        // Optional: Map image (can be placed in the center or a different section)
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0, 0, getWidth(),getHeight()); // Placeholder for map
                g.setColor(Color.BLACK);
                g.drawString("Map Placeholder", 10, 20);
            }
        };

        mapPanel.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        add(mapPanel,BorderLayout.EAST);
        
        addActionListeners();
    }  
        private JButton createButton(String text)  {
            JButton button = new JButton(text);
            button.putClientProperty("JButton.buttonType", "roundRect");
            button.putClientProperty("JComponent.roundRect", true);
            button.setMaximumSize(new Dimension(200, 60));
            return button;
        }
         
         private void addActionListeners() {
            metroBtn.addActionListener(e -> showMessage("Metro button clicked!"));
            metroBtn.addActionListener(e -> showMessage("Metro button clicked!"));
            metroBtn.addActionListener(e -> showMessage("Metro button clicked!"));
         }

         private void showMessage(String message) {
            JOptionPane.showMessageDialog(this,message,"Information",JOptionPane.INFORMATION_MESSAGE);
         }
        
    }


