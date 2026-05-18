package gui.Objects.Frames;

import javax.swing.*;
import java.awt.*;

public class ConnectingFrame extends JFrame {
    public ConnectingFrame() {
        setTitle("Connecting");
        setSize(350, 160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel label = new JLabel("Connecting to server...", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(label, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        add(panel);
    }
}