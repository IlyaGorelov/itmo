package Objects.Managers;

import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Comparators.PersonComparator;
import Objects.Comparators.ProductsComparator;
import Objects.DAOs.ProductDAO;
import Objects.Enums.UnitOfMeasure;
import Objects.UserData.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CollectionManager {
    private final static Logger logger = LoggerFactory.getLogger(CollectionManager.class);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final HashSet<Product> products;

    private final ProductDAO productDAO;

    private final Date initialDate = getCurrentDate();


    public CollectionManager() {
        productDAO = new ProductDAO();
        products = productDAO.loadProducts();
    }

    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public String getCollectionInfo() {
        lock.readLock().lock();

        try {
            String info = "";
            String collectionType = products.getClass().toString();
            info += "Type of collection: " + collectionType + "\n";
            info += "Initialization date: " + initialDate + "\n";
            info += "Collection size: " + products.size() + "\n";

            return info;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Product getById(long id) throws IndexOutOfBoundsException {
        lock.readLock().lock();

        try {
            return products.stream()
                    .filter(x -> x.getId() == id)
                    .findFirst()
                    .orElse(null);
        } finally {
            lock.readLock().unlock();
        }
    }

    public HashSet<Product> getElements() {
        lock.readLock().lock();

        try {
            return products;
        } finally {
            lock.readLock().unlock();
        }
    }

    public Product addElement(Product newProduct) {
        lock.writeLock().lock();

        Product product = createProduct(newProduct);
        try {
            Product added = productDAO.insertProduct(product);
            products.add(added);

            return added;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            IdManager.removeId(product.getId());
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /***
     add element with specified id
     * @param id element will be initialized with this id
     * @param newProduct product with values of the new element
     * @return added product
     */
    public Product addElement(Long id, Product newProduct) {
        lock.writeLock().lock();

        try {
            Product product = createProduct(id, newProduct);

            productDAO.insertProductWithId(id, product);
            products.add(product);

            return newProduct;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            IdManager.removeId(id);
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Product updateElement(long existingId, Product rawProduct) throws IndexOutOfBoundsException {
        lock.writeLock().lock();
        Product product = null;

        try {
            product = products.stream()
                    .filter(p -> p.getId() == existingId)
                    .findFirst()
                    .orElse(null);

            if (product == null) throw new IndexOutOfBoundsException("There is no element with such id!");

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
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Product deleteById(long existingId, User user) throws IndexOutOfBoundsException {
        lock.writeLock().lock();
        try {
            Product product = products.stream()
                    .filter(p -> p.getId() == existingId)
                    .findFirst()
                    .orElse(null);

            if (product == null) throw new IndexOutOfBoundsException("There is no element with id: " + existingId);

            if (!product.getAuthor().equals(user)) throw new IllegalArgumentException("It's not your product");

            productDAO.deleteProductById(existingId);

            products.remove(product);
            IdManager.removeId(existingId);

            return product;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Product[] clear(User user) {
        lock.writeLock().lock();
        try {
            Product[] productsToDelete = products.stream()
                    .filter(p -> p.getAuthor().equals(user))
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
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isMax(Product rawProduct) {
        lock.readLock().lock();
        try {
            Product newProduct = createProduct(rawProduct);
            var maxProduct = products.stream()
                    .max(new ProductsComparator())
                    .orElse(null);

            return newProduct.compareTo(maxProduct) == 1;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isMin(Product rawProduct) {
        lock.readLock().lock();

        try {
            Product newProduct = createProduct(rawProduct);
            var minProduct = products.stream()
                    .min(new ProductsComparator())
                    .orElse(null);

            return newProduct.compareTo(minProduct) == -1;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * get ids of elements greater than given one
     *
     * @return ArrayList of ids
     */
    public Product[] removeGreater(Product product, User user) {
        lock.writeLock().lock();

        try {
            Product[] productsToRemove = products.stream()
                    .filter(p -> p.compareTo(product) == 1 && p.getAuthor().equals(user))
                    .toArray(Product[]::new);

            Arrays.stream(productsToRemove).forEach(p -> {
                deleteById(p.getId(), user);
            });

            return productsToRemove;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Product[] removeByUnitOfMeasure(UnitOfMeasure comparing, User user) {
        lock.writeLock().lock();

        try {
            Product[] productsToDelete = products.stream()
                    .filter(p -> Objects.equals(p.getUnitOfMeasure(), comparing) && p.getAuthor().equals(user))
                    .toArray(Product[]::new);

            Arrays.stream(productsToDelete).forEach(p -> {
                deleteById(p.getId(), user);
            });

            return productsToDelete;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * get any minimal product with minimal unit of measure
     *
     * @return minimal product
     */
    public Product getMinByUnitOfMeasure() {
        lock.readLock().lock();

        try {
            UnitOfMeasure minUnit = getMinUnitOfMeasure();

            Product wanted = products.stream()
                    .filter(p -> p.getUnitOfMeasure() == minUnit)
                    .findFirst()
                    .orElse(null);

            return wanted;
        } finally {
            lock.readLock().unlock();
        }
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

    public ArrayList<Long> getIdsGreaterThanOwner(Person owner) {
        lock.readLock().lock();
        try {
            var ids = products.stream()
                    .filter(p -> new PersonComparator().compare(p.getOwner(), owner) == 1)
                    .map(Product::getId)
                    .collect(Collectors.toCollection(ArrayList<Long>::new));

            return ids;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * create product
     */
    private Product createProduct(Product newProduct) {
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
}
