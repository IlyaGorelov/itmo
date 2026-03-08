package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Managers.CollectionManager;

/*Show all elements of collection */
public class Show extends Command {

    public Show(CollectionManager collectionManager, boolean hasArgument) {
        super(collectionManager, hasArgument);
    }

    public Show(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        var products = getCollectionManager().getElements();

        if (products.size() > 0) {
            System.out.println("Showing all elements of collection:\n");
            for (var p : products) {
                System.out.println(p.toString() + "\n");
            }
            System.out.println("END OF LIST");
        } else
            System.out.println("Collection has no arguments");
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Show all elements of collection";
    }

}
