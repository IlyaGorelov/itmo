package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;
import gui.Objects.Elements.Main.TableTab.ContentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel() {
        setBackground(App.BACKGROUND);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);
        add(new ContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(25, 35, 10, 35));

        int sideWidth = 425;

        JPanel leftSlot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSlot.setOpaque(false);
        leftSlot.setPreferredSize(new Dimension(sideWidth, 80));
        leftSlot.add(new UserPanel());

        JPanel centerSlot = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerSlot.setOpaque(false);
        centerSlot.add(new TabsPanel());

        JPanel rightSlot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightSlot.setOpaque(false);
        rightSlot.setPreferredSize(new Dimension(sideWidth, 80));
        rightSlot.add(new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20)));

        topPanel.add(leftSlot, BorderLayout.WEST);
        topPanel.add(centerSlot, BorderLayout.CENTER);
        topPanel.add(rightSlot, BorderLayout.EAST);

        return topPanel;
    }


    }
