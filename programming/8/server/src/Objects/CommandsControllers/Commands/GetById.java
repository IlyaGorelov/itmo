package Objects.CommandsControllers.Commands;

import Commons.CustomPackage;
import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;
import Objects.Validators.IdValidator;

/**
 * Gets an element by id
 */
public class GetById extends Command {
    public GetById(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    /**
     * Asks for required fields then creates new element - Product
     */
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
            answer(pkg, element.toString());

        } catch (Exception e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, null);
            answer(pkg, e.getMessage());

            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "get_by_id";
    }

    @Override
    public String getDescription() {
        return "get element of collection by id";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException(String.format("Invalid format, use:\n\t%s id", getName()));
    }
}
