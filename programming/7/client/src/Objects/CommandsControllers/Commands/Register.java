package Objects.CommandsControllers.Commands;

import Objects.Collection.Builders.ProductBuilder;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.CommandsControllers.CommandWithComplexArg;
import Objects.Connection.CustomPackage;
import Objects.Parsers.ProductParser;

/**
 * Adds an element to the collection
 */
public class Register extends Command {

    public Register(boolean hasArgument, boolean hasComplexArgument) {
        super(hasArgument, hasComplexArgument);
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();

        System.out.println("Type values to register.");

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

}
