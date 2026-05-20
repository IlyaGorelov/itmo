package gui.Objects.Elements.Main.TableTab.Dialogs;

import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class ExecuteScriptDialog extends JDialog {

    private JTextField pathField;
    private JLabel errorLabel;
    private boolean confirmed = false;

    public ExecuteScriptDialog() {
        super(null, "Execute script", ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(680, 260));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getScriptPath() {
        return pathField.getText().trim();
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

        pathField = createTextField();

        JButton browseButton = createSecondaryButton("Browse");
        browseButton.setPreferredSize(new Dimension(130, 44));
        browseButton.addActionListener(this::chooseScriptFile);

        JPanel pathInputPanel = new JPanel(new BorderLayout(12, 0));
        pathInputPanel.setOpaque(false);
        pathInputPanel.add(pathField, BorderLayout.CENTER);
        pathInputPanel.add(browseButton, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        content.add(createFieldBlock("Script path", pathInputPanel), gbc);

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

        JButton clearButton = createSecondaryButton("Clear");
        JButton executeButton = createPrimaryButton("Execute");

        clearButton.addActionListener(e -> {
            pathField.setText("");
            errorLabel.setText(" ");
        });

        executeButton.addActionListener(this::executeScript);

        buttons.add(clearButton);
        buttons.add(executeButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        content.add(buttons, gbc);

        getRootPane().setDefaultButton(executeButton);

        return content;
    }

    private void chooseScriptFile(ActionEvent e) {
        FileDialog fileDialog = new FileDialog(
                this,
                "Choose script file",
                FileDialog.LOAD
        );

        String programDirectory = System.getProperty("user.dir");
        fileDialog.setDirectory(programDirectory);
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);

        String directory = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();

        if (directory != null && fileName != null) {
            File file = new File(directory, fileName);
            pathField.setText(file.getAbsolutePath());
            errorLabel.setText(" ");
        }
    }

    private void executeScript(ActionEvent e) {
        String path = pathField.getText().trim();

        if (path.isEmpty()) {
            errorLabel.setText("Script path cannot be empty.");
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
        field.setBorder(new RoundedBorder(App.TEXT_PURPLE, 2, 12));
        field.setMargin(new Insets(8, 14, 8, 14));
        field.setPreferredSize(new Dimension(420, 44));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new RoundedButton(text,
                18,
                App.BACKGROUND,
                Color.WHITE,
                null
        );

        button.setPreferredSize(new Dimension(160, 52));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new RoundedButton(text,
                18,
                Color.WHITE,
                App.TEXT_PURPLE,
                App.TEXT_PURPLE);

        button.setPreferredSize(new Dimension(130, 52));

        return button;
    }
}
