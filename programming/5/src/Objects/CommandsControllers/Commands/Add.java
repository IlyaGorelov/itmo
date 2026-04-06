package Objects.CommandsControllers.Commands;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.Revertable;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Managers.IdManager;
import Objects.Validators.*;

/** Adds an element to the collection */
public class Add extends Command implements Revertable {
    public Add(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Add(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /** Asks for required fields then creates new element - Product */
    @Override
    public void execute() {
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
            getCollectionManager().addElement(name, coordinates, price, manufactureCost, unitOfMeasure,
                    new Person(ownerName, height, eyeColor, hairColor, country, location));
        } else {
            getCollectionManager().addElement(name, coordinates, price, manufactureCost, unitOfMeasure,
                    null);
        }
        System.out.println("Successfully added");
    }

    @Override
    public void executeFromScript(String complexArg) {
        // add id input with spec sym $
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

        try {
            String id = null;
            if (tokens[tokenCounter].trim().startsWith("$")) {
                id = tokens[tokenCounter++].trim().replace("$", "");
                if (Long.parseLong(id) < 0)
                    throw new IllegalArgumentException("Id must be non negative");
                else if (IdManager.isIdIn(Long.parseLong(id)))
                    throw new IllegalArgumentException("Id is already taken");
            }

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
                        "Invalid value for unit of measure. Should be one of KILOGRAMS, LITERS, METERS, MILLILITERS");

            String ownerName = tokens[tokenCounter++].trim();
            if (ownerName.isBlank())
                if (id == null)
                    getCollectionManager().addElement(name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            null);
                else
                    getCollectionManager().addElement(Long.parseLong(id), name, coordinates,
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
                            "Invalid value for eye color. Should be one of GREEN, RED, ORANGE");

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

                if (id == null)
                    getCollectionManager().addElement(name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            new Person(ownerName, Float.parseFloat(height),
                                    !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                    HairColor.valueOf(hairColor.toUpperCase()),
                                    Country.valueOf(nationality.toUpperCase()),
                                    location));
                else
                    getCollectionManager().addElement(Long.parseLong(id), name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            new Person(ownerName, Float.parseFloat(height),
                                    !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                    HairColor.valueOf(hairColor.toUpperCase()),
                                    Country.valueOf(nationality.toUpperCase()),
                                    location));
            }
            System.out.println("Successfully added " + name);

        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add new element";
    }

    @Override
    public void Undo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Undo'");
    }

}
