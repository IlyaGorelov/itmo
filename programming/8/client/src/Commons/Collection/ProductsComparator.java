package Commons.Collection;

import java.util.Comparator;

/** Comparator for TreeSet */
public class ProductsComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o1.compareTo(o2);
    }

}
