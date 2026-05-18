package gui.Objects.Elements.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class RoundedButton extends JButton {
    private final int topLeft;
    private final int topRight;
    private final int bottomRight;
    private final int bottomLeft;
    private final Color backgroundColor;
    private final Color hoverColor;

    public RoundedButton(String text, Icon icon,
                         int topLeft, int topRight,
                         int bottomRight, int bottomLeft,
                         Color backgroundColor) {
        super(text, icon);

        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
        this.backgroundColor = backgroundColor;
        hoverColor = backgroundColor.darker();

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedButton(String text,
                         int topLeft, int topRight,
                         int bottomRight, int bottomLeft,
                         Color backgroundColor) {
        super(text);

        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
        this.backgroundColor = backgroundColor;
        hoverColor = backgroundColor.darker();

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public RoundedButton(String text,
                         int radius,
                         Color backgroundColor) {
        super(text);

        this.topLeft = radius;
        this.topRight = radius;
        this.bottomRight = radius;
        this.bottomLeft = radius;
        this.backgroundColor = backgroundColor;
        hoverColor = backgroundColor.darker();

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D path = new Path2D.Double();

        int w = getWidth();
        int h = getHeight();

        path.moveTo(topLeft, 0);

        path.lineTo(w - topRight, 0);
        path.quadTo(w, 0, w, topRight);

        path.lineTo(w, h - bottomRight);
        path.quadTo(w, h, w - bottomRight, h);

        path.lineTo(bottomLeft, h);
        path.quadTo(0, h, 0, h - bottomLeft);

        path.lineTo(0, topLeft);
        path.quadTo(0, 0, topLeft, 0);

        if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(backgroundColor);
        }

        g2.fill(path);

        g2.dispose();

        super.paintComponent(g);
    }
}