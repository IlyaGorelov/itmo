package gui.Objects.Elements.Main.TableTab;

import Commons.Collection.Person;
import Commons.Collection.Product;
import Commons.CustomPackage;
import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.Commands.*;
import core.Objects.Connection.Client;
import core.Objects.Managers.AuthManager;
import gui.App;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Main.TableTab.Dialogs.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;

public class ContentPanel extends JPanel {
    public enum CommandLabel {
        ADD(
                "Add",
                new Add().getName()
        ),

        ADD_IF_MAX(
                "Add if Max",
                new AddIfMax().getName()
        ),

        ADD_IF_MIN(
                "Add if Min",
                new AddIfMin().getName()
        ),

        CLEAR(
                "Clear",
                new Clear().getName()
        ),

        EXECUTE_SCRIPT(
                "Execute Script",
                new ExecuteScript().getName()
        ),

        FILTER_GREATER_THAN_OWNER(
                "Filter Greater Than Owner",
                new GreaterThanOwner().getName()
        ),

        MIN_BY_UNIT(
                "Min By Unit of Measure",
                new MinByUnit().getName()
        ),

        REDO(
                "Redo",
                new Redo().getName()
        ),

        REMOVE(
                "Remove",
                new Remove().getName()
        ),

        REMOVE_BY_UNIT(
                "Remove By Unit",
                new RemoveByUnitOfMeasure().getName()
        ),

        REMOVE_GREATER(
                "Remove Greater",
                new RemoveGreater().getName()
        ),

        UNDO(
                "Undo",
                new Undo().getName()
        ),

        UPDATE(
                "Update",
                new Update().getName()
        );

        private final String guiSideLabel;
        private final String programSideLabel;

        CommandLabel(String guiSideLabel, String programSideLabel) {
            this.guiSideLabel = guiSideLabel;
            this.programSideLabel = programSideLabel;
        }

        public String getGUISideLabel() {
            return guiSideLabel;
        }

        public String getProgramSideLabel() {
            return programSideLabel;
        }

        @Override
        public String toString() {
            return guiSideLabel;
        }
    }

    public ContentPanel() {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(15, 35, 35, 35));

        add(drawTopPart(), BorderLayout.NORTH);
        add(new TablePanel(), BorderLayout.CENTER);

    }

    private JPanel drawTopPart() {
        JPanel titleAndButtons = new JPanel(new BorderLayout());
        titleAndButtons.setOpaque(false);
        titleAndButtons.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel title = new JLabel("Products");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.PLAIN, 42));

        titleAndButtons.add(title, BorderLayout.WEST);
        titleAndButtons.add(drawButtons(), BorderLayout.CENTER);

        return titleAndButtons;
    }

    private JPanel drawButtons() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 0));
        buttonsPanel.setOpaque(false);

        JButton undoButton = createSimpleButton(CommandLabel.UNDO, 100);
        JButton redoButton = createSimpleButton(CommandLabel.REDO, 100);

        JButton updateButton = createSimpleButton(CommandLabel.UPDATE, 140);
        JButton removeButton = createSimpleButton(CommandLabel.REMOVE, 140);
        JButton executeScriptButton = createSimpleButton(CommandLabel.EXECUTE_SCRIPT, 220);

        JButton showDropDown = createDropDownButton(
                "Show",
                150,
                CommandLabel.FILTER_GREATER_THAN_OWNER,
                CommandLabel.MIN_BY_UNIT
        );

        JButton clearDropDown = createDropDownButton(
                "Clear",
                150,
                CommandLabel.CLEAR,
                CommandLabel.REMOVE_BY_UNIT,
                CommandLabel.REMOVE_GREATER
        );

        JButton addDropDown = createDropDownButton(
                "Add",
                140,
                CommandLabel.ADD,
                CommandLabel.ADD_IF_MIN,
                CommandLabel.ADD_IF_MAX
        );

        undoButton.addActionListener(e -> {
            Client.putCommand(new CustomPackage(new Undo().getName(),null,null,AuthManager.getInstance().getUser()));
        });

        redoButton.addActionListener(e -> {
            Client.putCommand(new CustomPackage(new Redo().getName(),null,null,AuthManager.getInstance().getUser()));
        });

        updateButton.addActionListener(e -> {
            openUpdateIdDialog();
        });

        removeButton.addActionListener(e -> {
            openRemoveDialog();
        });

        executeScriptButton.addActionListener(e -> {
            ExecuteScriptDialog dialog = new ExecuteScriptDialog();
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                String scriptPath = dialog.getScriptPath();

                System.out.println("execute_script " + scriptPath);

                Client.putCommand(new CustomPackage(new ExecuteScript().getName(),scriptPath,null));
            }
        });

        buttonsPanel.add(undoButton);
        buttonsPanel.add(redoButton);
        buttonsPanel.add(Box.createHorizontalStrut(100));
        buttonsPanel.add(executeScriptButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(showDropDown);
        buttonsPanel.add(clearDropDown);
        buttonsPanel.add(addDropDown);

        return buttonsPanel;
    }

    private JButton createSimpleButton(CommandLabel label, int width) {
        JButton button = new RoundedButton(label.getGUISideLabel(), 25, Color.WHITE);

        button.setPreferredSize(new Dimension(width, 55));
        button.setFont(new Font("Arial", Font.PLAIN, 28));
        button.setForeground(App.TEXT_PURPLE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setName(label.getProgramSideLabel());

        return button;
    }

    private JButton createDropDownButton(String label, int width, CommandLabel... variants) {
        JButton button = new RoundedButton(label + " ▼", 25, Color.WHITE);
        button.setPreferredSize(new Dimension(width, 55));
        button.setFont(new Font("Arial", Font.PLAIN, 28));
        button.setForeground(App.TEXT_PURPLE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPopupMenu popupMenu = new JPopupMenu();

        popupMenu.setBackground(Color.WHITE);
        popupMenu.setBorder(BorderFactory.createLineBorder(App.TEXT_PURPLE, 2));

        for (CommandLabel variant : variants) {
            JMenuItem item = new JMenuItem(variant.getGUISideLabel());
            item.setName(variant.getProgramSideLabel());

            item.setFont(new Font("Arial", Font.PLAIN, 22));
            item.setForeground(App.TEXT_PURPLE);
            item.setBackground(Color.WHITE);
            item.setCursor(new Cursor(Cursor.HAND_CURSOR));

            item.addActionListener(e -> {
                System.out.println("Selected command: " + variant);
                openDialog(variant);
                // здесь потом вызывай свою команду
                // например:
                // GUICommand = new CustomPackage(variant, null, null);
            });

            popupMenu.add(item);
        }

        button.addActionListener(e -> {
            popupMenu.show(button, 0, button.getHeight());
        });

        return button;
    }

    private void openProductDialog(ProductDialog.Mode mode) {
        ProductDialog dialog = new ProductDialog(mode);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String commandForServer = dialog.getServerCommand();

            System.out.println("Command for server: " + commandForServer);
            System.out.println("Name: " + dialog.getNameValue());
            System.out.println("Price: " + dialog.getPriceValue());

            Product newProduct = new Product(
                    0,
                    dialog.getNameValue(),
                    dialog.getCoordinatesValue(),
                    new Date(),
                    dialog.getPriceValue(),
                    dialog.getManufactureCostValue(),
                    dialog.getUnitOfMeasureValue(),
                    dialog.getPersonValue(),
                    AuthManager.getInstance().getUser()
            );

            Command command = switch (mode) {
                case ADD -> new Add();
                case ADD_IF_MIN -> new AddIfMin();
                case ADD_IF_MAX -> new AddIfMax();
                case UPDATE -> new Update();
            };

            CustomPackage request = new CustomPackage(
                    command.getName(),
                    null,
                    newProduct,
                    AuthManager.getInstance().getUser());
            Client.putCommand(request);
        }
    }

    private void openDialog(CommandLabel label) {
        switch (label) {
            case ADD -> openProductDialog(ProductDialog.Mode.ADD);
            case ADD_IF_MIN -> openProductDialog(ProductDialog.Mode.ADD_IF_MIN);
            case ADD_IF_MAX -> openProductDialog(ProductDialog.Mode.ADD_IF_MAX);
            case CLEAR -> openClearDialog();
            case FILTER_GREATER_THAN_OWNER -> openOwnerFilterDialog();
            case MIN_BY_UNIT -> openMinByUnitDialog();
            case REMOVE_BY_UNIT -> openRemoveAllByUnitDialog();
            case REMOVE_GREATER -> openRemoveGreaterDialog();
        }
    }

    private void openUpdateIdDialog() {
        ProductIdDialog idDialog = new ProductIdDialog();
        idDialog.setVisible(true);

        if (!idDialog.isConfirmed()) {
            return;
        }

        String productId = idDialog.getProductId();

        ProductDialog productDialog = new ProductDialog(ProductDialog.Mode.UPDATE);
        productDialog.setProductId(productId);
        productDialog.setVisible(true);

        if (productDialog.isConfirmed()) {
            String commandForServer = productDialog.getServerCommand();

            System.out.println("Command for server: " + commandForServer);
            System.out.println("Product ID: " + productId);
            System.out.println("Name: " + productDialog.getNameValue());

            Product newProduct = new Product(
                    Long.parseLong(productId),
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
                    productId,
                    newProduct,
                    AuthManager.getInstance().getUser());
            Client.putCommand(updateRequest);
        }
    }

    private void openRemoveDialog() {
        RemoveDialog dialog = new RemoveDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String productId = dialog.getProductId();
        String commandForServer = "remove";

        System.out.println("Command for server: " + commandForServer);
        System.out.println("Product ID: " + productId);

        Client.putCommand(new CustomPackage(
                new Remove().getName(),
                productId,
                null,
                AuthManager.getInstance().getUser()));

    }

    private void openClearDialog() {

        ClearConfirmDialog dialog = new ClearConfirmDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String commandForServer = "clear";

        System.out.println("Command for server: " + commandForServer);

        Client.putCommand(new CustomPackage(
                new Clear().getName(),
                null,
                null,
                AuthManager.getInstance().getUser()));
    }

    private void openOwnerFilterDialog() {
        OwnerFilterDialog dialog = new OwnerFilterDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String commandForServer = "filter_greater_than_owner";

        String ownerName = dialog.getOwnerName();
        String height = dialog.getHeightValue();

        System.out.println("Command for server: " + commandForServer);
        System.out.println("Owner name: " + ownerName);
        System.out.println("Height: " + height);

        Person newOwner = new Person(
                ownerName,
                Float.parseFloat(height),
                null,
                null,
                null,
                null
        );

        Client.putCommand(new CustomPackage(
                new GreaterThanOwner().getName(),
                null,
                newOwner,
                AuthManager.getInstance().getUser()));
    }

    private void openMinByUnitDialog() {
        MinByUnitDialog dialog = new MinByUnitDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String commandForServer = "min_by_unit";
        String unitOfMeasure = dialog.getUnitOfMeasure();

        System.out.println("Command for server: " + commandForServer);
        System.out.println("Unit of Measure: " + unitOfMeasure);

        Client.putCommand(new CustomPackage(
                            new MinByUnit().getName(),
                            unitOfMeasure, null,
                            AuthManager.getInstance().getUser()));
    }

    private void openRemoveAllByUnitDialog() {
        RemoveAllByUnitDialog dialog = new RemoveAllByUnitDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String commandForServer = "remove_all_by_unit";
        String unitOfMeasure = dialog.getUnitOfMeasure();

        System.out.println("Command for server: " + commandForServer);
        System.out.println("Unit of Measure: " + unitOfMeasure);

        Client.putCommand(new CustomPackage(
                new RemoveByUnitOfMeasure().getName()
                , unitOfMeasure,
                null,
                AuthManager.getInstance().getUser()));
    }

    private void openRemoveGreaterDialog() {
        RemoveGreaterDialog dialog = new RemoveGreaterDialog();
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return;
        }

        String commandForServer = "remove_greater";
        String price = dialog.getPriceValue();
        String manufactureCost = dialog.getManufactureCostValue();

        System.out.println("Command for server: " + commandForServer);
        System.out.println("Price: " + price);
        System.out.println("Manufacture Cost: " + manufactureCost);

        Product newProduct = new Product(
                0,
                null,
                null,
                new Date(),
                Double.parseDouble(price),
                Integer.parseInt(manufactureCost),
                null,
                null,
                AuthManager.getInstance().getUser()
        );

        Client.putCommand(new CustomPackage(
                new RemoveGreater().getName(),
                null, newProduct,
                AuthManager.getInstance().getUser()));
    }

}
