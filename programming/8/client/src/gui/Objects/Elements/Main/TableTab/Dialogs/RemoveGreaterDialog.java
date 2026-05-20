package gui.Objects.Elements.Main.TableTab.Dialogs;

import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RemoveGreaterDialog extends JDialog {

    private JTextField priceField;
    private JTextField manufactureCostField;
    private JLabel errorLabel;
    private boolean confirmed = false;

    public RemoveGreaterDialog() {
        super(null, "Remove greater", ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(640, 330));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getPriceValue() {
        String price = priceField.getText().trim();

        if (price.isEmpty() || "null".equalsIgnoreCase(price)) {
            return null;
        }

        return price;
    }

    public String getManufactureCostValue() {
        return manufactureCostField.getText().trim();
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
        priceField = createTextField();
        manufactureCostField = createTextField();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 12, 12);
        content.add(createFieldBlock("Price", priceField), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 12, 12, 0);
        content.add(createFieldBlock("Manufacture Cost", manufactureCostField), gbc);

        row++;

        JLabel hintLabel = new JLabel("Price may be empty or null. Manufacture Cost is required.");
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 15));
        hintLabel.setForeground(App.TEXT_GRAY);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        content.add(hintLabel, gbc);

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
        JButton removeButton = createRemoveButton("Remove");

        cancelButton.addActionListener(e -> dispose());
        removeButton.addActionListener(this::removeGreater);

        buttons.add(cancelButton);
        buttons.add(removeButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        content.add(buttons, gbc);

        getRootPane().setDefaultButton(removeButton);

        return content;
    }

    private void removeGreater(ActionEvent e) {
        String price = priceField.getText().trim();
        String manufactureCost = manufactureCostField.getText().trim();

        if (!price.isEmpty() && !"null".equalsIgnoreCase(price)) {
            try {
                double parsedPrice = Double.parseDouble(price);

                if (parsedPrice <= 0) {
                    errorLabel.setText("Price must be greater than 0.");
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Price must be a number or null.");
                return;
            }
        }

        if (manufactureCost.isEmpty()) {
            errorLabel.setText("Manufacture Cost is required.");
            return;
        }

        try {
            Integer.parseInt(manufactureCost);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Manufacture Cost must be an integer.");
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
        field.setBorder(new RoundedBorder(App.TEXT_PURPLE, 2, 18));
        field.setMargin(new Insets(8, 14, 8, 14));
        field.setPreferredSize(new Dimension(260, 44));

        return field;
    }

    private JButton createRemoveButton(String text) {
        JButton button = new RoundedButton(
                text,
                18,
                Color.RED,
                Color.WHITE,
                null
        );

        button.setPreferredSize(new Dimension(130, 52));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new RoundedButton(
                text,
                18,
                Color.WHITE,
                App.TEXT_PURPLE,
                App.TEXT_PURPLE
        );

        button.setPreferredSize(new Dimension(130, 52));
        return button;
    }


}