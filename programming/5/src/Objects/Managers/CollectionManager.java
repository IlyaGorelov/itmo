package Objects.Managers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

import Objects.Collection.Coordinates;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Collection.ProductsComparator;
import Objects.Enums.UnitOfMeasure;

/** class that controls collection */
public class CollectionManager {
    /** colllection we work with */
    private HashSet<Product> products = new HashSet<>();
    /** date of creating of the collection */
    private Date initialDate = getCurrentDate();
    /** path to the file */
    private String path;
    /** csv manager that controls reading and writing in .csv */
    CSVManager csvManager = new CSVManager();

    /**
     * load collection from env variable, then creates products and fill the
     * collection
     * 
     * @param envKey name of env variable with path to the .csv file
     */
    public void loadCollection(String envKey) {
        this.path = System.getenv(envKey);
        var CSVRecords = csvManager.read(path);

        for (var record : CSVRecords) {
            String name = record.get("name");
            Coordinates coordinates = new Coordinates(Integer.parseInt(record.get("x")),
                    Double.parseDouble(record.get("y")));
            Double price = Double.parseDouble(record.get("price"));
            Integer manufactureCost = Integer.parseInt(record.get("manufactureCost"));
            UnitOfMeasure unitOfMeasure = UnitOfMeasure.valueOf(record.get("unitOfMeasure"));
            Person owner = new Person(record.get("owner"));
            products.add(
                    createProduct(name, coordinates, price, manufactureCost, unitOfMeasure, owner));
        }
    }

    /**
     * sorts Hash Set by transforming it into TreeSet. Used to find max and min
     * 
     * @return TreeSet sorted HashSet, but TreeSet
     */
    public TreeSet<Product> getSorted() {
        TreeSet<Product> sorted = new TreeSet<>(new ProductsComparator());
        sorted.addAll(products);
        return sorted;
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

        ArrayList<String> records = new ArrayList<>();

        for (Product p : products) {
            records.add(String.valueOf(p.getId()) + ',' + p.getName() + ',' + p.getCoordinates().getX() + ','
                    + p.getCoordinates().getY() + ',' + p.getCreationDate() + ',' + p.getPrice()
                    + ',' + p.getManufactureCost() + ',' + p.getUnitOfMeasure() + ','
                    + p.getOwner().getName() + ',');
        }

        csvManager.write(path, records);

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
    public String getInfoById(long id) throws IndexOutOfBoundsException {
        for (Product product : products) {
            if (id == product.getId()) {
                return product.toString();
            }
        }
        throw new IndexOutOfBoundsException("There is no element with such id!");
    }

    public HashSet<Product> getElements() {
        return products;
    }

    /**
     * add element to collection
     */
    public void addElement(String name, Coordinates coordinates, Double price, Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, String ownerName) {
        Product p = createProduct(name, coordinates, price, manufactureCost, unitOfMeasure,
                new Person(ownerName));
        products.add(p);
    }

    /** update element of the collection, find element by id */
    public void updateElement(long existingId, String name, Coordinates coordinates, Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, String ownerName) throws IndexOutOfBoundsException {
        Product newProduct = createProduct(name, coordinates, price, manufactureCost, unitOfMeasure,
                new Person(ownerName));

        Product existingProduct = null;

        for (Product product : products) {
            if (product.getId() == existingId) {
                existingProduct = product;
                break;
            }
        }
        if (existingProduct == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");

        existingProduct.setName(newProduct.getName());
        existingProduct.setCoordinates(coordinates);
        existingProduct.setPrice(price);
        existingProduct.setManufactureCost(manufactureCost);
        existingProduct.setUnitOfMeasure(unitOfMeasure);
        existingProduct.setOwner(new Person(ownerName));
    }

    /**
     * delete element by its id
     * 
     * @param existingId id of a product
     * @throws IndexOutOfBoundsException if id isn't in collection
     */
    public void deleteById(long existingId) throws IndexOutOfBoundsException {
        Product existingProduct = null;
        for (Product product : products) {
            if (product.getId() == existingId) {
                existingProduct = product;
                break;
            }
        }
        if (existingProduct == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");
        products.remove(existingProduct);
    }

    /** clear all collection */
    public void clear() {
        products.clear();
    }

    /**
     * Is an element gonna be max
     * 
     * @return boolean is Is an element gonna be max
     */
    public boolean isMax(String name, Coordinates coordinates, Double price, Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, String ownerName) {
        Product newProduct = createProduct(ownerName, coordinates, price, manufactureCost, unitOfMeasure,
                new Person(ownerName));
        var sorted = getSorted();
        var maxProduct = sorted.last();

        return maxProduct.compareTo(newProduct) == 1;
    }

    /**
     * Is an element gonna be min
     * 
     * @return boolean is Is an element gonna be min
     */
    public boolean isMin(String name, Coordinates coordinates, Double price, Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, String ownerName) {
        Product newProduct = createProduct(ownerName, coordinates, price, manufactureCost, unitOfMeasure,
                new Person(ownerName));
        var sorted = getSorted();
        var minProduct = sorted.first();

        return minProduct.compareTo(newProduct) == -1;
    }

    /**
     * get ids of element greater than given one
     * 
     * @return ArrayList of ids
     */
    public ArrayList<Long> getGreaterIds(String name, Coordinates coordinates, Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, String ownerName) {
        ArrayList<Long> greaterIds = new ArrayList<>();
        Product newProduct = createProduct(ownerName, coordinates, price, manufactureCost, unitOfMeasure,
                new Person(ownerName));

        for (Product p : products)
            if (p.compareTo(newProduct) == 1)
                greaterIds.add(p.getId());

        return greaterIds;
    }

    /**
     * get ids with the same unit of measure
     * 
     * @return ArrayList of ids with the same unit of measure
     */
    public ArrayList<Long> getIdsByUnitOfMeasure(UnitOfMeasure comparing) {
        ArrayList<Long> ids = new ArrayList<>();

        for (Product p : products)
            if (p.getUnitOfMeasure().equals(comparing))
                ids.add(p.getId());

        return ids;
    }

    /**
     * get any minimal product with minimal unit of measure
     * 
     * @return information about minimal product
     */
    public String getMinByUnitOfMeasure() {
        TreeSet<Integer> unitOfMeasures = new TreeSet<>();

        for (Product p : products) {
            int ordinal = p.getUnitOfMeasure().ordinal();
            unitOfMeasures.add(ordinal);
            if (ordinal == 0)
                break;
        }
        Product wanted = null;

        for (Product p : products) {
            if (p.getUnitOfMeasure().ordinal() == unitOfMeasures.first()) {
                wanted = p;
                break;
            }
        }
        return wanted.toString();
    }

    /**
     * get ids of elements with bigger owner
     * 
     * @return ArrayList of ids
     */
    public ArrayList<Long> getIdsGreaterThanOwner(Person owner) {
        ArrayList<Long> ids = new ArrayList<>();
        for (Product p : products) {
            if (p.getOwner().compareTo(owner) == 1)
                ids.add(p.getId());
        }
        return ids;
    }

    /** create product example */
    private Product createProduct(String name, Coordinates coordinates, Double price,
            Integer manufactureCost, UnitOfMeasure unitOfMeasure, Person owner) {
        long id = IdManager.getId();
        Date creationDate = getCurrentDate();
        return new Product(id, name, coordinates, creationDate, price, manufactureCost, unitOfMeasure, owner);
    }
}
