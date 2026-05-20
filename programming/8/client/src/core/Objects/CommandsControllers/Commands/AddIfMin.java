package core.Objects.CommandsControllers.Commands;

import core.Objects.Builders.ProductBuilder;
import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.CommandWithComplexArg;
import Commons.CustomPackage;
import core.Objects.Parsers.ProductParser;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/** Adds element to a collection if this element gonna be max */
public class AddIfMin extends Command implements CommandWithComplexArg, AuthChecker {

    public AddIfMin(boolean hasArgument) {
        super(hasArgument);
    }

    public AddIfMin() {
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        checkAuth();

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
        return "add_if_min";
    }

    @Override
    public String getDescription() {
        return "add new element if the new is less than the max of collection";
    }


    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Product product = (Product) pack.getObject();

        if (product != null) {
            String answer = "Element with name \"" + ((Product) product).getName() + "\" was succesfully added" + "\n";
            ResultDialog.showSuccess(getName(),answer);
            TablePanel.fetchProductsAsync();
            return answer;
        }else {
            String answer = "Element wasn't added as it's not min" + "\n";
            ResultDialog.showInfo(getName(),answer);
            return answer;
        }
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
