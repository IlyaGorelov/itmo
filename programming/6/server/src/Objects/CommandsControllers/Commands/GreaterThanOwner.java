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

/** get all elements where owner is biger than input one */
public class GreaterThanOwner extends Command {
    public GreaterThanOwner(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public GreaterThanOwner(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        // StringValidator stringValidator = new StringValidator();
        // HeightValidator heightValidator = new HeightValidator();
        // EyeValidator eyeValidator = new EyeValidator();
        // HairValidator hairValidator = new HairValidator();
        // CountryValidator countryValidator = new CountryValidator();
        // LocationValidator locationValidator = new LocationValidator();
        // ArrayList<Long> ids = new ArrayList<>();

        // checkArgument();

        // System.out.println("Input an owner you want to compare with (you can also
        // type nothing):\n");

        // String ownerName = stringValidator.get(getReceiver(), true, "Enter owner's
        // name: ");
        // if (ownerName != null) {
        // Float height = heightValidator.get(getReceiver(), false, "Enter owner's
        // height: ");
        // EyeColor eyeColor = eyeValidator.get(getReceiver(), true, "Choose eye color:
        // ");
        // HairColor hairColor = hairValidator.get(getReceiver(), false, "Choose hair
        // color: ");
        // Country country = countryValidator.get(getReceiver(), false, "Choose
        // nationality: ");
        // Location location = locationValidator.get(getReceiver(), true, "Enter
        // location: ");
        // ids = getCollectionManager()
        // .getIdsGreaterThanOwner(new Person(ownerName, height, eyeColor, hairColor,
        // country, location));
        // } else {
        // ids = getCollectionManager()
        // .getIdsGreaterThanOwner(null);
        // }
        // if (ids.size() > 0) {
        // System.out.println("All products with owner greater than input:\n");
        // for (Long id : ids) {
        // System.out.println(getCollectionManager().getInfoById(id));
        // System.out.println();
        // }
        // System.out.println("END OF LIST");
        // } else
        // System.out.println("No elements with greater owners");
        executeFromScript("{}");
    }

    @Override
    public void executeFromScript(String complexArg) {
        String[] tokens = complexArg.replace("{", "").replace("}", "").split(";");
        ArrayList<Long> ids = new ArrayList<>();
        if (tokens.length == 0)
            throw new IllegalArgumentException("No args found. Use {} to compare with null owner");

        StringValidator stringValidator = new StringValidator();
        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        HeightValidator heightValidator = new HeightValidator();
        EyeValidator eyeValidator = new EyeValidator();
        HairValidator hairValidator = new HairValidator();
        CountryValidator countryValidator = new CountryValidator();
        LocationValidator locationValidator = new LocationValidator();

        try {
            String ownerName = tokens[0];
            if (ownerName.isBlank())
                ids = getCollectionManager()
                        .getIdsGreaterThanOwner(null);
            else {
                String height = tokens[1];
                if (!heightValidator.isValid(String.valueOf(height), false))
                    throw new IllegalArgumentException("Invalid value for height");

                String eyeColor = tokens[2];
                if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                    throw new IllegalArgumentException(
                            "Invalid value for eye color. Should be one of GREEN, RED, ORANGE");

                String hairColor = tokens[3];
                if (!hairValidator.isValid(String.valueOf(hairColor), false))
                    throw new IllegalArgumentException(
                            "Invalid value for hair color. Should be one of GREEN, BLACK, WHITE");

                String nationality = tokens[4];
                if (!countryValidator.isValid(String.valueOf(nationality), false))
                    throw new IllegalArgumentException(
                            "Invalid value for country. Should be one of USA, VATICAN, THAILAND");

                String locX = tokens[5];
                String locY = tokens[6];
                String locZ = tokens[7];
                String locName = tokens[8];
                Location location = null;
                if (!locX.isBlank()) {
                    if (!doubleValidator.isValid(String.valueOf(locX), false))
                        throw new IllegalArgumentException("Invalid value for locX");

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

                ids = getCollectionManager()
                        .getIdsGreaterThanOwner(
                                new Person(ownerName, Float.parseFloat(height),
                                        !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor) : null,
                                        HairColor.valueOf(hairColor), Country.valueOf(nationality),
                                        location));

            }
            ArrayList<Product> products = new ArrayList<>();
            if (ids.size() > 0) {
                // System.out.println("All products with owner greater than input:\n");
                for (Long id : ids) {
                    products.add(getCollectionManager().getById(id));
                }
                // System.out.println("END OF LIST");
            } 

            CustomPackage pkg = new CustomPackage(this.getName(), null, products.toArray());
            getReceiver().addToAnswer(getCLient(), pkg);
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            // System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getDescription() {
        return "show all elements where, firstly, owner's name is longer than input, secondly, owner's height is greater than input";
    }

}
