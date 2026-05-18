package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TabsPanel extends JPanel {

    public TabsPanel() {
        super(new FlowLayout(FlowLayout.CENTER, 0, 5));
        setOpaque(false);

        RoundedPanel tabs = new RoundedPanel(25, Color.WHITE);
        tabs.setLayout(new GridLayout(1, 2));
        tabs.setPreferredSize(new Dimension(540, 65));

        tabs.setBorder(new RoundedBorder(Color.WHITE,7,25));

        JButton tableButton = new TabButton("Table", true);;
        JButton visualizationButton = new TabButton("Visualization", false);

        tableButton.addActionListener((ActionEvent e) -> {
            tableButton.setBackground(App.BACKGROUND);
            tableButton.setForeground(Color.WHITE);

            visualizationButton.setBackground(Color.WHITE);
            visualizationButton.setForeground(App.TEXT_GRAY);
        });

        visualizationButton.addActionListener((ActionEvent e) -> {
            visualizationButton.setBackground(App.BACKGROUND);
            visualizationButton.setForeground(Color.WHITE);

            tableButton.setBackground(Color.WHITE);
            tableButton.setForeground(App.TEXT_GRAY);
        });

        tabs.add(tableButton);
        tabs.add(visualizationButton);

        add(tabs);
    }
}