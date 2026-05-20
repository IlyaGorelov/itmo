package gui.Objects.Elements.Main.TableTab.Dialogs;

import gui.App;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class OwnerFilterDialog extends JDialog {

    private JTextField nameField;
    private JTextField heightField;
    private JPanel heightPanel;
    private JLabel hintLabel;
    private JLabel errorLabel;

    private boolean confirmed = false;

    public OwnerFilterDialog() {
        super(null, "Filter by owner", ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(620, 330));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getOwnerName() {
        String text = nameField.getText().trim();

        if (text.isEmpty() || "null".equalsIgnoreCase(text)) {
            return null;
        }

        return text;
    }

    public String getHeightValue() {
        if (getOwnerName() == null) {
            return null;
        }

        return heightField.getText().trim();
    }

    private JComponent createContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        int row = 0;


        nameField = createTextField();
        heightField = createTextField();

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        content.add(createFieldBlock("Owner name", nameField), gbc);

        hintLabel = new JLabel("If owner name is empty or null, height will not be used.");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        hintLabel.setForeground(App.TEXT_GRAY);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(hintLabel, gbc);

        heightPanel = createFieldBlock("Height", heightField);
        heightPanel.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        content.add(heightPanel, gbc);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        errorLabel.setForeground(Color.RED);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 18, 0);
        content.add(errorLabel, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);

        JButton cancelButton = createSecondaryButton("Cancel");
        JButton applyButton = createPrimaryButton("Apply filter");

        cancelButton.addActionListener(e -> dispose());
        applyButton.addActionListener(this::applyFilter);

        buttons.add(cancelButton);
        buttons.add(applyButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        content.add(buttons, gbc);

        getRootPane().setDefaultButton(applyButton);

        setupDynamicVisibility();

        return content;
    }

    private void setupDynamicVisibility() {
        nameField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                String name = nameField.getText().trim();

                boolean hasName = !name.isEmpty() && !"null".equalsIgnoreCase(name);

                heightPanel.setVisible(hasName);

                if (!hasName) {
                    heightField.setText("");
                    errorLabel.setText(" ");
                }

                pack();
                setMinimumSize(new Dimension(620, 330));
                setLocationRelativeTo(getOwner());
            }
        });
    }

    private void applyFilter(ActionEvent e) {
        String name = nameField.getText().trim();

        boolean hasName = !name.isEmpty() && !"null".equalsIgnoreCase(name);

        if (!hasName) {
            confirmed = true;
            dispose();
            return;
        }

        String height = heightField.getText().trim();

        if (height.isEmpty()) {
            errorLabel.setText("Height is required when owner name is specified.");
            return;
        }

        try {
            float parsedHeight = Float.parseFloat(height);

            if (parsedHeight <= 0) {
                errorLabel.setText("Height must be greater than 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            errorLabel.setText("Height must be a number.");
            return;
        }

        confirmed = true;
        dispose();
    }

    private JPanel createFieldBlock(String labelText, JComponent input) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(App.TEXT_PURPLE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(input);

        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();

        field.setFont(new Font("Arial", Font.PLAIN, 20));
        field.setForeground(App.TEXT_PURPLE);
        field.setCaretColor(App.TEXT_PURPLE);
        field.setBackground(Color.WHITE);
        field.setBorder(new RoundedLineBorder(App.TEXT_PURPLE, 18, 2));
        field.setMargin(new Insets(8, 14, 8, 14));
        field.setPreferredSize(new Dimension(420, 44));

        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new RoundedButton(
                text,
                App.TEXT_PURPLE,
                Color.WHITE,
                null,
                18
        );

        button.setPreferredSize(new Dimension(170, 52));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new RoundedButton(
                text,
                Color.WHITE,
                App.TEXT_PURPLE,
                App.TEXT_PURPLE,
                18
        );

        button.setPreferredSize(new Dimension(130, 52));
        return button;
    }

    private static class RoundedLineBorder extends AbstractBorder {
        private final Color color;
        private final int arc;
        private final int thickness;

        public RoundedLineBorder(Color color, int arc, int thickness) {
            this.color = color;
            this.arc = arc;
            this.thickness = thickness;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 12;
            insets.right = 12;
            insets.top = 8;
            insets.bottom = 8;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));

            g2.draw(new RoundRectangle2D.Double(
                    x + thickness / 2.0,
                    y + thickness / 2.0,
                    width - thickness,
                    height - thickness,
                    arc,
                    arc
            ));

            g2.dispose();
        }
    }

    private static class RoundedButton extends JButton {
        private final Color bg;
        private final Color fg;
        private final Color borderColor;
        private final int arc;

        public RoundedButton(String text, Color bg, Color fg, Color borderColor, int arc) {
            super(text);

            this.bg = bg;
            this.fg = fg;
            this.borderColor = borderColor;
            this.arc = arc;

            setForeground(fg);
            setFont(new Font("Arial", Font.BOLD, 18));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color currentBg = bg;

            if (getModel().isPressed()) {
                currentBg = bg.darker();
            } else if (getModel().isRollover()) {
                currentBg = bg.brighter();
            }

            g2.setColor(currentBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, arc, arc);
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {
        public abstract void update();

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }
    }
}