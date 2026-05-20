package core.Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/** remove an element by id */
public class Remove extends Command implements AuthChecker {
    public Remove(boolean hasArgument) {
        super(hasArgument);
    }

    public Remove() {
        super();
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "remove an element by id";
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object object = pack.getObject();

        if (object instanceof Product) {
            ResultDialog.showSuccess(this.getName(),"Element with name \"" + ((Product) object).getName() + "\" was succesfully removed" + "\n");
            TablePanel.fetchProductsAsync();
            return "Element with name \"" + ((Product) object).getName() + "\" was succesfully removed" + "\n";
        }else {
            ResultDialog.showError(this.getName(),"Element wasn't removed as " + object.toString());
            return "Element wasn't removed as " + object.toString() + "\n";
        } }

}
