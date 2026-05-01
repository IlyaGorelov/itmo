package Objects.CommandsControllers;

import Objects.Collection.Product;

import java.util.LinkedList;
import java.util.Queue;

public class TrashBin {
    private static final Queue<Product[]> removedInSingleCommand = new LinkedList<>();

    public static void add(Product... adding) {
        if (adding.length > 0) {
            removedInSingleCommand.add(adding);
        }
    }

    public static Product[] poll() {
        return removedInSingleCommand.poll();
    }
}
