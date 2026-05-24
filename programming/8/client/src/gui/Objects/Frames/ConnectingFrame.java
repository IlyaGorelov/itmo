package gui.Objects.Frames;

import gui.Objects.Elements.Localized;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;

public class ConnectingFrame extends JFrame implements Localized {
    JLabel label;

    public ConnectingFrame() {
        setTitle(I18n.get("connecting.title"));
        setSize(350, 160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        label = new JLabel(I18n.get("connecting.label"), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(label, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        add(panel);
    }

    @Override
    public void updateTexts() {
        setTitle(I18n.get("connecting.title"));
        label.setText(I18n.get("connecting.label"));
    }
}