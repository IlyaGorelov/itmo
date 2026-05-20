package gui.Objects.Elements.Main.TableTab.Dialogs;

import gui.App;
import gui.Objects.Elements.Commons.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClearConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public ClearConfirmDialog() {
        super(null, "Clear collection", ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(540, 260));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
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

        JLabel question = new JLabel("Are you sure?");
        question.setFont(new Font("Arial", Font.PLAIN, 26));
        question.setForeground(App.TEXT_PURPLE);
        question.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(6, 0, 8, 0);
        content.add(question, gbc);

        JLabel warning = new JLabel("All your products will be removed from the collection.");
        warning.setFont(new Font("Arial", Font.ITALIC, 16));
        warning.setForeground(App.TEXT_GRAY);
        warning.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 22, 0);
        content.add(warning, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);

        JButton cancelButton = createSecondaryButton("Cancel");
        JButton clearButton = createClearButton("Clear");

        cancelButton.addActionListener(e -> dispose());

        clearButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        buttons.add(cancelButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 0, 0, 0);
        content.add(buttons, gbc);

        getRootPane().setDefaultButton(clearButton);

        return content;
    }

    private JButton createClearButton(String text) {
        JButton button = new RoundedButton(
                text,
                18,
                App.BACKGROUND,
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