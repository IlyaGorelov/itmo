package gui.Objects.Elements.Login;

import gui.App;
import gui.Objects.Elements.Commons.RoundedPanel;
import gui.Objects.Elements.Localized;
import gui.Objects.Frames.RegisterFrame;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottomPanel extends JPanel implements Localized {
    JLabel text;
    JLabel signUp;

    public BottomPanel() {
        super(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setOpaque(false);

        RoundedPanel card = new RoundedPanel(0, 0, 25, 25, Color.LIGHT_GRAY);
        card.setPreferredSize(new Dimension(460, 55));
        card.setLayout(new GridBagLayout());

        text = new JLabel(I18n.get("bottom.text"));
        text.setForeground(App.TEXT_GRAY);
        text.setFont(new Font("Arial", Font.PLAIN, 18));

        signUp = new JLabel(I18n.get("bottom.signup"));
        signUp.setForeground(App.TEXT_PURPLE);
        signUp.setFont(new Font("Arial", Font.PLAIN, 18));
        signUp.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);

                Window loginWindow = SwingUtilities.getWindowAncestor(BottomPanel.this);

                if (loginWindow != null) {
                    loginWindow.dispose();
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 0, 12);
        card.add(text, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(signUp, gbc);
        add(card);

    }

    @Override
    public void updateTexts() {
        text.setText(I18n.get("bottom.text"));
        signUp.setText(I18n.get("bottom.signup"));
    }
}

