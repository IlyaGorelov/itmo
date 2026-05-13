package Objects.CommandsControllers.Commands;

import Objects.Collection.Person;
import Objects.CommandsControllers.AuthChecker;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandWithComplexArg;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Parsers.PersonParser;
import Objects.Validators.*;

/**
 * get all elements where owner is biger than input one
 */
public class GreaterThanOwner extends Command implements CommandWithComplexArg, AuthChecker {
    public GreaterThanOwner(boolean hasArgument) {
        super(hasArgument);
    }

    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getDescription() {
        return "show all elements where, firstly, owner's name is longer than input, secondly, owner's height is greater than input";
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        checkAuth();

        Object complexArg = tryGetObjectViaComplexArg();
        if (complexArg != null) {
            return complexArg;
        }

        StringValidator stringValidator = new StringValidator();
        HeightValidator heightValidator = new HeightValidator();

        System.out.println("Input an owner values you want to compare with (you can also type nothing):\n");

        String ownerName = stringValidator.get(getScanner(), true, "Enter owner's name: ");
        if (ownerName != null) {
            Float height = heightValidator.get(getScanner(), false, "Enter owner's height: ");
            return new Person(ownerName, height, EyeColor.GREEN, HairColor.BLACK, Country.USA, null).getFuncString();
        }

        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object[] products = (Object[]) pack.getObject();
        if (products.length == 0) {
            return "There is no products with greater owner.";
        } else {
            String relevant = "Products with greater owner:\n";
            for (Object o : products) {
                relevant += o.toString() + "\n";
            }
            return relevant;
        }
    }

    @Override
    public Object tryGetObjectViaComplexArg() {
        PersonParser personParser = new PersonParser();
        try {
            if (getComplexArgument() != null) {
                return personParser.parse(getComplexArgument());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid command format, use:\n\tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
        }
        return null;
    }

}
