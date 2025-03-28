import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonStyles extends JButton{
public ButtonStyles(String text) {
    super(text);
    setFont(new Font("Arial",Font.BOLD,18));
    setBackground(new Color(50,150,250));
    setForeground(Color.WHITE);
    setFocusPainted(false);
    setBorder(BorderFactory.createLineBorder(new Color(40,120,220),2));
    Dimension size =new Dimension(300,60);
    setMaximumSize(size);  // Force maximum size

    addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            setBackground(new Color(30, 120, 220)); // Darker blue
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setBackground(new Color(50, 150, 250)); // Original blue
        }
    });
}

}
