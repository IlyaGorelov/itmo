package core.Objects.CommandsControllers.Commands;

import Commons.Collection.*;
import Localization.I18n;
import core.Objects.Builders.ProductBuilder;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import core.Objects.CommandsControllers.CommandWithComplexArg;
import Commons.CustomPackage;
import core.Objects.Parsers.ProductParser;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/**
 * Adds an element to the collection
 */
public class Add extends Command implements CommandWithComplexArg, AuthChecker {

    public Add(boolean hasArgument) {
        super(hasArgument);
    }

    public Add() {
        super();
    }

    @Override
    public Object getRelevantObject() {
        checkArgument();
        checkAuth();

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

        if (object instanceof Product) {
            TablePanel.fetchProductsAsync();
            return I18n.get("info.command.add").formatted(((Product) object).getName())+"\n";
        } else {
            return object.toString();
        }
    }

    @Override
    public String getDescription() {
        return "add new element";
    }

    @Override
    public Object tryGetObjectViaComplexArg() {
        ProductParser productParser = new ProductParser();
        try {
            if (getComplexArgument() != null) {
                return productParser.parse(getComplexArgument());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid command format, use:\n\tadd {Name(String);X(int);Y(double>-990);Price(double>0 | null);Man Cost(int);unit of measure | null;Owner name(String) | null;Height(float>0);eye color | null;hair color;country;location x|null;loc y;loc z;loc name}");
        }
        return null;
    }
}
