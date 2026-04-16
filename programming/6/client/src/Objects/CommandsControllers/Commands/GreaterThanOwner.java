package Objects.CommandsControllers.Commands;

import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Validators.*;

/** get all elements where owner is biger than input one */
public class GreaterThanOwner extends Command {
    public GreaterThanOwner(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    public GreaterThanOwner() {
        super();
    }

    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        StringValidator stringValidator = new StringValidator();
        HeightValidator heightValidator = new HeightValidator();
        EyeValidator eyeValidator = new EyeValidator();
        HairValidator hairValidator = new HairValidator();
        CountryValidator countryValidator = new CountryValidator();
        LocationValidator locationValidator = new LocationValidator();

        checkArgument();

        System.out.println("Input an owner values you want to compare with (you can also type nothing):\n");

        String ownerName = stringValidator.get(getScanner(), true, "Enter owner's name: ");
        if (ownerName != null) {
            Float height = heightValidator.get(getScanner(), false, "Enter owner's height: ");
            return new Person(ownerName, height, EyeColor.GREEN, HairColor.BLACK, Country.USA, null).getFuncString();
        } else {
            return null;
        }
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object[] products = (Object[]) pack.getObject();
        if (products.length == 0)
            return "There is no products with greater owner.";
        else {
            String relevant = "Products with greater owner:\n";
            for (Object o : products) {
                relevant += o.toString() + "\n";
            }
            return relevant;
        }
    }

}
