package gui.Objects.Frames;

import gui.App;
import gui.Objects.Elements.Main.MainPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Product Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setContentPane(new MainPanel());

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());
    }
}