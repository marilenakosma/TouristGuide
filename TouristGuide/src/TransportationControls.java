import javax.swing.*;
import java.awt.*;

public class TransportationControls extends JPanel{
private JComboBox<String> fromComboBox;
private JComboBox<String> toComboBox;
private JRadioButton busBtn,trainBtn,planeBtn;
private JButton searchBtn;

public TransportationControls() {
    setLayout(new GridBagLayout());
    setOpaque(false);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5,10,5,10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    fromComboBox = new JComboBox<>(new String[]{"Brasov","Bucharest","Cluj","Sibiu"});
    gbc.gridx = 0;
    gbc.gridy = 0;
    add(new JLabel("From:"),gbc);
    gbc.gridy = 1;
    add(fromComboBox,gbc);

    toComboBox = new JComboBox<>(new String[]{"Brasov","Bucharest","Cluj","Sibiu"});
    gbc.gridx = 1;
    gbc.gridy = 0;
    add(new JLabel("To:"),gbc);
    gbc.gridy = 1;
    add(toComboBox,gbc);

    JPanel transportationModePnl = new JPanel(new FlowLayout());
    busBtn = new JRadioButton("Bus");
    trainBtn = new JRadioButton("Train");
    planeBtn = new JRadioButton("Plane");

    ButtonGroup modeGroup = new ButtonGroup();
    modeGroup.add(busBtn);
    modeGroup.add(trainBtn);
    modeGroup.add(planeBtn);

    transportationModePnl.add(busBtn);
    transportationModePnl.add(trainBtn);
    transportationModePnl.add(planeBtn);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    add(transportationModePnl,gbc);

    searchBtn = new JButton("Plan Trip");
    searchBtn.setBackground(new Color(0, 120, 215));
    searchBtn.setForeground(Color.WHITE);
    gbc.gridy = 3;
    add(searchBtn,gbc);
}

public JComboBox<String> getFromComboBox() {
    return fromComboBox;
}

public JComboBox<String> getToComboBox() {
    return toComboBox;
}

public String getSelectedMode() {
    if(busBtn.isSelected()) return "Bus";
    if(trainBtn.isSelected()) return "Train";
    if(planeBtn.isSelected()) return "Plane";
    return "";
}

public JButton getSearchButton() {
    return searchBtn;
}

}
