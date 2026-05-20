package gui.Objects.Elements.Main.TableTab.Dialogs;


import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Main.CustomComboBox;
import gui.Objects.Elements.Main.TableTab.TablePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FilterDialog extends JDialog {

    public enum SortingOperation{
        EQUALS, STARTS,ENDS,CONTAINS
    }

    private String columnName;
    private JComboBox<String> operationBox;
    private JTextField valueField;
    private JTextField secondValueField;
    private JLabel secondValueLabel;
    private JPanel secondValuePanel;

    private int modelColumn;


    public FilterDialog( String selectedColumn,int modelColumn) {
        super(null, "Add filter", ModalityType.APPLICATION_MODAL);

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
//        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        int row = 0;



        operationBox = new CustomComboBox(new String[]{
                "Equals",
                "Contains",
                "Starts with",
                "Ends with"
        });

        valueField = createTextField();
        secondValueField = createTextField();

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

        content.add(createFieldBlock("Column", column), gbc);

        gbc.gridx = 1;
        content.add(createFieldBlock("Operation", operationBox), gbc);

        gbc.gridx = 2;
        content.add(createFieldBlock("Value", valueField), gbc);

        row++;

        secondValueLabel = new JLabel("Second Value");
        secondValueLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        secondValueLabel.setForeground(App.TEXT_PURPLE);

        secondValuePanel = createFieldBlock("Second Value", secondValueField);
        secondValuePanel.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        content.add(secondValuePanel, gbc);

        row++;

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);

        JButton applyButton = createPrimaryButton("Apply filter");

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
        String column = String.valueOf(columnName);
        String operation = String.valueOf(operationBox.getSelectedItem());
        String value = valueField.getText().trim();

        System.out.println("Column: " + column);
        System.out.println("Operation: " + operation);
        System.out.println("Value: " + value);

        switch (operation){
            case "Equals" -> TablePanel.filterTable(SortingOperation.EQUALS,modelColumn,value);
            case "Contains" -> TablePanel.filterTable(SortingOperation.CONTAINS,modelColumn,value);
            case "Starts with" -> TablePanel.filterTable(SortingOperation.STARTS,modelColumn,value);
            case "Ends with" -> TablePanel.filterTable(SortingOperation.ENDS,modelColumn,value);
        }
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

    private JComponent createSectionHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setForeground(App.TEXT_PURPLE);
        label.setFont(new Font("Arial", Font.PLAIN, 24));

        JSeparator separator = new JSeparator();
        separator.setForeground(App.TEXT_PURPLE);
        separator.setBackground(App.TEXT_PURPLE);

        JPanel separatorWrapper = new JPanel(new GridBagLayout());
        separatorWrapper.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        separatorWrapper.add(separator, gbc);

        panel.add(label, BorderLayout.WEST);
        panel.add(separatorWrapper, BorderLayout.CENTER);

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
        JButton button = new RoundedButton(text, 18,App.BACKGROUND, Color.WHITE,null);
        button.setPreferredSize(new Dimension(190, 54));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new RoundedButton(text,18, Color.WHITE, App.BACKGROUND,null);
        button.setBorder(new RoundedBorder(App.BACKGROUND, 2,18));
        button.setPreferredSize(new Dimension(130, 54));
        return button;
    }

}
