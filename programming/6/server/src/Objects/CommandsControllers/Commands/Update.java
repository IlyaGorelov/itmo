package Objects.CommandsControllers.Commands;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.*;

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
        // checkArgument();
        // try {
        // Long id = Long.parseLong(getArgument());
        // IdValidator idValidator = new IdValidator();
        // if (!idValidator.isValid(getArgument(), false))
        // throw new IllegalArgumentException();

        // System.out.println("Updating an element with id " + id + ". Type new
        // values");
        // var stringValidator = new StringValidator();
        // CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        // PriceValidator priceValidator = new PriceValidator();
        // IntegerValidator integerValidator = new IntegerValidator();
        // UnitValidator unitValidator = new UnitValidator();
        // HeightValidator heightValidator = new HeightValidator();
        // EyeValidator eyeValidator = new EyeValidator();
        // HairValidator hairValidator = new HairValidator();
        // CountryValidator countryValidator = new CountryValidator();
        // LocationValidator locationValidator = new LocationValidator();

        // String name = stringValidator.get(getReceiver(), false, "Enter product name:
        // ");
        // System.out.println(name);
        // Coordinates coordinates = coordinatesValidator.get(getReceiver(), false,
        // "Enter coordinates:");
        // Double price = priceValidator.get(getReceiver(), true, "Enter price(double)
        // or type nothing: ");
        // Integer manufactureCost = integerValidator.get(getReceiver(), false, "Enter
        // manufacture cost(integer): ");
        // UnitOfMeasure unitOfMeasure = unitValidator.get(getReceiver(), true,
        // "Choose unit of measure or type nothing: ");

        // String ownerName = stringValidator.get(getReceiver(), true, "Enter owner's
        // name or type nothing: ");
        // if (ownerName != null) {
        // Float height = heightValidator.get(getReceiver(), false, "Enter owner's
        // height: ");
        // EyeColor eyeColor = eyeValidator.get(getReceiver(), true, "Choose eye color
        // or type nothing: ");
        // HairColor hairColor = hairValidator.get(getReceiver(), false, "Choose hair
        // color: ");
        // Country country = countryValidator.get(getReceiver(), false, "Choose
        // nationality: ");
        // Location location = locationValidator.get(getReceiver(), true, "Enter
        // location: ");
        // getCollectionManager().updateElement(id, name, coordinates, price,
        // manufactureCost, unitOfMeasure,
        // new Person(ownerName, height, eyeColor, hairColor, country, location));
        // } else {
        // getCollectionManager().updateElement(id, name, coordinates, price,
        // manufactureCost, unitOfMeasure,
        // null);
        // }
        // System.out.println("Successfully updated");
        // } catch (IllegalArgumentException e) {
        // throw new IllegalArgumentException(e.getMessage());
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }

    }

    @Override
    public void executeFromScript(String complexArg) {
        String[] tokens = complexArg.replace("{", "").replace("}", "").replace(";", " ; ").split(";");
        int tokenCounter = 0;

        StringValidator stringValidator = new StringValidator();
        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        PriceValidator priceValidator = new PriceValidator();
        UnitValidator unitValidator = new UnitValidator();
        HeightValidator heightValidator = new HeightValidator();
        EyeValidator eyeValidator = new EyeValidator();
        HairValidator hairValidator = new HairValidator();
        CountryValidator countryValidator = new CountryValidator();
        LocationValidator locationValidator = new LocationValidator();

        Product newProduct = null;

        try {
            Long id = Long.parseLong(getArgument());
            IdValidator idValidator = new IdValidator();
            if (!idValidator.isValid(getArgument(), false))
                throw new IllegalArgumentException();

            String name = tokens[tokenCounter++].trim();
            if (!stringValidator.isValid(String.valueOf(name), false))
                throw new IllegalArgumentException("Product name can't be null");

            String x = tokens[tokenCounter++].trim();
            if (!integerValidator.isValid(String.valueOf(x), false))
                throw new IllegalArgumentException("Invalid value for x");

            String y = tokens[tokenCounter++].trim();
            if (!doubleValidator.isValid(String.valueOf(y), false))
                throw new IllegalArgumentException("Invalid value for y");

            Coordinates coordinates = new Coordinates(Integer.parseInt(x),
                    Double.parseDouble(y));
            if (!coordinatesValidator.isValid(String.valueOf(coordinates), false))
                throw new IllegalArgumentException("Invalid value for coordinates");

            String price = tokens[tokenCounter++].trim();
            if (!priceValidator.isValid(String.valueOf(price), true))
                throw new IllegalArgumentException("Invalid value for price");

            String manufactureCost = tokens[tokenCounter++].trim();
            if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                throw new IllegalArgumentException("Invalid value for manufacture cost");

            String unitOfMeasure = tokens[tokenCounter++].trim();
            if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true))
                throw new IllegalArgumentException(
                        "Invalid value for unit of measure. Should be one of KILOGRAMS, METERS, LITERS, MILLILITERS");

            String ownerName = tokens[tokenCounter++].trim();
            if (ownerName.isBlank())
                newProduct = getCollectionManager().updateElement(id, name, coordinates,
                        !price.isBlank() ? Double.parseDouble(price) : null,
                        Integer.parseInt(manufactureCost),
                        !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                        null);
            else {
                String height = tokens[tokenCounter++].trim();
                if (!heightValidator.isValid(String.valueOf(height), false))
                    throw new IllegalArgumentException("Invalid value for height");

                String eyeColor = tokens[tokenCounter++].trim();
                if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                    throw new IllegalArgumentException(
                            "Invalid value for eye color. Should be one of GREEN,RED,ORANGE");

                String hairColor = tokens[tokenCounter++].trim();
                if (!hairValidator.isValid(String.valueOf(hairColor), false))
                    throw new IllegalArgumentException(
                            "Invalid value for hair color. Should be one of GREEN, BLACK, WHITE");

                String nationality = tokens[tokenCounter++].trim();
                if (!countryValidator.isValid(String.valueOf(nationality), false))
                    throw new IllegalArgumentException(
                            "Invalid value for country. Should be one of USA, VATICAN, THAILAND");

                String locX = tokens[tokenCounter++].trim();
                Location location = null;
                if (!locX.isBlank()) {
                    if (!doubleValidator.isValid(String.valueOf(locX), false))
                        throw new IllegalArgumentException("Invalid value for locX");

                    String locY = tokens[tokenCounter++].trim();
                    String locZ = tokens[tokenCounter++].trim();
                    String locName = tokens[tokenCounter++].trim();

                    if (!integerValidator.isValid(String.valueOf(locY), false))
                        throw new IllegalArgumentException("Invalid value for locY");

                    if (!doubleValidator.isValid(String.valueOf(locZ), false))
                        throw new IllegalArgumentException("Invalid value for locZ");

                    if (!stringValidator.isValid(String.valueOf(locName), false))
                        throw new IllegalArgumentException("Invalid value for locName");

                    location = new Location(Double.parseDouble(locX),
                            Integer.parseInt(locY),
                            Double.parseDouble(locZ),
                            locName);

                    if (!locationValidator.isValid(String.valueOf(location), false))
                        throw new IllegalArgumentException("Invalid value for location");
                }

                newProduct = getCollectionManager().updateElement(id, name, coordinates,
                        !price.isBlank() ? Double.parseDouble(price) : null,
                        Integer.parseInt(manufactureCost),
                        !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                        new Person(ownerName, Float.parseFloat(height),
                                !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                HairColor.valueOf(hairColor.toUpperCase()),
                                Country.valueOf(nationality.toUpperCase()),
                                location));
            }
            System.out.println("Successfully updated " + name);
            getReceiver().addToAnswer(this, getArgument(), newProduct);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            System.out.println("Skip\n");
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
