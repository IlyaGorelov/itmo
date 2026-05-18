package gui.Objects.Frames;

import gui.Objects.Elements.Login.LoginPanel;
import gui.Objects.Elements.Register.RegisterPanel;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame(){
        setSize(1100,650);
        setTitle("Product Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());

        setContentPane(new RegisterPanel());
    }
}
