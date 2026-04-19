package Objects.CommandsControllers.Commands;

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

    public AddIfMin(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
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
    }

    private void addIfMin(String name, Coordinates coordinates, Double price, Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, Person person) {
        Product newProduct = null;
        if (getCollectionManager().isMin(name, coordinates, price, manufactureCost, unitOfMeasure, person)) {
            newProduct = getCollectionManager().addElement(name, coordinates, price, manufactureCost, unitOfMeasure,
                    person);
            // System.out.println("Successfully added " + name);

            CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct);
            answer(pkg,"Successfully added");
        } else {

            CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct);
            answer(pkg,"Not added");
        }
    }

    @Override
    public void executeInline() {
        String complexArg = getComplexArgument();
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
                answer(null,e.getMessage());
        }

    }

    @Override
    public  void checkArgument(){
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tadd_if_min {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
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
