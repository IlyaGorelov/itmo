package core.Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import Localization.I18n;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/* remove all elements with the same unit of measure*/
public class RemoveByUnitOfMeasure extends Command implements AuthChecker {
    public RemoveByUnitOfMeasure(boolean hasArgument) {
        super(hasArgument);
    }

    public RemoveByUnitOfMeasure() {
        super();
    }

    @Override
    public String getName() {
        return "remove_all_by_unit";
    }

    @Override
    public String getDescription() {
        return "remove all elements with the same unit of measure";
    }


    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();
        if (object instanceof Exception)
            return ((Exception) object).getMessage() + "\n";

        Object[] products = (Object[]) object;
        String relevant = "";
        if (products.length == 0)
            return I18n.get("info.remove.by.unit") +"\n";

        for (Object product : products) {
            relevant += I18n.get("info.remove").formatted(((Product)product).getName())+ "\n";
        }
        TablePanel.fetchProductsAsync();
        return relevant;
    }

    @Override
    public void checkArgument() {
        return;
    }

    @Override
    public boolean getHasArgument() {
        return false;
    }

}
