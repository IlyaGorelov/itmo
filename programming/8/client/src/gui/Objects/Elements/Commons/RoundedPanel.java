package gui.Objects.Elements.Commons;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class RoundedPanel extends JPanel {
    private final int topLeftRadius;
    private final int topRightRadius;
    private final int bottomRightRadius;
    private final int bottomLeftRadius;
    private final Color backgroundColor;

    public RoundedPanel(
            int topLeftRadius,
            int topRightRadius,
            int bottomRightRadius,
            int bottomLeftRadius,
            Color backgroundColor
    ) {
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomRightRadius = bottomRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.backgroundColor = backgroundColor;

        setOpaque(false);
    }

    public RoundedPanel(int radius, Color backgroundColor) {
        this(radius, radius, radius, radius, backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        int tl = Math.min(topLeftRadius, Math.min(width, height) / 2);
        int tr = Math.min(topRightRadius, Math.min(width, height) / 2);
        int br = Math.min(bottomRightRadius, Math.min(width, height) / 2);
        int bl = Math.min(bottomLeftRadius, Math.min(width, height) / 2);

        Path2D path = new Path2D.Double();

        path.moveTo(tl, 0);

        path.lineTo(width - tr, 0);
        path.quadTo(width, 0, width, tr);

        path.lineTo(width, height - br);
        path.quadTo(width, height, width - br, height);

        path.lineTo(bl, height);
        path.quadTo(0, height, 0, height - bl);

        path.lineTo(0, tl);
        path.quadTo(0, 0, tl, 0);

        path.closePath();

        g2.setColor(backgroundColor);
        g2.fill(path);

        g2.dispose();
    }
}