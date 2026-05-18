package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ContentPanel extends JPanel {
    public ContentPanel() {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 35, 35, 35));

        JPanel titleAndButtons = new JPanel(new BorderLayout());
        titleAndButtons.setOpaque(false);
        titleAndButtons.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("Products");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.PLAIN, 42));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        buttonsPanel.setOpaque(false);

        JButton undo = createPillButton("Undo", 100);
        JButton redo = createPillButton("Redo", 100);

        JButton update = createPillButton("Update", 140);
        JButton remove = createPillButton("Remove", 140);
        JButton executeScript = createPillButton("Execute Script", 220);

        JButton show = createDropDownButton(
                "Show",
                150,
                "filter_by_name",
                "filter_contains_name",
                "filter_greater_than_owner"
        );

        JButton clear = createDropDownButton(
                "Clear",
                150,
                "clear",
                "remove_greater"
        );

        JButton add = createDropDownButton(
                "Add",
                140,
                "add",
                "add_if_min",
                "add_if_max"
        );

        undo.addActionListener(e -> {
            System.out.println("Undo clicked");
        });

        redo.addActionListener(e -> {
            System.out.println("Redo clicked");
        });

        buttonsPanel.add(undo);
        buttonsPanel.add(redo);
        buttonsPanel.add(Box.createHorizontalStrut(170));
        buttonsPanel.add(executeScript);
        buttonsPanel.add(update);
        buttonsPanel.add(remove);
        buttonsPanel.add(show);
        buttonsPanel.add(clear);
        buttonsPanel.add(add);

        titleAndButtons.add(title, BorderLayout.WEST);
        titleAndButtons.add(buttonsPanel, BorderLayout.CENTER);

        add(titleAndButtons, BorderLayout.NORTH);
        add(new TablePanel(), BorderLayout.CENTER);

    }

    private JButton createPillButton(String text, int width) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                if (getModel().isRollover()) {
                    g2.setColor(Color.decode("#E8E8E8"));
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(width, 55));
        button.setFont(new Font("Arial", Font.PLAIN, 28));
        button.setForeground(App.TEXT_PURPLE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JButton createDropDownButton(String text, int width, String... variants) {
        JButton button = createPillButton(text + " ▼", width);

        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.setBackground(Color.WHITE);
        popupMenu.setBorder(BorderFactory.createLineBorder(App.TEXT_PURPLE, 2));

        for (String variant : variants) {
            JMenuItem item = new JMenuItem(variant);

            item.setFont(new Font("Arial", Font.PLAIN, 22));
            item.setForeground(App.TEXT_PURPLE);
            item.setBackground(Color.WHITE);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            item.addActionListener(e -> {
                System.out.println("Selected command: " + variant);

                // здесь потом вызывай свою команду
                // например:
                // GUICommand = new CustomPackage(variant, null, null);
            });

            popupMenu.add(item);
        }

        button.addActionListener(e -> {
            popupMenu.show(button, 0, button.getHeight());
        });

        return button;
    }

}
