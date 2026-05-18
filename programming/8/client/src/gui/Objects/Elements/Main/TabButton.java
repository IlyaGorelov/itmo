package gui.Objects.Elements.Main;

import gui.App;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TabButton extends JButton {
    public TabButton(String text, boolean active) {
        super(text);

        setFont(new Font("Arial", Font.PLAIN, 32));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            setBackground(App.TEXT_PURPLE);
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
            setForeground(App.TEXT_GRAY);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(getBackground());

        g2.fill(new RoundRectangle2D.Double(
                0,
                0,
                getWidth(),
                getHeight(),
                25,
                25
        ));

        g2.dispose();

        super.paintComponent(g);
    }
}