package gui.Objects.Elements.Main.VisualizationTab;

import Commons.Collection.Location;
import Commons.Collection.Person;
import Commons.Collection.Product;
import Commons.CustomPackage;
import Localization.EnumI18n;
import core.Objects.CommandsControllers.Commands.Remove;
import core.Objects.CommandsControllers.Commands.Update;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Localized;
import gui.Objects.Elements.Main.TableTab.Dialogs.ProductDialog;
import Localization.I18n;
import gui.Objects.Helpers.Formatters;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Date;

public class ProductInfoDialog extends JDialog implements Localized {

    private static final Color PURPLE = App.TEXT_PURPLE;

    private static final int DIALOG_WIDTH = 1200;
    private static final int DIALOG_HEIGHT = 610;

    private static final int FIELD_WIDTH = 220;
    private static final int FIELD_HEIGHT = 38;

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 28);
    private static final Font SMALL_LABEL_FONT = new Font("Arial", Font.PLAIN, 24);
    private static final Font VALUE_FONT = new Font("Arial", Font.PLAIN, 20);

    private Product product;

    public ProductInfoDialog(Component parent, Product product) {
        super(
                (JFrame) null,
                I18n.get("info.title"),
                true
        );
        this.product = product;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(createContent(this, product));

        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        pack();
        setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JPanel createContent(JDialog dialog, Product product) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(26, 24, 26, 24));

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 30);
        center.add(createProductPanel(product), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        center.add(createOwnerPanel(product), gbc);

        root.add(center, BorderLayout.CENTER);
        root.add(createBottomPanel(dialog, product), BorderLayout.SOUTH);


        return root;
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = createTitledPanel(I18n.get("info.product.panel"), 390, 470);

        GridBagConstraints gbc = createBaseConstraints();

        int row = 0;

        addField(productPanel, gbc, row++, "ID", value(product.getId()));
        addField(productPanel, gbc, row++, I18n.get("product.name"), value(product.getName()));

        if (product.getCoordinates() != null) {
            addField(productPanel, gbc, row++, "X", Formatters.integer(product.getCoordinates().getX()));
            addField(productPanel, gbc, row++, "Y", Formatters.decimal(product.getCoordinates().getY()));
        } else {
            addField(productPanel, gbc, row++, "X", "-");
            addField(productPanel, gbc, row++, "Y", "-");
        }

        addField(productPanel, gbc, row++, I18n.get("product.price"), product.getPrice() != null ? Formatters.decimal(product.getPrice()) : "-");

        addField(
                productPanel,
                gbc,
                row++,
                I18n.get("product.man.cost"),
                Formatters.integer(product.getManufactureCost())
        );

        addField(
                productPanel,
                gbc,
                row,
                I18n.get("product.unit"),
                EnumI18n.unitOfMeasure(product.getUnitOfMeasure())
        );

        return productPanel;
    }

    private static JPanel createOwnerPanel(Product product) {
        JPanel panel = createTitledPanel(I18n.get("info.owner.panel"), 390, 470);

        Person owner = product.getOwner();
        GridBagConstraints gbc = createBaseConstraints();

        int row = 0;

        if (owner == null) {
            addField(panel, gbc, row, I18n.get("info.owner.panel"), I18n.get("nodata"));
            return panel;
        }

        addField(panel, gbc, row++, I18n.get("owner.name"), value(owner.getName()));
        addField(panel, gbc, row++, I18n.get("owner.height"), Formatters.decimal(owner.getHeight()));
        addField(panel, gbc, row++,  I18n.get("owner.eye"), EnumI18n.eyeColor(owner.getEyeColor()));
        addField(panel, gbc, row++,  I18n.get("owner.hair"), EnumI18n.hairColor(owner.getHairColor()));
        addField(panel, gbc, row++, I18n.get("owner.country"), EnumI18n.country(owner.getNationality()));

        addLocationHeader(panel, gbc, row++);

        Location location = owner.getLocation();

        if (location == null) {
            addField(panel, gbc, row, I18n.get("owner.loc"), I18n.get("nodata"));
            return panel;
        }

        addLocationCoordinates(panel, gbc, row++, location);
        addLocationName(panel, gbc, row, location);

        return panel;
    }

    private JPanel createBottomPanel(JDialog dialog, Product product) {
        JPanel bottom = new JPanel(new GridBagLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(16, 12, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 12);

        JLabel authorLabel = createLabel(I18n.get("product.author"), SMALL_LABEL_FONT);

        JLabel authorValue = createValueBox(
                product.getAuthor() != null ? product.getAuthor().getLogin() : "-",
                140,
                FIELD_HEIGHT
        );


        gbc.weightx = 0;
        bottom.add(authorLabel, gbc);
        gbc.gridx = 1;
        bottom.add(authorValue, gbc);

        gbc.gridx = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottom.add(Box.createHorizontalGlue(), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 12, 0, 0);
        bottom.add(drawUpdateButton(dialog, product), gbc);

        gbc.gridx = 4;
        bottom.add(drawRemoveButton(dialog, product), gbc);

        return bottom;
    }

    private JButton drawUpdateButton(JDialog dialog, Product product) {
        RoundedButton updateButton = new RoundedButton(
                I18n.get("update"),
                25,
                App.BACKGROUND,
                Color.WHITE,
                null
        );

        if (!AuthManager.getInstance().getUser().getLogin().equals(product.getAuthor().getLogin())) {
            updateButton.setVisible(false);
        }

        updateButton.setPreferredSize(new Dimension(120, FIELD_HEIGHT + 10));

        updateButton.addActionListener(e -> {
            Product updatedProduct = openUpdateDialog(product);

            if (updatedProduct != null) {
                dialog.setContentPane(createContent(dialog, updatedProduct));
                dialog.revalidate();
                dialog.repaint();
                dialog.pack();
                dialog.setLocationRelativeTo(dialog.getOwner());
            }
        });
        return updateButton;
    }

    private static JButton drawRemoveButton(JDialog dialog, Product product) {
        RoundedButton removeButton = new RoundedButton(
                I18n.get("remove"),
                25,
                Color.RED,
                Color.WHITE,
                null
        );

        if (!AuthManager.getInstance().getUser().getLogin().equals(product.getAuthor().getLogin())) {
            removeButton.setVisible(false);
        }

        removeButton.setPreferredSize(new Dimension(120, FIELD_HEIGHT + 10));

        removeButton.addActionListener(e -> {
            Client.putCommand(new CustomPackage(
                    new Remove().getName(),
                    product.getId().toString(),
                    null,
                    AuthManager.getInstance().getUser()));
            dialog.dispose();
        });
        return removeButton;
    }

    private static JPanel createTitledPanel(String title, int width, int height) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        TitledBorder productBorder = BorderFactory.createTitledBorder(
                new LineBorder(PURPLE, 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                TITLE_FONT,
                PURPLE
        );

        panel.setBorder(productBorder);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setMinimumSize(new Dimension(width, height));

        return panel;
    }

    private static GridBagConstraints createBaseConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        return gbc;
    }

    private static void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            String value
    ) {
        JLabel labelComponent = createLabel(label, LABEL_FONT);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.insets = new Insets(5, 14, 5, 12);
        panel.add(labelComponent, gbc);

        JLabel valueComponent = createValueBox(value, FIELD_WIDTH, FIELD_HEIGHT);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 8, 5, 14);
        panel.add(valueComponent, gbc);
    }

    private static void addLocationHeader(JPanel panel, GridBagConstraints gbc, int row) {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JLabel title = new JLabel(I18n.get("owner.loc"));
        title.setForeground(PURPLE);
        title.setFont(TITLE_FONT);

        header.add(title, BorderLayout.WEST);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 12, 4, 20);
        panel.add(header, gbc);

        gbc.gridwidth = 1;
    }

    private static void addLocationCoordinates(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            Location location
    ) {
        JPanel locationRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        locationRow.setOpaque(false);

        locationRow.add(createLabel("X", LABEL_FONT));
        locationRow.add(createValueBox(Formatters.decimal(location.getX()), 100, FIELD_HEIGHT));

        locationRow.add(createLabel("Y", LABEL_FONT));
        locationRow.add(createValueBox(Formatters.integer(location.getY()), 100, FIELD_HEIGHT));

        locationRow.add(createLabel("Z", LABEL_FONT));
        locationRow.add(createValueBox(Formatters.decimal(location.getZ()), 100, FIELD_HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(4, 14, 8, 10);
        panel.add(locationRow, gbc);

        gbc.gridwidth = 1;
    }

    private static void addLocationName(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            Location location
    ) {
        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        nameRow.setOpaque(false);

        nameRow.add(createLabel(I18n.get("owner.loc.name"), LABEL_FONT));
        nameRow.add(createValueBox(value(location.getName()), 130, FIELD_HEIGHT));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 14, 0, 10);
        panel.add(nameRow, gbc);

        gbc.gridwidth = 1;
    }

    private static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(PURPLE);
        label.setFont(font);
        return label;
    }

    private static JLabel createValueBox(String value, int width, int height) {
        JLabel label = new JLabel(value == null || value.isBlank() ? "-" : value);
        label.setFont(VALUE_FONT);
        label.setForeground(PURPLE);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(PURPLE, 1, 12),
                new EmptyBorder(0, 12, 0, 12)
        ));

        Dimension size = new Dimension(width, height);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.setMaximumSize(size);

        return label;
    }

    private static String value(Object value) {
        return value == null ? "-" : String.valueOf(value);
    }

    private static Product openUpdateDialog(Product product) {

        ProductDialog productDialog = new ProductDialog(ProductDialog.Mode.UPDATE, product);
        productDialog.setVisible(true);

        if (productDialog.isConfirmed()) {
            String commandForServer = productDialog.getServerCommand();

            System.out.println("Command for server: " + commandForServer);
            System.out.println("Product ID: " + product.getId());
            System.out.println("Name: " + productDialog.getNameValue());

            Product newProduct = new Product(
                    product.getId(),
                    productDialog.getNameValue(),
                    productDialog.getCoordinatesValue(),
                    new Date(),
                    productDialog.getPriceValue(),
                    productDialog.getManufactureCostValue(),
                    productDialog.getUnitOfMeasureValue(),
                    productDialog.getPersonValue(),
                    AuthManager.getInstance().getUser()
            );


            CustomPackage updateRequest = new CustomPackage(
                    new Update().getName(),
                    product.getId().toString(),
                    newProduct,
                    AuthManager.getInstance().getUser());
            Client.putCommand(updateRequest);
            return newProduct;
        }
        return null;
    }

    @Override
    public void updateTexts() {
        setTitle(I18n.get("info.title"));

        setContentPane(createContent(this, product));
        revalidate();
        repaint();
        pack();
    }
}