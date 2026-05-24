package gui.Objects.Elements.Main;

import gui.App;
import gui.Objects.Elements.Commons.LanguagePanel;
import gui.Objects.Elements.Localized;
import gui.Objects.Elements.Main.TableTab.ContentPanel;
import gui.Objects.Elements.Main.VisualizationTab.VisualizationPanel;
import Localization.I18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainPanel extends JPanel implements Localized {

    private static final String TABLE_CARD = "table";
    private static final String VISUALIZATION_CARD = "visualization";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardsPanel = new JPanel(cardLayout);

    private final ContentPanel contentPanel = new ContentPanel();
    private final VisualizationPanel visualizationPanel = new VisualizationPanel();

    private UserPanel userPanel=new UserPanel();
    private TabsPanel tabsPanel = new TabsPanel(
            this::showTable,
            this::showVisualization
    );

    public MainPanel() {
        setBackground(App.BACKGROUND);
        setLayout(new BorderLayout());

        cardsPanel.setOpaque(false);

        cardsPanel.add(contentPanel, TABLE_CARD);
        cardsPanel.add(visualizationPanel, VISUALIZATION_CARD);

        add(createTopPanel(), BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);

        showTable();
    }

    public VisualizationPanel getVisualizationPanel(){
        return visualizationPanel;
    }

    private void showTable() {
        cardLayout.show(cardsPanel, TABLE_CARD);
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void showVisualization() {
        cardLayout.show(cardsPanel, VISUALIZATION_CARD);
        cardsPanel.revalidate();
        visualizationPanel.fetchProductsAsync();
        cardsPanel.repaint();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(25, 35, 10, 35));

        int sideWidth = 425;

        JPanel leftSlot = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftSlot.setOpaque(false);
        leftSlot.setPreferredSize(new Dimension(sideWidth, 80));
        leftSlot.add(userPanel);

        JPanel centerSlot = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerSlot.setOpaque(false);

        centerSlot.add(tabsPanel);

        JPanel rightSlot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightSlot.setOpaque(false);
        rightSlot.setPreferredSize(new Dimension(sideWidth, 80));
        rightSlot.add(new LanguagePanel(new FlowLayout(FlowLayout.RIGHT, 20, 20), this::updateTexts));

        topPanel.add(leftSlot, BorderLayout.WEST);
        topPanel.add(centerSlot, BorderLayout.CENTER);
        topPanel.add(rightSlot, BorderLayout.EAST);

        return topPanel;
    }

    @Override
    public void updateTexts() {
        tabsPanel.updateTexts();
        userPanel.updateTexts();
        visualizationPanel.updateTexts();
        contentPanel.updateTexts();
        ((JFrame) getTopLevelAncestor()).setTitle(I18n.get("title"));
    }
}