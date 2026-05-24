package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedPanel;
import gui.Objects.Elements.Localized;
import Localization.I18n;

import javax.swing.*;
import java.awt.*;

public class TabsPanel extends JPanel implements Localized {

    private final JButton tableButton;
    private final JButton visualizationButton;

    public TabsPanel(Runnable onTableClick, Runnable onVisualizationClick) {
        super(new FlowLayout(FlowLayout.CENTER, 0, 5));
        setOpaque(false);

        RoundedPanel tabs = new RoundedPanel(25, Color.WHITE);
        tabs.setLayout(new GridLayout(1, 2));
        tabs.setPreferredSize(new Dimension(540, 65));
        tabs.setBorder(new RoundedBorder(Color.WHITE, 7, 25));

        tableButton = new TabButton(I18n.get("tabs.table"), true);
        visualizationButton = new TabButton(I18n.get("tabs.visual"), false);

        tableButton.addActionListener(e -> {
            selectTable();
            onTableClick.run();
        });

        visualizationButton.addActionListener(e -> {
            selectVisualization();
            onVisualizationClick.run();
        });

        tabs.add(tableButton);
        tabs.add(visualizationButton);

        add(tabs);

        selectTable();
    }

    private void selectTable() {
        tableButton.setBackground(App.BACKGROUND);
        tableButton.setForeground(Color.WHITE);

        visualizationButton.setBackground(Color.WHITE);
        visualizationButton.setForeground(App.TEXT_GRAY);

        tableButton.repaint();
        visualizationButton.repaint();
    }

    private void selectVisualization() {
        visualizationButton.setBackground(App.BACKGROUND);
        visualizationButton.setForeground(Color.WHITE);

        tableButton.setBackground(Color.WHITE);
        tableButton.setForeground(App.TEXT_GRAY);

        tableButton.repaint();
        visualizationButton.repaint();
    }

    @Override
    public void updateTexts() {
        tableButton.setText(I18n.get("tabs.table"));
        visualizationButton.setText(I18n.get("tabs.visual"));
    }
}