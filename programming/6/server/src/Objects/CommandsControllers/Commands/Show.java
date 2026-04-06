package Objects.CommandsControllers.Commands;

import org.openjdk.jol.info.GraphLayout;

import Objects.Collection.Product;
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
        String answer = "";

        // if (products.size() > 0) {
        // answer += ("Showing all elements of collection:\n");
        // // System.out.println("Showing all elements of collection:\n");
        // for (var p : products) {
        // answer += (p.toString() + "\n");
        // }
        // answer += ("END OF LIST");
        // } else
        // answer += ("Collection has no arguments");

        // for (Product product : products) {
        // System.out.println(GraphLayout.parseInstance(product).totalSize());
        // }

        getReceiver()
                .addToAnswer(this, null,
                        products.stream().sorted((x, y) -> Long.compare(GraphLayout.parseInstance(x).totalSize(),
                                GraphLayout.parseInstance(y).totalSize())).toArray());

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
