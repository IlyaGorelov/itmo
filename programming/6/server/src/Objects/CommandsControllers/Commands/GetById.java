package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import Objects.Validators.*;

/** Gets an element by id */
public class GetById extends Command {
    public GetById(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public GetById(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /** Asks for required fields then creates new element - Product */
    @Override
    public void execute() {
        checkArgument();
        IdValidator idValidator = new IdValidator();
        try {
            long id = Long.parseLong(getArgument());
            if (!idValidator.isValid(String.valueOf(id), false))
                throw new IllegalArgumentException("Invalid value for id");
            var element = getCollectionManager().getById(id);

            CustomPackage pkg = new CustomPackage(this.getName(), null, element);
            getReceiver().addToAnswer(getCLient(), pkg);

        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, null);
            getReceiver().addToAnswer(getCLient(), pkg);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void executeFromScript(String complexArg) {
        execute();
    }

    @Override
    public String getName() {
        return "get_by_id";
    }

    @Override
    public String getDescription() {
        return "get element of collection by id";
    }

}
