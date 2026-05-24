package core.Objects.CommandsControllers.Commands;

import Localization.I18n;
import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Elements.Main.TableTab.TablePanel;

/** show information about collection */
public class Redo extends Command implements AuthChecker {

    public Redo() {
        super();
    }

    @Override
    public String getName() {
        return "redo";
    }

    @Override
    public String getDescription() {
        return "undo(undo)";
    }

    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object arg = pack.getObject();

        TablePanel.fetchProductsAsync();
        return I18n.get("info.redo") + "\n";
    }

}
