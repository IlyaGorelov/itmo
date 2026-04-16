package Objects.CommandsControllers.Commands;

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

/** Adds an element to the collection */
public class Add extends Command {

    public Add(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        System.out.println("Adding new element. Type new values.");
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
        Double price = priceValidator.get(getScanner(), true, "Enter price(double) or type nothing: ");
        Integer manufactureCost = integerValidator.get(getScanner(), false, "Enter manufacture cost(integer): ");
        UnitOfMeasure unitOfMeasure = unitValidator.get(getScanner(), true, "Choose unit of measure or type nothing: ");

        String ownerName = stringValidator.get(getScanner(), true, "Enter owner's name or type nothing: ");
        if (ownerName != null) {
            Float height = heightValidator.get(getScanner(), false, "Enter owner's height: ");
            EyeColor eyeColor = eyeValidator.get(getScanner(), true, "Choose eye color or type nothing: ");
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
    public String getName() {
        return "add";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object instanceof Product)
            return "Element with name \"" + ((Product) object).getName() + "\" was succesfully added" + "\n";
        else
            return object.toString() + "\n";
    }

}
