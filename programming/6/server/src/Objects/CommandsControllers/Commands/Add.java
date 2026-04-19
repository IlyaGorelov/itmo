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
import Objects.Managers.IdManager;
import Objects.Validators.*;

/** Adds an element to the collection */
public class Add extends Command {
    public Add(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public Add(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /** Asks for required fields then creates new element - Product */
    @Override
    public void execute() {
        checkArgument();
    }

    @Override
    public void executeInline() {
        // add id input with spec sym $
        checkArgument();
        String complexArg = getComplexArgument();
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
            Product newProduct = null;
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
                    newProduct = getCollectionManager().addElement(name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            null);
                else
                    newProduct = getCollectionManager().addElement(Long.parseLong(id), name, coordinates,
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
                    newProduct = getCollectionManager().addElement(name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            new Person(ownerName, Float.parseFloat(height),
                                    !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                    HairColor.valueOf(hairColor.toUpperCase()),
                                    Country.valueOf(nationality.toUpperCase()),
                                    location));
                else
                    newProduct = getCollectionManager().addElement(Long.parseLong(id), name, coordinates,
                            !price.isBlank() ? Double.parseDouble(price) : null,
                            Integer.parseInt(manufactureCost),
                            !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                            new Person(ownerName, Float.parseFloat(height),
                                    !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                    HairColor.valueOf(hairColor.toUpperCase()),
                                    Country.valueOf(nationality.toUpperCase()),
                                    location));
            }
            // System.out.println("Successfully added " + name);

            CustomPackage pkg = new CustomPackage(this.getName(), null, newProduct);

            answer(pkg,"Successfully added " + name);
        } catch (IndexOutOfBoundsException e){
                CustomPackage pkg = new CustomPackage(this.getName(), null, "Invalid number of arguments!");
                answer(pkg,"Invalid number of arguments!");
        }
        catch (Exception e) {
            if (e.getMessage() != null) {
                CustomPackage pkg = new CustomPackage(this.getName(), null, e);
                answer(pkg,e.getMessage());
            }
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
    public  void checkArgument(){
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
    }

}
