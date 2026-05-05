package Objects.Managers;

import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Collection.ProductsComparator;
import Objects.DAOs.ProductDAO;
import Objects.Enums.UnitOfMeasure;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class that controls collection
 */
public class CollectionManager {
    private final static Logger logger = LoggerFactory.getLogger(CollectionManager.class);

    private final HashSet<Product> products;
    private final ProductDAO productDAO;
    /**
     * date of creating of the collection
     */
    private final Date initialDate = getCurrentDate();

    private final String urlToDb;

    private User currentUser;

    public CollectionManager(String envKeyToDbUrl, String envKeyToPropsPath) {
        this.urlToDb = System.getenv(envKeyToDbUrl);
        String pathToProps = System.getenv(envKeyToPropsPath);

        productDAO = new ProductDAO(new DBManager(urlToDb, pathToProps));
        products = productDAO.loadProducts();
    }


    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }


    public String getCollectionInfo() {
        String info = "";
        String collectionType = products.getClass().toString();
        info += "Type of collection: " + collectionType + "\n";
        info += "Initialization date: " + initialDate + "\n";
        info += "Collection size: " + products.size() + "\n";
        return info;
    }


    public Product getById(long id) throws IndexOutOfBoundsException {
        return products.stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public HashSet<Product> getElements() {
        return products;
    }

    public Product addElement(Product newProduct) {
        Product product = createProduct(newProduct);
        try {
            productDAO.insertProduct(product);
            products.add(product);

            return newProduct;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            IdManager.removeId(product.getId());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product addElement(Long id, Product rawProduct) {
        try {
            Product product = createProduct(id, rawProduct);

            productDAO.insertProductWithId(id, product);
            products.add(product);

            return rawProduct;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            IdManager.removeId(id);
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product updateElement(long existingId, Product rawProduct) throws IndexOutOfBoundsException {
        Product product = null;
        product = products.stream().filter(p -> p.getId() == existingId).findFirst().orElse(null);

        if (product == null) throw new IndexOutOfBoundsException("There is no element with such id!");

        try {
            productDAO.updateProduct(existingId, rawProduct);


            product.setName(rawProduct.getName());
            product.setCoordinates(rawProduct.getCoordinates());
            product.setPrice(rawProduct.getPrice());
            product.setManufactureCost(rawProduct.getManufactureCost());
            product.setUnitOfMeasure(rawProduct.getUnitOfMeasure());
            product.setOwner(rawProduct.getOwner());

            return product;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param existingId id of a product
     * @throws IndexOutOfBoundsException if id isn't in collection
     */
    public Product deleteById(long existingId) throws IndexOutOfBoundsException {
        Product product = products.stream().filter(p -> p.getId() == existingId).findFirst().orElse(null);

        if (product == null)
            throw new IndexOutOfBoundsException("There is no element with id: " + existingId);

        if (!product.getAuthor().equals(currentUser))
            throw new IllegalArgumentException("It's not your product");

        try {
            productDAO.deleteProductById(existingId);

            products.remove(product);
            IdManager.removeId(existingId);

            return product;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public Product[] clear() {
        try {
            Product[] productsToDelete = products.stream()
                    .filter(p -> p.getAuthor().equals(currentUser))
                    .toArray(Product[]::new);

            for (Product p : productsToDelete) {
                productDAO.deleteProductById(p.getId());
                products.remove(p);
                IdManager.removeId(p.getId());
            }
            return productsToDelete;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @return boolean is the element gonna be max
     */
    public boolean isMax(Product rawProduct) {
        Product newProduct = createProduct(rawProduct);
        var maxProduct = products.stream().max(new ProductsComparator()).orElse(null);

        return newProduct.compareTo(maxProduct) == 1;
    }

    /**
     * Is an element gonna be min
     *
     * @return boolean is an element gonna be min
     */
    public boolean isMin(Product rawProduct) {
        Product newProduct = createProduct(rawProduct);
        var minProduct = products.stream().min(new ProductsComparator()).orElse(null);

        return newProduct.compareTo(minProduct) == -1;
    }

    /**
     * get ids of element greater than given one
     *
     * @return ArrayList of ids
     */
    public Product[] removeGreater(Product product) {
        Product[] productsToRemove = products.stream().filter(p -> p.compareTo(product) == 1 && p.getAuthor().equals(currentUser)).toArray(Product[]::new);

        Arrays.stream(productsToRemove).forEach(p -> {
            deleteById(p.getId());
        });

        return productsToRemove;
    }

    /**
     * @return Array of deleted Products
     */
    public Product[] removeByUnitOfMeasure(UnitOfMeasure comparing) {
        Product[] productsToDelete = products.stream()
                .filter(p -> Objects.equals(p.getUnitOfMeasure(), comparing) && p.getAuthor().equals(currentUser))
                .toArray(Product[]::new);

        Arrays.stream(productsToDelete).forEach(p -> {
            deleteById(p.getId());
        });

        return productsToDelete;
    }

    /**
     * get any minimal product with minimal unit of measure
     *
     * @return information about minimal product
     */
    public Product getMinByUnitOfMeasure() {
        UnitOfMeasure minUnit = getMinUnitOfMeasure();

        Product wanted = products.stream()
                .filter(p -> p.getUnitOfMeasure() == minUnit)
                .findFirst()
                .orElse(null);

        return wanted;
    }

    private UnitOfMeasure getMinUnitOfMeasure() {
        TreeSet<UnitOfMeasure> unitOfMeasures = new TreeSet<>();

        for (Product p : products) {
            if (p.getUnitOfMeasure() != null) {
                unitOfMeasures.add(p.getUnitOfMeasure());
            }
        }

        if (unitOfMeasures.isEmpty()) return null;
        return unitOfMeasures.first();
    }


    /**
     * get ids of elements with bigger owner
     *
     * @return ArrayList of ids
     */
    public ArrayList<Long> getIdsGreaterThanOwner(Person owner) {
        var ids = products.stream().filter(p -> {
            if (owner == null) {
                return p.getOwner() != null;
            } else {
                return p.getOwner().compareTo(owner) == 1;
            }
        }).map(Product::getId).collect(Collectors.toCollection(ArrayList<Long>::new));

        return ids;
    }

    /**
     * create product
     */
    private Product createProduct(Product newProduct) {
        long id = IdManager.getId();
        newProduct.setId(id);
        newProduct.setCreationDate(getCurrentDate());
        return newProduct;
    }

    /**
     * create product with given id
     */
    private Product createProduct(long id, Product product) {
        IdManager.addId(id);
        product.setId(id);
        return product;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
