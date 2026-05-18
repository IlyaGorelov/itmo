package gui.Objects.Elements.Register;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;
import gui.Objects.Elements.Register.FormPanel;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel() {
        setLayout(new BorderLayout());
        setBackground(App.BACKGROUND);

        JPanel topPanel = new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
       JPanel centerPanel = new FormPanel(new GridBagLayout());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }


}
