package Objects.CommandsControllers.Commands;

import java.util.Date;

import Objects.Collection.Builders.ProductBuilder;
import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
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
 * update an element
 */
public class Update extends Command implements CommandWithComplexArg {

    public Update(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        Object complexArg = tryGetObjectViaComplexArg();
        if(complexArg!=null) {
            return complexArg;
        }

        long id = Long.parseLong(getArgument());
        IdValidator idValidator = new IdValidator();
        if (!idValidator.isValid(getArgument(), false))
            throw new IllegalArgumentException();

        System.out.println("Updating an element with id " + id + ". Type new values");

        ProductBuilder productBuilder = new ProductBuilder();
        return productBuilder.build(getScanner());
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Product product = (Product) pack.getObject();
        String relevant = "Element was succesfully updated with id " + product.getId() + "\n";

        return relevant.trim();
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
