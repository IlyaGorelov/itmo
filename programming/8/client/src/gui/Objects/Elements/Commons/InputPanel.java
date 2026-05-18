package gui.Objects.Elements.Commons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InputPanel extends JPanel {
    public InputPanel(JTextField textField, Icon icon){
            super(new BorderLayout());
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            setPreferredSize(new Dimension(370, 40));
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            setBackground(Color.WHITE);
            setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setPreferredSize(new Dimension(38, 38));
            iconLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

            textField.setBorder(new EmptyBorder(0, 10, 0, 10));
            textField.setFont(new Font("Arial", Font.PLAIN, 18));
            textField.setBackground(Color.WHITE);

            add(iconLabel, BorderLayout.WEST);
            add(textField, BorderLayout.CENTER);
    }
}
