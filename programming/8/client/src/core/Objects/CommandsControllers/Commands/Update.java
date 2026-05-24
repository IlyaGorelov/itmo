package core.Objects.CommandsControllers.Commands;

import Localization.I18n;
import core.Objects.Builders.ProductBuilder;
import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.CommandWithComplexArg;
import Commons.CustomPackage;
import core.Objects.Managers.AuthManager;
import core.Objects.Managers.IdManager;
import core.Objects.Parsers.ProductParser;
import core.Objects.Validators.*;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/**
 * update an element
 */
public class Update extends Command implements CommandWithComplexArg, AuthChecker {

    public Update(boolean hasArgument) {
        super(hasArgument);
    }

    public Update() {
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

        long id = Long.parseLong(getArgument());
        IdValidator idValidator = new IdValidator();
        if (!idValidator.isValid(getArgument(), false))
            throw new IllegalArgumentException();

        if(IdManager.getProductById(id).getAuthor().getId()!= AuthManager.getInstance().getUser().getId()){
            throw new IllegalArgumentException("You aren't this product's author!");
        }

        System.out.println("Updating an element with id " + id + ". Type new values");

        ProductBuilder productBuilder = new ProductBuilder();
        return productBuilder.build(getScanner());
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update an element";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Product product = (Product) pack.getObject();
        String relevant = I18n.get("info.update").formatted(product.getName()) + "\n";

        TablePanel.fetchProductsAsync();
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
