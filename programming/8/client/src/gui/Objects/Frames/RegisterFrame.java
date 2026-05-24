package gui.Objects.Frames;

import gui.Objects.Elements.Register.RegisterPanel;
import Localization.I18n;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame(){
        setSize(1100,650);
        setTitle(I18n.get("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());

        setContentPane(new RegisterPanel());
    }
}
