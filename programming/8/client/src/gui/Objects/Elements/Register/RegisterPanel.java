package gui.Objects.Elements.Register;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    FormPanel centerPanel;

    public RegisterPanel() {
        setLayout(new BorderLayout());
        setBackground(App.BACKGROUND);

        JPanel topPanel = new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20), this::updateTexts);
        centerPanel = new FormPanel(new GridBagLayout());

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void updateTexts(){
        centerPanel.updateTexts();
       ( (JFrame) getTopLevelAncestor()).setTitle(I18n.get("title"));
    }


}
