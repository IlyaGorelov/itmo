package gui.Objects.Elements.Commons;

import gui.App;

import javax.swing.*;
import java.awt.*;

public class LanguagePanel extends JPanel {
    public LanguagePanel(FlowLayout flowLayout){
        super(flowLayout);

        setOpaque(false);

        ImageIcon englishIcon = new ImageIcon(App.class.getResource("/img/en.png"));
        LanguageItem english = new LanguageItem(
                "English",
                englishIcon
                );

        JComboBox<LanguageItem> languageBox = new JComboBox<>(new LanguageItem[]{
                english
        }) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        20,
                        20
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        languageBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBorder(null);
                button.setFont(new Font("Arial", Font.BOLD, 22));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setForeground(App.TEXT_PURPLE);

                return button;
            }
        });

        languageBox.setRenderer(new LanguageRenderer());
        languageBox.setBackground(Color.WHITE);
        languageBox.setFocusable(false);
        languageBox.setPreferredSize(new Dimension(110, 45));
        languageBox.setBorder(new RoundedBorder(Color.WHITE,5,20));
        languageBox.setOpaque(false);

        add(languageBox);
    }

    private record LanguageItem(String text, ImageIcon icon) {
    }

    private static class LanguageRenderer extends JLabel implements ListCellRenderer<LanguageItem> {
        public LanguageRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
            setIconTextGap(10);
            setFont(new Font("Arial", Font.PLAIN, 16));
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends LanguageItem> list,
                LanguageItem value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
        ) {
            if (value != null) {
                setText(value.text());
                setIcon(value.icon());
            }

            if (isSelected) {
                setBackground(new Color(235, 230, 245));
                setForeground(App.TEXT_PURPLE);
            } else {
                setBackground(Color.WHITE);
                setForeground(App.TEXT_PURPLE);
            }

            if (index == -1) {
                setText("");
                ImageIcon biggerIcon = resizeIcon(value.icon(),50,40);
                setIcon(biggerIcon);
            } else {
                setText(value.text());
                ImageIcon smallerIcon = resizeIcon(value.icon(),40,25);
                setIcon(smallerIcon);
            }

            return this;
        }
    }

    public static ImageIcon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(App.class.getResource(path));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
