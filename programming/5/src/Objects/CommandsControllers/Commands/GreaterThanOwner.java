package Objects.CommandsControllers.Commands;

import Objects.Collection.Person;
import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;

/** get all elements where owner is biger than input one */
public class GreaterThanOwner extends Command {
    public GreaterThanOwner(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public GreaterThanOwner(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();

        var ids = getCollectionManager().getIdsGreaterThanOwner(new Person(getArgument()));
        for (Long id : ids) {
            System.out.println(getCollectionManager().getInfoById(id));
        }

    }

    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getDescription() {
        return "show all elements where owner is bigger than input";
    }

}
