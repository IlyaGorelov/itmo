package gui.Objects.Elements.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedClipPanel extends JPanel {
    private final int radius;
    private final Color backgroundColor;

    public RoundedClipPanel(int radius, Color backgroundColor) {
        this.radius = radius;
        this.backgroundColor = backgroundColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape round = new RoundRectangle2D.Double(
                0,
                0,
                getWidth(),
                getHeight(),
                radius,
                radius
        );

        g2.setColor(backgroundColor);
        g2.fill(round);

        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Shape round = new RoundRectangle2D.Double(
                0,
                0,
                getWidth(),
                getHeight(),
                radius,
                radius
        );

        g2.setClip(round);

        super.paintChildren(g2);

        g2.dispose();
    }
}