package gui.Objects.Elements.Main;

import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.Login;
import core.Objects.CommandsControllers.Commands.Logout;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import gui.App;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Commons.RoundedPanel;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Path2D;

public class UserPanel extends JPanel {
    public UserPanel() {
        setOpaque(false);

        RoundedPanel panel = new RoundedPanel(25, Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 70));

        JPanel userPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        userPart.setOpaque(false);
        userPart.setBorder(new EmptyBorder(0, 10, 0, 0));

        JLabel avatar = new JLabel(new AvatarIcon(50, App.TEXT_GRAY));
        JLabel username = new JLabel(AuthManager.getInstance().getUser().getLogin());
        username.setFont(new Font("Arial", Font.PLAIN, 22));
        username.setForeground(App.TEXT_PURPLE);

        userPart.add(avatar);
        userPart.add(username);

        JButton logoutButton = new RoundedButton("Logout",
                new ExitIcon(50, Color.BLACK),
                0, 25, 25, 0,
                App.LIGHT_GRAY);
        logoutButton.setPreferredSize(new Dimension(130, 70));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 22));
        logoutButton.setForeground(App.TEXT_PURPLE);
        logoutButton.setIconTextGap(2);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        logoutButton.addActionListener(e -> {
            Client.putCommand(new CustomPackage(new Logout().getName(), null, null));
        });

        panel.add(userPart, BorderLayout.CENTER);
        panel.add(logoutButton, BorderLayout.EAST);

        add(panel);
    }

    private record AvatarIcon(int size, Color color) implements Icon {
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.decode("#F2F2F2"));
            g2.fillOval(x, y, size, size);

            g2.setColor(color);

            int headSize = (int) (size * 0.35);
            int headX = x + (size - headSize) / 2;
            int headY = y + (int) (size * 0.18);

            g2.fillOval(headX, headY, headSize, headSize);

            int bodyWidth = (int) (size * 0.62);
            int bodyHeight = (int) (size * 0.42);
            int bodyX = x + (size - bodyWidth) / 2;
            int bodyY = y + (int) (size * 0.55);

            g2.fillOval(bodyX, bodyY, bodyWidth, bodyHeight);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static class ExitIcon implements Icon {
        private final int width;
        private final int height;
        private final Color color;

        public ExitIcon(int width, int height, Color color) {
            this.width = width;
            this.height = height;
            this.color = color;
        }

        public ExitIcon(int size, Color color) {
            this(size, size, color);
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double baseW = 116.0;
            double baseH = 146.0;

            double scale = Math.min(width / baseW, height / baseH);

            double offsetX = x + (width - baseW * scale) / 2.0;
            double offsetY = y + (height - baseH * scale) / 2.0;

            g2.translate(offsetX, offsetY);
            g2.scale(scale, scale);

            g2.setColor(color);
            g2.setStroke(new BasicStroke(
                    9f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            Path2D bracket = new Path2D.Double();
            bracket.moveTo(81, 18);
            bracket.lineTo(36, 18);
            bracket.quadTo(24, 18, 24, 30);
            bracket.lineTo(24, 100);
            bracket.quadTo(24, 112, 36, 112);
            bracket.lineTo(81, 112);

            g2.draw(bracket);

            g2.drawLine(54, 65, 103, 65);

            Path2D arrowHead = new Path2D.Double();
            arrowHead.moveTo(80, 41);
            arrowHead.lineTo(104, 65);
            arrowHead.lineTo(80, 89);

            g2.draw(arrowHead);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }
    }


}