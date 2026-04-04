package Objects.CommandsControllers.Commands;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.CommandsControllers.Command;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.CountryValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.EyeValidator;
import Objects.Validators.HairValidator;
import Objects.Validators.HeightValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.LocationValidator;
import Objects.Validators.PriceValidator;
import Objects.Validators.StringValidator;
import Objects.Validators.UnitValidator;

/** Adds element to a collection if this element gonna be max */
public class AddIfMin extends Command {

    public AddIfMin(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public AddIfMin(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Asks for required fields then creates new element - Product, then put it into
     * collection if it's gonna be min
     */
    @Override
    public void execute() {
        // checkArgument();
        // System.out.println("Adding new element. Type new values.");
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
        // addIfMin(name, coordinates, price, manufactureCost, unitOfMeasure,
        // new Person(ownerName, height, eyeColor, hairColor, country, location));
        // } else {
        // addIfMin(ownerName, coordinates, price, manufactureCost, unitOfMeasure,
        // new Person(ownerName));
        // }
    }

    private void addIfMin(String name, Coordinates coordinates, Double price, Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, Person person) {
        if (getCollectionManager().isMin(name, coordinates, price, manufactureCost, unitOfMeasure, person)) {
            getCollectionManager().addElement(name, coordinates, price, manufactureCost, unitOfMeasure, person);
            System.out.println("Successfully added " + name);
        } else {
            System.out.println("Not added because not Max");
        }
    }

    @Override
    public void executeFromScript(String complexArg) {
        String[] tokens = complexArg.replace("{", "").replace("}", "").replace(";", " ; ").split(";");

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
            String name = tokens[0].trim();
            if (!stringValidator.isValid(String.valueOf(name), false))
                throw new IllegalArgumentException("Product name can't be null");

            String x = tokens[1].trim();
            if (!integerValidator.isValid(String.valueOf(x), false))
                throw new IllegalArgumentException("Invalid value for x");

            String y = tokens[2].trim();
            if (!doubleValidator.isValid(String.valueOf(y), false))
                throw new IllegalArgumentException("Invalid value for y");

            Coordinates coordinates = new Coordinates(Integer.parseInt(x),
                    Double.parseDouble(y));
            if (!coordinatesValidator.isValid(String.valueOf(coordinates), false))
                throw new IllegalArgumentException("Invalid value for coordinates");

            String price = tokens[3].trim();
            if (!priceValidator.isValid(String.valueOf(price), true))
                throw new IllegalArgumentException("Invalid value for price");

            String manufactureCost = tokens[4].trim();
            if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                throw new IllegalArgumentException("Invalid value for manufacture cost");

            String unitOfMeasure = tokens[5].trim();
            if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true))
                throw new IllegalArgumentException(
                        "Invalid value for unit of measure. Should be one of KILOGRAMS, METERS, LITERS, MILLILITERS");

            String ownerName = tokens[6].trim();
            if (ownerName.isBlank())
                addIfMin(name, coordinates, !price.isBlank() ? Double.parseDouble(price) : null,
                        Integer.parseInt(manufactureCost),
                        !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                        null);
            else {
                String height = tokens[7].trim();
                if (!heightValidator.isValid(String.valueOf(height), false))
                    throw new IllegalArgumentException("Invalid value for height");

                String eyeColor = tokens[8].trim();
                if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                    throw new IllegalArgumentException(
                            "Invalid value for eye color. Should be one of GREEN, RED, ORANGE");

                String hairColor = tokens[9].trim();
                if (!hairValidator.isValid(String.valueOf(hairColor), false))
                    throw new IllegalArgumentException(
                            "Invalid value for hair color. Should be one of GREEN, BLACK, WHITE");

                String nationality = tokens[10].trim();
                if (!countryValidator.isValid(String.valueOf(nationality), false))
                    throw new IllegalArgumentException(
                            "Invalid value for country. Should be one of USA, VATICAN, THAILAND");

                String locX = tokens[11].trim();
                Location location = null;
                if (!locX.isBlank()) {
                    if (!doubleValidator.isValid(String.valueOf(locX), false))
                        throw new IllegalArgumentException("Invalid value for locX");

                    String locY = tokens[12].trim();
                    String locZ = tokens[13].trim();
                    String locName = tokens[14].trim();

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

                addIfMin(name, coordinates,
                        !price.isBlank() ? Double.parseDouble(price) : null,
                        Integer.parseInt(manufactureCost),
                        !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                        new Person(ownerName, Float.parseFloat(height),
                                !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                HairColor.valueOf(hairColor.toUpperCase()),
                                Country.valueOf(nationality.toUpperCase()),
                                location));
            }
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "add new element if the new is smaller than the min of collection";
    }

}
