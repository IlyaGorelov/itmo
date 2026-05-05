package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import org.openjdk.jol.info.GraphLayout;

/*Show all elements of collection */
public class Show extends Command {


    public Show(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        checkArgument();
        var products = getCollectionManager().getElements();
        String answer = "";

        var productsSorted = products.stream().sorted((x, y) -> Long.compare(GraphLayout.parseInstance(x).totalSize(),
                GraphLayout.parseInstance(y).totalSize())).toArray();

        CustomPackage pkg = new CustomPackage(this.getName(), null,
                products.stream().sorted((x, y) -> Long.compare(GraphLayout.parseInstance(x).totalSize(),
                        GraphLayout.parseInstance(y).totalSize())).toArray());

        answer(pkg, products.stream().sorted((x, y) -> Long.compare(GraphLayout.parseInstance(x).totalSize(),
                GraphLayout.parseInstance(y).totalSize())).toArray(Product[]::new));

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
