package Objects.CommandsControllers.Commands;

import java.util.ArrayList;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.*;

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

    }

    @Override
    public void executeFromScript(String complexArg) {
        String[] tokens = complexArg.replace("{", "").replace("}", "").replace(";", " ; ").split(";");

        IntegerValidator integerValidator = new IntegerValidator();
        PriceValidator priceValidator = new PriceValidator();

        Product[] greaters = null;

        try {
            String price = tokens[3].trim();
            if (!priceValidator.isValid(String.valueOf(price), true))
                throw new IllegalArgumentException("Invalid value for price");

            String manufactureCost = tokens[4].trim();
            if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                throw new IllegalArgumentException("Invalid value for manufacture cost");

            greaters = getCollectionManager().removeGreaters(
                    !price.isBlank() ? Double.parseDouble(price) : null,
                    Integer.parseInt(manufactureCost));

            CustomPackage pkg = new CustomPackage(this.getName(), null, greaters);
            getReceiver().addToAnswer(getCLient(), pkg);

        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
                CustomPackage pkg = new CustomPackage(this.getName(), null, e);
                getReceiver().addToAnswer(getCLient(), pkg);
            }
            // System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove elements greater than input ";
    }

}
