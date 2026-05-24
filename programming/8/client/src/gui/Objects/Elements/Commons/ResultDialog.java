package gui.Objects.Elements.Commons;
import gui.App;
import Localization.I18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResultDialog extends JDialog {

    public enum Type {
        ERROR("Error"),
        INFO("Info");

        private final String title;

        Type(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private final Type type;
    private final String message;

    public ResultDialog(Type type, String message) {
        super((Frame) null, type.getTitle(), true);

        this.type = type;
        this.message = message == null ? "" : message;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        setContentPane(createContent());

        pack();
        setMinimumSize(new Dimension(620, 360));

        setLocationRelativeTo(null);
    }

    public static String showError( String message) {
        show( Type.ERROR, message);
        return message;
    }

    public static String showInfo(String message) {
        show(Type.INFO, message);
        return message;
    }

    public static void show(Type type, String message) {
        ResultDialog dialog = new ResultDialog( type, message);
        dialog.setVisible(true);
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

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        content.add(createSectionHeader(getHeaderText()), gbc);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 18));
        messageArea.setForeground(new Color(55, 55, 55));
        messageArea.setBackground(Color.WHITE);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(new EmptyBorder(12, 14, 12, 14));
        messageArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setPreferredSize(new Dimension(560, 160));
        scrollPane.setBorder(new RoundedBorder(getAccentColor(), 2, 2));
        scrollPane.getViewport().setBackground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        content.add(scrollPane, gbc);

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton okButton = createOkButton("OK");

        okButton.addActionListener(e -> dispose());

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 0, 0, 0);
        content.add(okButton, gbc);

        getRootPane().setDefaultButton(okButton);

        return content;
    }

    private String getHeaderText() {
        return switch (type) {
            case ERROR -> I18n.get("result.fail");
            case INFO -> I18n.get("result");
        };
    }

    private Color getAccentColor() {
        return switch (type) {
            case INFO -> App.TEXT_PURPLE;
            case ERROR -> new Color(180, 40, 40);
        };
    }

    private JComponent createSectionHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setForeground(getAccentColor());
        label.setFont(new Font("Arial", Font.PLAIN, 24));

        JSeparator separator = new JSeparator();
        separator.setForeground(getAccentColor());
        separator.setBackground(getAccentColor());

        JPanel separatorWrapper = new JPanel(new GridBagLayout());
        separatorWrapper.setOpaque(false);
        separatorWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        separatorWrapper.add(separator, gbc);

        panel.add(label, BorderLayout.WEST);
        panel.add(separatorWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JButton createOkButton(String text) {
        JButton button = new RoundedButton(text,25,getAccentColor());
        button.setFont(new Font("Arial",Font.BOLD,18));
        button.setForeground(Color.WHITE);

        button.setPreferredSize(new Dimension(130, 52));
        return button;
    }
    }