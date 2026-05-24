package gui.Objects.Elements.Login;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    FormPanel formPanel;

    public LoginPanel() {
        setLayout(new BorderLayout());
        setBackground(App.BACKGROUND);

        JPanel languagePanel = new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20), this::updateTexts);
        formPanel = new FormPanel(new GridBagLayout());

        add(languagePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }

    private void updateTexts(){
            formPanel.updateTexts();
            ((JFrame) getTopLevelAncestor()).setTitle(I18n.get("title"));
    }


}
