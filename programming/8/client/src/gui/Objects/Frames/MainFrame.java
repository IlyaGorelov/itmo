package gui.Objects.Frames;

import Commons.Collection.Product;
import gui.Objects.Elements.Main.MainPanel;
import gui.Objects.Elements.Main.TableTab.TablePanel;
import Localization.I18n;

import javax.swing.*;

public class MainFrame extends JFrame {
    public Product[] actualProducts;

    public void setActualProducts(Product[] products){
        actualProducts = products;

        SwingUtilities.invokeLater(() -> {
            TablePanel.setProducts(products);
        });

        if(mainPanel.getVisualizationPanel().isVisible()){
            mainPanel.getVisualizationPanel().fetchProductsAsync();
        }
    }

    MainPanel mainPanel=new MainPanel();

    public MainFrame() {
        setTitle(I18n.get("title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setContentPane(mainPanel);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/img/icon.png"));
        setIconImage(imageIcon.getImage());
    }


}