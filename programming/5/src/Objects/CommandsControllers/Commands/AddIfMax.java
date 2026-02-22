package Objects.CommandsControllers.Commands;

import Objects.Collection.Coordinates;
import Objects.CommandsControllers.Command;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.StringValidator;
import Objects.Validators.UnitValidator;

/** Adds element to a collection if this element gonna be max */
public class AddIfMax extends Command {
    public AddIfMax(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public AddIfMax(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Asks for required fields then creates new element - Product, then put it into
     * collection if it's gonna be max
     */
    @Override
    public void execute() {
        checkArgument();
        var stringValidator = new StringValidator();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        IntegerValidator integerValidator = new IntegerValidator();
        UnitValidator unitValidator = new UnitValidator();

        String name = stringValidator.get(getScanner(), false, "Enter product name: ");
        Coordinates coordinates = coordinatesValidator.get(getScanner(), false, "Enter coordinates:");
        Double price = doubleValidator.get(getScanner(), true, "Enter price(double): ");
        Integer manufactureCost = integerValidator.get(getScanner(), false, "Enter manufacture cost(integer): ");
        UnitOfMeasure unitOfMeasure = unitValidator.get(getScanner(), true, "Choose unit of measure: ");
        String ownerName = stringValidator.get(getScanner(), true, "Enter owner name: ");

        if (getCollectionManager().isMax(name, coordinates, price, manufactureCost, unitOfMeasure, ownerName)) {
            getCollectionManager().addElement(name, coordinates, price, manufactureCost, unitOfMeasure, ownerName);
            System.out.println("Successfully added");
        } else {
            System.out.println("Not added because not Max");
        }
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getDescription() {
        return "add new element if the new is bigger than the max of collection";
    }

}
