package gui.Objects.Elements.Main;

import Commons.Enums.EyeColor;
import Commons.Enums.HairColor;
import Localization.EnumI18n;
import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class CustomComboBoxForHair extends JComboBox<HairColor> {
    public CustomComboBoxForHair(HairColor[] items) {
        super(items);

        setFont(new Font("Arial", Font.PLAIN, 20));
        setForeground(App.BACKGROUND);
        setBackground(Color.WHITE);
        setBorder(new RoundedBorder(App.BACKGROUND, 2, 18));
        setPreferredSize(new Dimension(240, 44));
        setUI(new CustomComboBoxForHair.PurpleComboBoxUI());

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel c = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                c.setFont(new Font("Arial", Font.PLAIN, 18));
                c.setBorder(new EmptyBorder(4, 10, 4, 10));

                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(App.BACKGROUND);
                } else {
                    c.setBackground(App.BACKGROUND);
                    c.setForeground(Color.WHITE);
                }

                if (value == null) {
                    c.setText(" ");
                } else {
                    c.setText(EnumI18n.hairColor((HairColor) value));
                }

                return c;
            }
        });
    }

    private static class PurpleComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton("▼");
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setForeground(App.TEXT_PURPLE);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return button;
        }
    }
}