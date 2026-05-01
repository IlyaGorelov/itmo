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
public class AddIfMax extends Command implements CommandWithComplexArg {

    public AddIfMax(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        Object complexArg = tryGetObjectViaComplexArg();
        if(complexArg!=null) {
            return complexArg;
        }

        System.out.println("Adding new element. Type new values.");

        ProductBuilder productBuilder = new ProductBuilder();
        return productBuilder.build(getScanner());
    }

    @Override
    public String getName() {
        return "add_if_max";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Product product = (Product) pack.getObject();

        if (product != null)
            return "Element with name \"" + ((Product) product).getName() + "\" was succesfully added" + "\n";
        else
            return "Element wasn't added as it's not max" + "\n";

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
