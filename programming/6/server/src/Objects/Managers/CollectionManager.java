package Objects.Managers;

import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Collection.ProductsComparator;
import Objects.Enums.UnitOfMeasure;
import Objects.Parsers.ProductParser;

import java.util.*;
import java.util.stream.Collectors;

/**
 * class that controls collection
 */
public class CollectionManager {
    private final HashSet<Product> products = new HashSet<>();
    /**
     * date of creating of the collection
     */
    private final Date initialDate = getCurrentDate();
    /**
     * path to the file
     */
    private String path;
    /**
     * csv manager that controls reading and writing in .csv
     */
    CSVManager csvManager = new CSVManager();

    /**
     * load collection from env variable, then creates products and fill the
     * collection
     *
     * @param envKey name of env variable with path to the .csv file
     */
    public void loadCollection(String envKey) {
        ProductParser productParser = new ProductParser();

        this.path = System.getenv(envKey);
        var CSVRecords = csvManager.read(path);

        for (var record : CSVRecords) {
            try {
                Product newProduct = productParser.parse(record);
                products.add(newProduct);
            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println(e.getMessage());
                System.out.println("Skip row\n");
            }
        }
    }

    /**
     * returns current date
     *
     * @return Date current date
     */
    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * transform collection into ArrayList to save it in CSVManager
     */
    public void setCollection() {
        ArrayList<ArrayList<Object>> records = new ArrayList<>();

        for (Product p : products) {
            var record = new ArrayList<Object>();
            addProductFieldsToRecord(p, record);
            records.add(record);
        }
        csvManager.write(path, records);
    }

    private void addProductFieldsToRecord(Product p, ArrayList<Object> record) {
        record.add(p.getId());
        record.add(p.getName());
        record.add(p.getCoordinates().getX());
        record.add(p.getCoordinates().getY());
        record.add(p.getCreationDate());
        record.add(p.getPrice());
        record.add(p.getManufactureCost());
        record.add(p.getUnitOfMeasure());

        addPersonFieldsToRecord(p.getOwner(), record);
    }

    private void addPersonFieldsToRecord(Person p, ArrayList<Object> record) {
        if (p == null) {
            for (int index = 0; index < 8; index++) {
                record.add(null);
            }
        } else {
            record.add(p.getName());
            record.add(p.getHeight());
            record.add(p.getEyeColor());
            record.add(p.getHairColor());
            record.add(p.getNationality());

            addLocationFieldsToRecord(p.getLocation(), record);
        }
    }

    private void addLocationFieldsToRecord(Location loc, ArrayList<Object> record) {
        if (loc == null) {
            for (int index = 0; index < 4; index++) {
                record.add(null);
            }
        } else {
            record.add(loc.getX());
            record.add(loc.getY());
            record.add(loc.getZ());
            record.add(loc.getName());
        }
    }

    /**
     * Get info about collection
     *
     * @return info as string like collection type, inital date, size
     */
    public String getCollectionInfo() {
        String info = "";
        String collectionType = products.getClass().toString();
        info += "Type of collection: " + collectionType + "\n";
        info += "Initialization date: " + initialDate + "\n";
        info += "Collection size: " + products.size() + "\n";
        return info;
    }

    /**
     * gets info about product by its id
     *
     * @param id id of a product
     * @return info about product
     * @throws IndexOutOfBoundsException if id isn't in collection
     */
    public Product getById(long id) throws IndexOutOfBoundsException {
        return products.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public HashSet<Product> getElements() {
        return products;
    }

    /**
     * add element to collection
     */
    public Product addElement(Product newProduct) {
        Product product = createProduct(newProduct);
        products.add(product);
        return newProduct;
    }

    /**
     * add element to collection with id
     */
    public Product addElement(Long id, Product rawProduct) {
        Product p = createProduct(id, rawProduct);
        products.add(p);
        return p;
    }

    /**
     * update element of the collection, find element by id
     */
    public Product updateElement(
            long existingId,
            Product rawProduct) throws IndexOutOfBoundsException {
        Product product = null;
        product = products.stream()
                .filter(p -> p.getId() == existingId)
                .findFirst()
                .orElse(null);

        if (product == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");

        product.setName(rawProduct.getName());
        product.setCoordinates(rawProduct.getCoordinates());
        product.setPrice(rawProduct.getPrice());
        product.setManufactureCost(rawProduct.getManufactureCost());
        product.setUnitOfMeasure(rawProduct.getUnitOfMeasure());
        product.setOwner(rawProduct.getOwner());

        return product;
    }

    /**
     * @param existingId id of a product
     * @throws IndexOutOfBoundsException if id isn't in collection
     */
    public Product deleteById(long existingId) throws IndexOutOfBoundsException {
        Product product = products.stream()
                .filter(p -> p.getId() == existingId)
                .findFirst()
                .orElse(null);

        if (product == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");

        products.remove(product);
        IdManager.removeId(existingId);

        return product;
    }

    /**
     * clear all collection
     */
    public void clear() {
        products.clear();
        IdManager.clear();
    }

    /**
     * @return boolean is the element gonna be max
     */
    public boolean isMax(Product rawProduct) {
        Product newProduct = createProduct(rawProduct);
        var maxProduct = products.stream()
                .max(new ProductsComparator())
                .orElse(null);

        return newProduct.compareTo(maxProduct) == 1;
    }

    /**
     * Is an element gonna be min
     *
     * @return boolean is an element gonna be min
     */
    public boolean isMin(Product rawProduct) {
        Product newProduct = createProduct(rawProduct);
        var minProduct = products.stream()
                .min(new ProductsComparator())
                .orElse(null);

        return newProduct.compareTo(minProduct) == -1;
    }

    /**
     * get ids of element greater than given one
     *
     * @return ArrayList of ids
     */
    public Product[] removeGreater(Product product) {
        Product[] productsToRemove = products.stream()
                .filter(p -> p.compareTo(product) == 1)
                .toArray(Product[]::new);

        Arrays.stream(productsToRemove)
                .forEach(p -> {
                    products.remove(p);
                    IdManager.removeId(p.getId());
                });

        return productsToRemove;
    }

    /**
     * @return Array of deleted Products
     */
    public Product[] removeByUnitOfMeasure(UnitOfMeasure comparing) {
        Product[] productsToDelete = products.stream()
                .filter(p -> Objects.equals(p.getUnitOfMeasure(), comparing))
                .toArray(Product[]::new);

        Arrays.stream(productsToDelete).forEach(p -> {
            products.remove(p);
            IdManager.removeId(p.getId());
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
        var ids = products.stream()
                .filter(p -> {
                    if (owner == null) {
                        return p.getOwner() != null;
                    } else {
                        return p.getOwner().compareTo(owner) == 1;
                    }
                })
                .map(Product::getId)
                .collect(Collectors.toCollection(ArrayList<Long>::new));

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
    private Product createProduct(
            long id,
            Product product
    ) {
        IdManager.addId(id);
        product.setId(id);
        return product;
    }
}
