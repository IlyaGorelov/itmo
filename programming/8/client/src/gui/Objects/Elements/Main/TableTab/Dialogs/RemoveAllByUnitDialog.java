package gui.Objects.Elements.Main.TableTab.Dialogs;

import gui.App;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Main.CustomComboBox;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

public class RemoveAllByUnitDialog extends JDialog {

    private JComboBox<String> unitBox;
    private JLabel errorLabel;
    private boolean confirmed = false;

    public RemoveAllByUnitDialog() {
        super(null, "Remove all by unit", ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(580, 280));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getUnitOfMeasure() {
        Object selected = unitBox.getSelectedItem();

        if (selected == null) {
            return "";
        }

        return selected.toString().trim();
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

        unitBox = new CustomComboBox(new String[]{
                "",
                "KILOGRAMS",
                "METERS",
                "LITERS",
                "MILLILITERS"
        });

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        content.add(createFieldBlock("Unit Of Measure", unitBox), gbc);

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
        JButton removeButton = createRemoveButton("Remove all");

        cancelButton.addActionListener(e -> dispose());
        removeButton.addActionListener(this::removeAll);

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

    private void removeAll(ActionEvent e) {
        String unit = getUnitOfMeasure();

        if (unit.isEmpty()) {
            errorLabel.setText("Unit Of Measure is required.");
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

    private JButton createRemoveButton(String text) {
        JButton button = new RoundedButton(
                text,
                18,
                Color.RED,
                Color.WHITE,
                null
        );

        button.setPreferredSize(new Dimension(150, 52));
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