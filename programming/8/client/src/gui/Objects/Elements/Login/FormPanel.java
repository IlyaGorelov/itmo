package gui.Objects.Elements.Login;

import core.Objects.CommandsControllers.Commands.Login;
import core.Objects.Connection.Client;
import Commons.CustomPackage;
import Commons.UserData.User;
import core.Objects.Validators.PasswordValidator;
import core.Objects.Validators.StringValidator;
import core.Objects.Validators.UserValidator;
import gui.App;
import gui.Objects.Elements.Commons.InputPanel;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Commons.RoundedPanel;
import gui.Objects.Helpers.ErrorMessageDeliverer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FormPanel extends JPanel {
    private enum InputType {
        login, password
    }

    Timer timer;

    private Map<InputType, JLabel> errorLabels = new HashMap<>();
    private InputType fieldToBeChecked = InputType.login;
    private JLabel passwordErrorLabel = new JLabel();
    private JLabel loginErrorLabel = new JLabel();

    public FormPanel(GridBagLayout gridBagLayout) {
        super(gridBagLayout);
        errorLabels.put(InputType.login, loginErrorLabel);
        errorLabels.put(InputType.password, passwordErrorLabel);

        setOpaque(false);

        RoundedPanel card = new RoundedPanel(25, Color.WHITE);
        card.setPreferredSize(new Dimension(460, 365));
        card.setLayout(new BorderLayout());

        card.add(drawForm(), BorderLayout.CENTER);
        card.add(new BottomPanel(), BorderLayout.SOUTH);

        add(card);
    }

    private JPanel drawForm() {
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(25, 45, 20, 45));

        formPanel.add(drawTitle());
        formPanel.add(Box.createVerticalStrut(35));

        JTextField loginField = new JTextField();
        formPanel.add(drawLoginInput(loginField));
        formPanel.add(drawLoginErrorPanel());
        formPanel.add(Box.createVerticalStrut(20));

        JPasswordField passwordField = new JPasswordField();
        formPanel.add(drawPasswordInput(passwordField));
        formPanel.add(drawPasswordErrorPanel());
        formPanel.add(Box.createVerticalStrut(18));


        formPanel.add(drawLoginButton(loginField, passwordField));
        return formPanel;
    }

    private JLabel drawTitle() {
        JLabel title = new JLabel("Log in to your account");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(App.TEXT_PURPLE);
        title.setFont(new Font("Arial", Font.PLAIN, 28));
        return title;
    }

    private JPanel drawLoginInput(JTextField loginField) {
        JPanel loginInput = new InputPanel(loginField, new UserIcon(App.TEXT_PURPLE));
        loginInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        return loginInput;
    }

    private JPanel drawPasswordInput(JPasswordField passwordField) {
        JPanel passwordInput = new InputPanel(passwordField, new LockIcon(App.TEXT_PURPLE));
        passwordInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        char defaultEchoChar = passwordField.getEchoChar();

        JToggleButton showButton = new JToggleButton("Show");
        showButton.setFocusable(false);
        showButton.setBorderPainted(false);
        showButton.setContentAreaFilled(false);
        showButton.setForeground(App.TEXT_PURPLE);
        showButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        showButton.addActionListener(e -> {
            if (showButton.isSelected()) {
                passwordField.setEchoChar((char) 0);
                showButton.setText("Hide");
            } else {
                passwordField.setEchoChar(defaultEchoChar);
                showButton.setText("Show");
            }
        });

        passwordInput.add(showButton, BorderLayout.EAST);

        return passwordInput;
    }

    private JPanel drawLoginErrorPanel() {
        loginErrorLabel.setForeground(Color.RED);
        loginErrorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        loginErrorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setOpaque(false);
        errorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorPanel.setBorder(new EmptyBorder(6, 0, 0, 0));
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        errorPanel.add(loginErrorLabel, BorderLayout.WEST);

        return errorPanel;
    }

    private JPanel drawPasswordErrorPanel() {
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordErrorLabel.setVisible(false);

        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setOpaque(false);
        errorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorPanel.setBorder(new EmptyBorder(6, 0, 0, 0));
        errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        errorPanel.add(passwordErrorLabel, BorderLayout.WEST);

        return errorPanel;
    }


    private JButton drawLoginButton(JTextField loginField, JPasswordField passwordField) {
        JButton loginButton = new RoundedButton("Log in",null,8,8,8,8,App.BACKGROUND);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 24));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(App.TEXT_PURPLE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        loginButton.addActionListener(e -> {
            stopErrorListener();
            hideError();

            String login = loginField.getText();
            fieldToBeChecked = InputType.login;
            if (!(new StringValidator().isValid(login, false))) {
                showError(ErrorMessageDeliverer.poll());
            }

            String password = new String(passwordField.getPassword());
            fieldToBeChecked = InputType.password;
            if (!(new PasswordValidator().isValid(password, false))) {
                showError(ErrorMessageDeliverer.poll());
                return;
            }

            System.out.println("Login: " + login);
            System.out.println("Password: " + password);

            User user = new User(0, login, password);

            UserValidator userValidator = new UserValidator();
            if (userValidator.isValid(user, false) && ErrorMessageDeliverer.hasNoErrors())
                Client.putCommand(new CustomPackage(new Login().getName(), null, user));

            startErrorListener();
        });

        return loginButton;
    }

    private static class UserIcon implements Icon {
        private final Color color;

        public UserIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);

            g2.fillOval(x + 9, y + 4, 12, 12);
            g2.fillRoundRect(x + 5, y + 17, 20, 12, 10, 10);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 30;
        }

        @Override
        public int getIconHeight() {
            return 30;
        }
    }

    private static class LockIcon implements Icon {
        private final Color color;

        public LockIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);

            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(x + 8, y + 5, 14, 14, 8, 8);
            g2.fillRoundRect(x + 5, y + 15, 20, 14, 4, 4);

            g2.setColor(Color.WHITE);
            g2.fillOval(x + 13, y + 19, 4, 4);
            g2.fillRect(x + 14, y + 22, 2, 4);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return 30;
        }

        @Override
        public int getIconHeight() {
            return 30;
        }
    }

    private void startErrorListener() {
        Timer timer = new Timer(100, e -> {
            String error = ErrorMessageDeliverer.poll();

            if (error != null) {
                showError(error);
            }
        });

        timer.start();
    }

    private void stopErrorListener() {
        if (timer != null) timer.stop();
    }

    public void showError(String message) {
        if (message == null) return;

        for (JLabel label : errorLabels.values()) {
            if (errorLabels.get(fieldToBeChecked) == label)
                SwingUtilities.invokeLater(() -> {
                    label.setText(message);
                    label.setVisible(true);
                    revalidate();
                    repaint();
                });
        }
    }

    public void hideError() {
        for (JLabel label : errorLabels.values()) {
            SwingUtilities.invokeLater(() -> {
                label.setText("");
                label.setVisible(false);
                revalidate();
                repaint();
            });
        }
    }
}
