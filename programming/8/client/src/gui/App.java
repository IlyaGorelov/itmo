package gui;

import core.Objects.Connection.Client;
import gui.Objects.Frames.ConnectingFrame;
import gui.Objects.Frames.LoginFrame;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;
import java.awt.*;

public class App {
    // make add,update,remove menus
    // make visualizations


    public static final Color BACKGROUND = Color.decode("#7E4286");
    public static final Color TEXT_PURPLE = Color.decode("#7E4286");
    public static final Color LIGHT_GRAY = Color.decode("#DCDCDC");
    public static final Color TEXT_GRAY = Color.decode("#777777");
    public static final Color TABLE_HEADER_BG = Color.decode("#E5E5E5");

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    public static void main(String[] args) throws Exception {
        new MainFrame().setVisible(true);
//        Client client = new Client();
//        client.connect(HOST, PORT, Client.Mode.GUI);


    }
}