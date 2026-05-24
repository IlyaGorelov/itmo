package core.Objects.CommandsControllers.Commands;

import Commons.Collection.Product;
import Localization.I18n;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/** show any element where unit of measure is minimal */
public class MinByUnit extends Command implements AuthChecker {

    public MinByUnit() {
        super();
    }

    @Override
    public String getName() {
        return "min_by_unit";
    }

    @Override
    public String getDescription() {
        return "show any element where unit of measure is minimal";
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

        if (object == null)
            return I18n.get("info.minByUnit1");
        if (object instanceof Product) {
            TablePanel.setProducts((Product) object);
            return "";
        }
        else {
            sendError(object + "\n");
            return null;
        }

    }

}
