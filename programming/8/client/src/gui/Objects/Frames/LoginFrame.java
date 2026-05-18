package gui.Objects.Frames;

import gui.App;
import gui.Objects.Elements.Login.LoginPanel;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame(){
        setSize(1100,650);
        setTitle("Product Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());

        setContentPane(new LoginPanel());
    }
}
