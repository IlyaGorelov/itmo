package gui.Objects.Elements.Main.TableTab.Dialogs;


import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Main.CustomComboBox;
import gui.Objects.Elements.Main.TableTab.TablePanel;
import Localization.I18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FilterDialog extends JDialog {

    public enum SortingOperation{
        EQUALS, STARTS,ENDS,CONTAINS
    }

    private String columnName;
    private JComboBox<OperationItem> operationBox;
    private JTextField valueField;

    private int modelColumn;


    public FilterDialog( String selectedColumn,int modelColumn) {
        super(null, I18n.get("dialog.filter.title"), ModalityType.APPLICATION_MODAL);

        this.modelColumn=modelColumn;
        this.columnName = selectedColumn;

        JComponent content = createContent(selectedColumn);
        setContentPane(content);

        pack();
        setLocationRelativeTo(null);
    }

    private JComponent createContent(String selectedColumn) {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        int row = 0;

        operationBox = new CustomComboBox<>(new OperationItem[]{
                new OperationItem(I18n.get("dialog.filter.equals"), SortingOperation.EQUALS),
                new OperationItem(I18n.get("dialog.filter.contains"), SortingOperation.CONTAINS),
                new OperationItem(I18n.get("dialog.filter.starts"), SortingOperation.STARTS),
                new OperationItem(I18n.get("dialog.filter.ends"), SortingOperation.ENDS)
        });

        valueField = createTextField();

        gbc.gridwidth = 1;
        gbc.gridy = row;

        gbc.gridx = 0;

        JLabel column = new JLabel(selectedColumn);
        column.setFont(new Font("Arial", Font.PLAIN, 20));
        column.setForeground(App.TEXT_PURPLE);
        column.setBorder(new RoundedBorder(App.BACKGROUND, 2, 18));
        column.setOpaque(true);
        column.setBackground(Color.WHITE);
        column.setPreferredSize(new Dimension(240, 44));
        column.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        column.setHorizontalAlignment(SwingConstants.CENTER);

        content.add(createFieldBlock(I18n.get("dialog.filter.column"), column), gbc);

        gbc.gridx = 1;
        content.add(createFieldBlock(I18n.get("dialog.filter.operation"), operationBox), gbc);

        gbc.gridx = 2;
        content.add(createFieldBlock(I18n.get("dialog.filter.value"), valueField), gbc);

        row++;

        row++;

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);

        JButton applyButton = createPrimaryButton(I18n.get("dialog.filter.apply"));

        applyButton.addActionListener(this::applyFilter);

        buttons.add(applyButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(24, 12, 4, 12);
        content.add(buttons, gbc);


        return content;
    }


    private void applyFilter(ActionEvent e) {
        OperationItem selectedOperation = (OperationItem) operationBox.getSelectedItem();

        if (selectedOperation == null) {
            return;
        }

        String value = valueField.getText().trim();

        System.out.println("Column: " + columnName);
        System.out.println("Operation: " + selectedOperation.operation());
        System.out.println("Value: " + value);

        TablePanel.filterTable(
                selectedOperation.operation(),
                modelColumn,
                value
        );

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
        field.setForeground(App.BACKGROUND);
        field.setCaretColor(App.BACKGROUND);
        field.setBackground(Color.WHITE);
        field.setBorder(new RoundedBorder(App.BACKGROUND, 2,18));
        field.setMargin(new Insets(8, 14, 8, 14));
        field.setPreferredSize(new Dimension(240, 44));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new RoundedButton(text, 18, App.BACKGROUND, Color.WHITE, null);

        FontMetrics fm = button.getFontMetrics(button.getFont());
        int width = Math.max(190, fm.stringWidth(text) + 45);

        button.setPreferredSize(new Dimension(width, 54));
        return button;
    }

    private record OperationItem(String label, SortingOperation operation) {
        @Override
        public String toString() {
            return label;
        }
    }


}
