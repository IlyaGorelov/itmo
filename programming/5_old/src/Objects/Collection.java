package Objects;

import java.util.HashSet;

public class Collection {
    private HashSet<Product> products = new HashSet<>();

    public Collection() {
        products = sort(products);
    }

    private HashSet<Product> sort(HashSet<Product> oldProducts) {
        HashSet<Product> newProducts = new HashSet<>();
        return newProducts;
    }
}
