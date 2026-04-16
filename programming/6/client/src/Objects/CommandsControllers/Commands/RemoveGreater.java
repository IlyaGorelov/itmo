package Objects.CommandsControllers.Commands;

import java.util.ArrayList;
import java.util.Date;

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
import Objects.Validators.*;

/*remove elements greater than input */
public class RemoveGreater extends Command {
    public RemoveGreater(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    public RemoveGreater() {
        super();
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        System.out.println("Type values to compare with.");
        PriceValidator priceValidator = new PriceValidator();
        IntegerValidator integerValidator = new IntegerValidator();

        Double price = priceValidator.get(getScanner(), true, "Enter price(double): ");
        Integer manufactureCost = integerValidator.get(getScanner(), false, "Enter manufacture cost(integer): ");

        Product p = new Product(0, "", new Coordinates(0, 0), new Date(), price, manufactureCost, null,
                null);
        return p;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object[] products = (Object[]) pack.getObject();
        String relevant = "";
        if (products.length == 0)
            return "There are no elements greater than input\n";

        for (Object product : products) {
            relevant += "Element with id " + ((Product) product).getId() + " was removed\n";
        }
        return relevant;
    }

}
