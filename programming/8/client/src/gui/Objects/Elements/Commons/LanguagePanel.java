package gui.Objects.Elements.Commons;

import gui.App;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class LanguagePanel extends JPanel {
    public LanguagePanel(FlowLayout flowLayout, Runnable updateTexts) {
        super(flowLayout);

        setOpaque(false);

        LanguageItem russian = createLanguageItem(
                "Русский",
                "/img/ru.png",
                new Locale("ru", "RU")
        );

        LanguageItem portuguese = createLanguageItem(
                "Português",
                "/img/pt.png",
                new Locale("pt")
        );

        LanguageItem french = createLanguageItem(
                "Français",
                "/img/fr.png",
                Locale.FRENCH
        );

        LanguageItem englishZA = createLanguageItem(
                "English",
                "/img/en_ZA.png",
                new Locale("en", "ZA")
        );

        JComboBox<LanguageItem> languageBox = new JComboBox<>(new LanguageItem[]{
                russian,
                portuguese,
                french,
                englishZA,
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

        languageBox.setRenderer(new LanguageRenderer());
        languageBox.setSelectedItem(getSelectedItem(englishZA, russian, portuguese, french));

        languageBox.addActionListener(e -> {
            LanguageItem selected = (LanguageItem) languageBox.getSelectedItem();

            if (selected == null) {
                return;
            }

            I18n.setLocale(selected.locale());
            updateTexts.run();
        });

        languageBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setBorder(null);
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setForeground(App.TEXT_PURPLE);

                return button;
            }
        });

        languageBox.setBackground(Color.WHITE);
        languageBox.setFocusable(false);
        languageBox.setPreferredSize(new Dimension(110, 45));
        languageBox.setBorder(new RoundedBorder(Color.WHITE, 5, 20));
        languageBox.setOpaque(false);

        add(languageBox);
    }

    private LanguageItem getSelectedItem(LanguageItem... items) {
        Locale currentLocale = I18n.getLocale();

        for (LanguageItem item : items) {
            if (item.locale().equals(currentLocale)) {
                return item;
            }
        }

        return items[0];
    }

    private record LanguageItem(
            String text,
            ImageIcon smallIcon,
            ImageIcon bigIcon,
            Locale locale
    ) {
    }

    private LanguageItem createLanguageItem(
            String text,
            String iconPath,
            Locale locale
    ) {
        ImageIcon original = new ImageIcon(App.class.getResource(iconPath));

        return new LanguageItem(
                text,
                resizeIcon(original, 40, 25),
                resizeIcon(original, 50, 40),
                locale
        );
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
            if (value == null) {
                setText("");
                setIcon(null);
                return this;
            }

            if (index == -1) {
                setText("");
                setIcon(value.bigIcon());
            } else {
                setText(value.text());
                setIcon(value.smallIcon());
            }

            if (isSelected) {
                setBackground(new Color(235, 230, 245));
            } else {
                setBackground(Color.WHITE);
            }

            setForeground(App.TEXT_PURPLE);

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
