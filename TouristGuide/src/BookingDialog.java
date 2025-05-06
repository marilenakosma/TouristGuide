import org.jdatepicker.impl.*;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.text.DefaultFormatter;

public class BookingDialog extends JDialog {

    public BookingDialog(JFrame parent, String hotelName) {
        super(parent, "Book " + hotelName, true);
        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Check-in Date:"));
        JDatePickerImpl checkInPicker = createDatePicker();
        formPanel.add(checkInPicker);

        formPanel.add(new JLabel("Check-out Date:"));
        JDatePickerImpl checkOutPicker = createDatePicker();
        formPanel.add(checkOutPicker);

        formPanel.add(new JLabel("Guests:"));
        JSpinner guestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        formPanel.add(guestsSpinner);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton bookBtn = new JButton("Book");
        bookBtn.setBackground(new Color(0, 120, 215));
        bookBtn.setForeground(Color.WHITE);
        JButton cancelButton = new JButton("Cancel");

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(bookBtn);
        add(buttonsPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());
        bookBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String checkIn = checkInPicker.getJFormattedTextField().getText();
            String checkOut = checkOutPicker.getJFormattedTextField().getText();
            int guests = (int) guestsSpinner.getValue();

            if (name.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Booking confirmed for " + hotelName + "!\n" +
                    "Name: " + name + "\n" +
                    "Check-in: " + checkIn + "\n" +
                    "Check-out: " + checkOut + "\n" +
                    "Guests: " + guests, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        return new JDatePickerImpl(datePanel, new DateLabelFormatter());
    }

    public class DateLabelFormatter extends DefaultFormatter {
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    
        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parse(text);
        }
    
        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}