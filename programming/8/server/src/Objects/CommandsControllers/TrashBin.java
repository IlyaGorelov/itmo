package Objects.CommandsControllers;

import Commons.Collection.Product;

import java.util.Deque;
import java.util.LinkedList;

public class TrashBin {
    private static final Deque<Product[]> removedInSingleCommand = new LinkedList<>();

    public static void add(Product... adding) {
        if (adding.length > 0) {
            removedInSingleCommand.add(adding);
        }
    }

    public static Product[] pop() {
        return removedInSingleCommand.pollLast();
    }
}
