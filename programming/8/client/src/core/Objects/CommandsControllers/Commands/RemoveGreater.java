package core.Objects.CommandsControllers.Commands;

import java.util.Date;

import Commons.Collection.Coordinates;
import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.CommandWithComplexArg;
import Commons.CustomPackage;
import core.Objects.Managers.AuthManager;
import core.Objects.Parsers.ProductParser;
import core.Objects.Validators.*;

/*remove elements greater than input */
public class RemoveGreater extends Command implements CommandWithComplexArg , AuthChecker {
    public RemoveGreater(boolean hasArgument) {
        super(hasArgument);
    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove your products greater than input ";
    }


    @Override
    public Object getRelevantObject() {
        checkArgument();
        checkAuth();

        Object complexArg = tryGetObjectViaComplexArg();
        if(complexArg!=null) {
            return complexArg;
        }

        System.out.println("Type values to compare with.");
        PriceValidator priceValidator = new PriceValidator();
        IntegerValidator integerValidator = new IntegerValidator();

        Double price = priceValidator.get(getScanner(), true, "Enter price(double): ");
        Integer manufactureCost = integerValidator.get(getScanner(), false, "Enter manufacture cost(integer): ");

        return new Product(0, "", new Coordinates(0, 0), new Date(), price, manufactureCost, null,
                null, AuthManager.getInstance().getUser());
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        String relevant = "";
            Object[] products = (Object[]) pack.getObject();
            if (products.length == 0)
                return "There are no elements greater than input\n";

            for (Object product : products) {
                relevant += "Element with id " + ((Product) product).getId() + " was removed\n";
            }

        return relevant;
    }

    @Override
    public Object tryGetObjectViaComplexArg() {
        ProductParser productParser = new ProductParser();
        try {
            if (getComplexArgument() != null)
                return productParser.parse(getComplexArgument());
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid command format, use:\n\tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
        }
        return null;
    }

}
