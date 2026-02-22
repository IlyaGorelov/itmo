package Objects.CommandsControllers.Commands;

import java.io.InputStream;
import java.util.ArrayList;

import Objects.Collection.Coordinates;
import Objects.CommandsControllers.Command;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.StringValidator;
import Objects.Validators.UnitValidator;

/*remove elements greater than input */
public class RemoveGreater extends Command {
    public RemoveGreater(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public RemoveGreater(CollectionManager collectionManager) {
        super(collectionManager);
    }

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

        ArrayList<Long> ids = getCollectionManager().getGreaterIds(name, coordinates, price, manufactureCost,
                unitOfMeasure, ownerName);

        for (Long id : ids) {
            getCollectionManager().deleteById(id);
            System.out.println("Successfully deleted");
        }
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove elements greater than input";
    }

}
