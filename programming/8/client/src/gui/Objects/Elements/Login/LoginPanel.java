package gui.Objects.Elements.Login;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    public LoginPanel() {
        setLayout(new BorderLayout());
        setBackground(App.BACKGROUND);

        JPanel topPanel = new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
       JPanel centerPanel = new FormPanel(new GridBagLayout());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }


}
