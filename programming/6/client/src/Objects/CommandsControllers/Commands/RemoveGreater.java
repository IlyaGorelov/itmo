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
        System.out.println("Type element to compare. Type new values.");
        var stringValidator = new StringValidator();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        PriceValidator priceValidator = new PriceValidator();
        IntegerValidator integerValidator = new IntegerValidator();
        UnitValidator unitValidator = new UnitValidator();
        HeightValidator heightValidator = new HeightValidator();
        EyeValidator eyeValidator = new EyeValidator();
        HairValidator hairValidator = new HairValidator();
        CountryValidator countryValidator = new CountryValidator();
        LocationValidator locationValidator = new LocationValidator();

        String name = stringValidator.get(getScanner(), false, "Enter product name: ");
        Coordinates coordinates = coordinatesValidator.get(getScanner(), false, "Enter coordinates:");
        Double price = priceValidator.get(getScanner(), true, "Enter price(double): ");
        Integer manufactureCost = integerValidator.get(getScanner(), false, "Enter manufacture cost(integer): ");
        UnitOfMeasure unitOfMeasure = unitValidator.get(getScanner(), true, "Choose unit of measure: ");

        String ownerName = stringValidator.get(getScanner(), true, "Enter owner's name: ");
        if (ownerName != null) {
            Float height = heightValidator.get(getScanner(), false, "Enter owner's height: ");
            EyeColor eyeColor = eyeValidator.get(getScanner(), true, "Choose eye color: ");
            HairColor hairColor = hairValidator.get(getScanner(), false, "Choose hair color: ");
            Country country = countryValidator.get(getScanner(), false, "Choose nationality: ");
            Location location = locationValidator.get(getScanner(), true, "Enter location: ");
            Product p = new Product(0, name, coordinates, new Date(), price, manufactureCost, unitOfMeasure,
                    new Person(ownerName, height, eyeColor, hairColor, country, location));
            return p;
        } else {
            Product p = new Product(0, name, coordinates, new Date(), price, manufactureCost, unitOfMeasure,
                    null);
            return p;
        }
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
