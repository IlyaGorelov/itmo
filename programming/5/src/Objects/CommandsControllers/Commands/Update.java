package Objects.CommandsControllers.Commands;

import Objects.Collection.Coordinates;
import Objects.CommandsControllers.Command;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IdValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.StringValidator;
import Objects.Validators.UnitValidator;

/** update an element */
public class Update extends Command {
    public Update(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Update(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() throws IndexOutOfBoundsException {
        checkArgument();
        try {
            Long id = Long.parseLong(getArgument());
            IdValidator idValidator = new IdValidator();
            if (!idValidator.validate(getArgument(), false))
                throw new IllegalArgumentException("ID mismatch");

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

            getCollectionManager().updateElement(id, name, coordinates, price, manufactureCost, unitOfMeasure,
                    ownerName);
            System.out.println("Successfully updated");
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update an element";
    }

}
