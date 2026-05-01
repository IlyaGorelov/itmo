package Objects.CommandsControllers.Commands;

import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;

import java.util.ArrayList;

/**
 * get all elements where owner is bigger than input one
 */
public class GreaterThanOwner extends Command {
    public GreaterThanOwner(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public GreaterThanOwner(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        Person owner = (Person) getComplexArgument();
        ArrayList<Long> ids = getCollectionManager()
                .getIdsGreaterThanOwner(owner);


        ArrayList<Product> products = new ArrayList<>();
        if (!ids.isEmpty()) {
            for (Long id : ids) {
                products.add(getCollectionManager().getById(id));
            }
        }

        CustomPackage pkg = new CustomPackage(this.getName(), null, products.toArray());
        answer(pkg, products.toArray(new Product[0]));
    }


    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getDescription() {
        return "show all elements where, firstly, owner's name is longer than input, secondly, owner's height is greater than input";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        // boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument())
            throw new IllegalArgumentException(String.format("Invalid format, use:\n\t%s {name(String);height(float>0)}", getName()));
    }

}
