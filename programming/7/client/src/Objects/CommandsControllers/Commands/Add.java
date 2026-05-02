package Objects.CommandsControllers.Commands;

import java.util.Date;

import Objects.Collection.*;
import Objects.Collection.Builders.ProductBuilder;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandWithComplexArg;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Parsers.ProductParser;
import Objects.Validators.*;

/**
 * Adds an element to the collection
 */
public class Add extends Command implements CommandWithComplexArg {

    public Add(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        Object complexArg = tryGetObjectViaComplexArg();
        if (complexArg != null) {
            return complexArg;
        }

        System.out.println("Adding new element. Type new values.");

        ProductBuilder productBuilder = new ProductBuilder();
        return productBuilder.build(getScanner());
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

    @Override
    public Object tryGetObjectViaComplexArg() {
        ProductParser productParser = new ProductParser();
        try {
            if (getComplexArgument() != null)
                return productParser.parse(getComplexArgument());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid command format, use:\n\tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
        }
        return null;
    }

}
