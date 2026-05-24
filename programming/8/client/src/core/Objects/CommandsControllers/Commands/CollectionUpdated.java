package core.Objects.CommandsControllers.Commands;

import Commons.CustomPackage;
import core.Objects.CommandsControllers.Command;

public class CollectionUpdated extends Command {
    @Override
    public String getName() {
        return "collection_updated";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public Object getRelevantObject() {
        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        return "";
    }
}
