package gui.Objects.Elements.Main.TableTab.Dialogs;

import Commons.Collection.Coordinates;
import Commons.Collection.Location;
import Commons.Collection.Person;
import Commons.Enums.Country;
import Commons.Enums.EyeColor;
import Commons.Enums.HairColor;
import Commons.Enums.UnitOfMeasure;
import core.Objects.Validators.DoubleValidator;
import core.Objects.Validators.HeightValidator;
import core.Objects.Validators.IntegerValidator;
import core.Objects.Validators.PriceValidator;
import gui.App;
import gui.Objects.Elements.Commons.RoundedBorder;
import gui.Objects.Elements.Commons.RoundedButton;
import gui.Objects.Elements.Main.CustomComboBox;
import gui.Objects.Helpers.ErrorMessageDeliverer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProductDialog extends JDialog {

    private static final int FIELD_WIDTH = 230;
    private static final int FIELD_HEIGHT = 44;
    private static final int ERROR_HEIGHT = 18;

    public enum Mode {
        ADD("Add product", "add", "Add product"),
        ADD_IF_MIN("Add product if min", "add_if_min", "Add if min"),
        ADD_IF_MAX("Add product if max", "add_if_max", "Add if max"),
        UPDATE("Update product", "update", "Update product");

        private final String title;
        private final String serverCommand;
        private final String buttonText;

        Mode(String title, String serverCommand, String buttonText) {
            this.title = title;
            this.serverCommand = serverCommand;
            this.buttonText = buttonText;
        }

        public String getTitle() {
            return title;
        }

        public String getServerCommand() {
            return serverCommand;
        }

        public String getButtonText() {
            return buttonText;
        }
    }

    private final Mode mode;
    private boolean confirmed = false;

    private JTextField idField;

    private JTextField nameField;
    private JTextField xField;
    private JTextField yField;
    private JTextField priceField;
    private JTextField manufactureCostField;
    private JComboBox<String> unitOfMeasureBox;

    private JTextField ownerNameField;
    private JPanel ownerDetailsPanel;
    private JTextField heightField;
    private JComboBox<String> eyeColorBox;
    private JComboBox<String> hairColorBox;
    private JComboBox<String> countryBox;

    private JPanel locationDetailsPanel;
    private JTextField locXField;
    private JTextField locYField;
    private JTextField locZField;
    private JTextField locNameField;

    private JLabel idErrorLabel;

    private JLabel nameErrorLabel;
    private JLabel xErrorLabel;
    private JLabel yErrorLabel;
    private JLabel priceErrorLabel;
    private JLabel manufactureCostErrorLabel;

    private JLabel ownerNameErrorLabel;
    private JLabel heightErrorLabel;

    private JLabel locXErrorLabel;
    private JLabel locYErrorLabel;
    private JLabel locZErrorLabel;
    private JLabel locNameErrorLabel;

    public void setProductId(String productId) {
        if (idField != null) {
            idField.setText(productId);
            idField.setEditable(false);
            idField.setFocusable(false);
        }
    }

    public ProductDialog(Mode mode) {
        super(null, mode.getTitle(), ModalityType.APPLICATION_MODAL);
        this.mode = mode;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JComponent content = createContent();
        setContentPane(content);

        pack();
        setMinimumSize(new Dimension(900, mode == Mode.UPDATE ? 760 : 700));
        setLocationRelativeTo(null);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getServerCommand() {
        return mode.getServerCommand();
    }

    public String getIdValue() {
        return idField == null ? null : idField.getText().trim();
    }

    public String getNameValue() {
        return nameField.getText().trim();
    }

    public Coordinates getCoordinatesValue() {
        return new Coordinates(
                Integer.parseInt(xField.getText()),
                Double.parseDouble(yField.getText())
                );
    }


    public Double getPriceValue() {
        return priceField.getText().isBlank() ? null : Double.parseDouble(priceField.getText().trim());
    }

    public Integer getManufactureCostValue() {
        return Integer.parseInt(manufactureCostField.getText().trim());
    }

    public UnitOfMeasure getUnitOfMeasureValue() {
        if(!getSelectedValue(unitOfMeasureBox).isBlank())
            return UnitOfMeasure.valueOf(getSelectedValue(unitOfMeasureBox));
        return null;
    }

    public Person getPersonValue() {
        if(ownerNameField.getText().isBlank())
            return null;

        return new Person(
                ownerNameField.getText(),
                Float.parseFloat(heightField.getText()),
                getSelectedValue(eyeColorBox).isBlank() ? null : EyeColor.valueOf(getSelectedValue(eyeColorBox)),
                HairColor.valueOf(getSelectedValue(hairColorBox)),
                Country.valueOf(getSelectedValue(countryBox)),
                getLocationValue()
        );
    }

    public Location getLocationValue() {
        if(locXField.getText().isBlank()) return null;

        return new Location(
                Double.parseDouble(locXField.getText()),
                Integer.parseInt(locYField.getText()),
                Double.parseDouble(locZField.getText()),
                locNameField.getText()
        );
    }



    private String getSelectedValue(JComboBox<String> box) {
        Object selected = box.getSelectedItem();

        if (selected == null) {
            return "";
        }

        return selected.toString().trim();
    }

    private JComponent createContent() {
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(true);
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(24, 28, 24, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;

        int row = 0;

        if (mode == Mode.UPDATE) {
            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 3;
            gbc.insets = new Insets(0, 0, 22, 0);
            content.add(createSectionHeader("Update Info"), gbc);

            idField = createTextField();

            gbc.gridx = 0;
            gbc.gridy = row++;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 12, 22, 12);
            content.add(createFieldBlock("Product ID", idField), gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 22, 0);
        content.add(createSectionHeader("Product Info"), gbc);

        nameField = createTextField();
        xField = createTextField();
        yField = createTextField();
        priceField = createTextField();
        manufactureCostField = createTextField();

        nameErrorLabel = createErrorLabel();
        xErrorLabel = createErrorLabel();
        yErrorLabel = createErrorLabel();
        priceErrorLabel = createErrorLabel();
        manufactureCostErrorLabel = createErrorLabel();

        unitOfMeasureBox = new CustomComboBox(new String[]{
                " ",
                UnitOfMeasure.KILOGRAMS.name(),
                UnitOfMeasure.METERS.name(),
                UnitOfMeasure.LITERS.name(),
                UnitOfMeasure.MILLILITERS.name()
        });

        gbc.gridwidth = 1;
        gbc.gridy = row;
        gbc.insets = new Insets(0, 12, 22, 12);

        gbc.gridx = 0;
        content.add(createFieldBlock("Name", nameField, nameErrorLabel), gbc);

        gbc.gridx = 1;
        content.add(createFieldBlock("X", xField, xErrorLabel), gbc);

        gbc.gridx = 2;
        content.add(createFieldBlock("Y", yField, yErrorLabel), gbc);

        row++;

        gbc.gridy = row;

        gbc.gridx = 0;
        content.add(createFieldBlock("Price", priceField, priceErrorLabel), gbc);

        gbc.gridx = 1;
        content.add(createFieldBlock("Manufacture Cost", manufactureCostField, manufactureCostErrorLabel), gbc);

        gbc.gridx = 2;
        content.add(createFieldBlock("Unit of Measure", unitOfMeasureBox), gbc);

        row++;

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(12, 0, 22, 0);
        content.add(createSectionHeader("Owner Info"), gbc);

        ownerNameField = createTextField();

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 12, 6, 12);
        content.add(createFieldBlock("Owner Name", ownerNameField), gbc);

        JLabel ownerHint = createHintLabel(
                "Owner details will appear after entering owner name. You may leave it blank."
        );

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 12, 14, 12);
        content.add(ownerHint, gbc);

        ownerDetailsPanel = createOwnerDetailsPanel();

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        content.add(ownerDetailsPanel, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);

        JButton submitButton = createPrimaryButton(mode.getButtonText());

        submitButton.addActionListener(this::submit);

        buttons.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(8, 12, 0, 12);
        content.add(buttons, gbc);

        getRootPane().setDefaultButton(submitButton);

        setupDynamicVisibility();

        return content;
    }

    private JPanel createOwnerDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 12, 16, 12);

        heightField = createTextField();

        heightErrorLabel = createErrorLabel();
        locXErrorLabel = createErrorLabel();

        eyeColorBox = new CustomComboBox(new String[]{
                " ",
                EyeColor.RED.name(),
                EyeColor.GREEN.name(),
                EyeColor.ORANGE.name(),
        });

        hairColorBox = new CustomComboBox(new String[]{
                HairColor.GREEN.name(),
                HairColor.BLACK.name(),
                HairColor.WHITE.name()
        });

        countryBox = new CustomComboBox(new String[]{
                Country.USA.name(),
                Country.VATICAN.name(),
                Country.THAILAND.name()
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(createFieldBlock("Height", heightField, heightErrorLabel), gbc);

        gbc.gridx = 1;
        panel.add(createFieldBlock("Eye Color", eyeColorBox), gbc);

        gbc.gridx = 2;
        panel.add(createFieldBlock("Hair Color", hairColorBox), gbc);

        gbc.gridx = 3;
        panel.add(createFieldBlock("Country", countryBox), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(12, 0, 22, 0);
        panel.add(createSectionHeader("Owner Location"), gbc);

        locXField = createTextField();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 12, 16, 12);
        panel.add(createFieldBlock("Location X", locXField,locXErrorLabel), gbc);

        JLabel locationHint = createHintLabel(
                "Location details will appear after entering X. You may leave it blank."
        );

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 12, 14, 12);
        panel.add(locationHint, gbc);

        locationDetailsPanel = createLocationDetailsPanel();

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(locationDetailsPanel, gbc);

        return panel;
    }

    private JPanel createLocationDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 12, 8, 12);

        locYField = createTextField();
        locZField = createTextField();
        locNameField = createTextField();

        locYErrorLabel = createErrorLabel();
        locZErrorLabel = createErrorLabel();
        locNameErrorLabel = createErrorLabel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createFieldBlock("Location Y", locYField, locYErrorLabel), gbc);

        gbc.gridx = 1;
        panel.add(createFieldBlock("Location Z", locZField, locZErrorLabel), gbc);

        gbc.gridx = 2;
        panel.add(createFieldBlock("Location Name", locNameField, locNameErrorLabel), gbc);

        return panel;
    }

    private void clearValidationErrors() {
        if (idErrorLabel != null) {
            idErrorLabel.setText(" ");
        }

        nameErrorLabel.setText(" ");
        xErrorLabel.setText(" ");
        yErrorLabel.setText(" ");
        priceErrorLabel.setText(" ");
        manufactureCostErrorLabel.setText(" ");


        if (heightErrorLabel != null) {
            heightErrorLabel.setText(" ");
        }

        if (locXErrorLabel != null) {
            locXErrorLabel.setText(" ");
        }

        if (locYErrorLabel != null) {
            locYErrorLabel.setText(" ");
        }

        if (locZErrorLabel != null) {
            locZErrorLabel.setText(" ");
        }

        if (locNameErrorLabel != null) {
            locNameErrorLabel.setText(" ");
        }
    }

    private void setupDynamicVisibility() {
        ownerDetailsPanel.setVisible(false);
        locationDetailsPanel.setVisible(false);

        ownerNameField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                boolean hasOwner = !ownerNameField.getText().trim().isEmpty();

                ownerDetailsPanel.setVisible(hasOwner);

                repackDialog();
            }
        });

        locXField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update() {
                boolean hasOwner = !ownerNameField.getText().trim().isEmpty();
                boolean hasLocationX = !locXField.getText().trim().isEmpty();

                locationDetailsPanel.setVisible(hasOwner && hasLocationX);

                repackDialog();
            }
        });
    }

    private void repackDialog() {
        pack();
        setMinimumSize(new Dimension(900, mode == Mode.UPDATE ? 760 : 700));
        setLocationRelativeTo(getOwner());
    }

    private void submit(ActionEvent e) {
        if (!validateProductFields()) {
            return;
        }

        confirmed = true;
        dispose();
    }

    private boolean validateProductFields() {
        clearValidationErrors();

        boolean isValid = true;

        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        PriceValidator priceValidator = new PriceValidator();

        if (nameField.getText().trim().isEmpty()) {
            nameErrorLabel.setText("Name is required.");
            isValid = false;
        }

        if (!integerValidator.isValid(xField.getText().trim(), false)) {
            xErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (!doubleValidator.isValid(yField.getText().trim(), false)) {
            yErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (!yField.getText().isEmpty() && Double.parseDouble(yField.getText().trim()) <= -990) {
            yErrorLabel.setText("Y must be > -990.");
            isValid = false;
        }

        if (!priceValidator.isValid(priceField.getText().trim(), true)) {
            priceErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (!integerValidator.isValid(manufactureCostField.getText().trim(),false)) {
            manufactureCostErrorLabel.setText("Manufacture cost is required.");
            isValid = false;
        }

        if(!ownerNameField.getText().isBlank())
            isValid=validatePersonFields();

        return isValid;
    }

    private boolean validatePersonFields(){
        boolean isValid = true;

        HeightValidator heightValidator = new HeightValidator();

        if (!heightValidator.isValid(heightField.getText(),false)) {
            heightErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if(!locXField.getText().isBlank())
            isValid=validateLocationFields();

        return isValid;
    }

    private boolean validateLocationFields(){
        boolean isValid = true;

        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();

        if (!doubleValidator.isValid(locXField.getText(),false)) {
            locXErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (!integerValidator.isValid(locYField.getText(),false)) {
            locYErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (!doubleValidator.isValid(locZField.getText(),false)) {
            locZErrorLabel.setText(ErrorMessageDeliverer.poll());
            isValid = false;
        }

        if (locNameField.getText().isBlank()) {
           locNameErrorLabel.setText("Field is required");
           isValid=false;
        }

        if (locNameField.getText().length()>479) {
            locNameErrorLabel.setText("Length should be < 379");
            isValid=false;
        }

        return isValid;
    }

    private JPanel createFieldBlock(String labelText, JComponent input) {
        return createFieldBlock(labelText, input, null);
    }

    private JPanel createFieldBlock(String labelText, JComponent input, JLabel errorLabel) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(App.TEXT_PURPLE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension inputSize = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        input.setPreferredSize(inputSize);
        input.setMinimumSize(inputSize);
        input.setMaximumSize(inputSize);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(6));
        panel.add(input);

        if (errorLabel != null) {
            panel.add(Box.createVerticalStrut(4));
            panel.add(errorLabel);
        }

        return panel;
    }

    private JComponent createSectionHeader(String title) {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(title);
        label.setForeground(App.TEXT_PURPLE);
        label.setFont(new Font("Arial", Font.PLAIN, 26));

        JSeparator separator = new JSeparator();
        separator.setForeground(App.TEXT_PURPLE);
        separator.setBackground(App.TEXT_PURPLE);

        JPanel separatorWrapper = new JPanel(new GridBagLayout());
        separatorWrapper.setOpaque(false);
        separatorWrapper.setBorder(new EmptyBorder(5, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        separatorWrapper.add(separator, gbc);

        panel.add(label, BorderLayout.WEST);
        panel.add(separatorWrapper, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createHintLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.ITALIC, 16));
        label.setForeground(new Color(170, 170, 170));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 19));
        field.setForeground(App.TEXT_PURPLE);
        field.setCaretColor(App.TEXT_PURPLE);
        field.setBackground(Color.WHITE);

        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(App.TEXT_PURPLE, 1, 16),
                new EmptyBorder(0, 14, 0, 14)
        ));

        Dimension size = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
        field.setPreferredSize(size);
        field.setMinimumSize(size);
        field.setMaximumSize(size);

        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new RoundedButton(
                text,
                18,
                App.TEXT_PURPLE,
                Color.WHITE,
                null
        );

        button.setPreferredSize(new Dimension(190, 54));
        return button;
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.RED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        Dimension size = new Dimension(FIELD_WIDTH, ERROR_HEIGHT);
        label.setPreferredSize(size);
        label.setMinimumSize(size);
        label.setMaximumSize(size);

        return label;
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {
        public abstract void update();

        @Override
        public void insertUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update();
        }
    }
}