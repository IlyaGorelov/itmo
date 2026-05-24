package core.Objects.CommandsControllers.Commands;

import Localization.I18n;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/** show information about collection */
public class Undo extends Command implements AuthChecker {

    public Undo() {
        super();
    }

    @Override
    public String getName() {
        return "undo";
    }

    @Override
    public String getDescription() {
        return "undo prev command";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object arg = (Object) pack.getObject();

        TablePanel.fetchProductsAsync();
        return I18n.get("info.undo") + "\n";
    }

}
