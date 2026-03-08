package Objects.Managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Collection.ProductsComparator;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Validators.*;

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
        StringValidator stringValidator = new StringValidator();
        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
        PriceValidator priceValidator = new PriceValidator();
        UnitValidator unitValidator = new UnitValidator();
        HeightValidator heightValidator = new HeightValidator();
        EyeValidator eyeValidator = new EyeValidator();
        HairValidator hairValidator = new HairValidator();
        CountryValidator countryValidator = new CountryValidator();
        LocationValidator locationValidator = new LocationValidator();

        this.path = System.getenv(envKey);
        var CSVRecords = csvManager.read(path);

        for (var record : CSVRecords) {
            try {
                String name = record.get(CSVManager.Headers.name);
                if (!stringValidator.isValid(String.valueOf(name), false))
                    throw new IllegalArgumentException("Product name can't be null");

                String x = record.get(CSVManager.Headers.x);
                if (!integerValidator.isValid(String.valueOf(x), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            String.valueOf(CSVManager.Headers.x), record.getRecordNumber()));

                String y = record.get(CSVManager.Headers.y);
                if (!doubleValidator.isValid(String.valueOf(y), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            String.valueOf(CSVManager.Headers.y), record.getRecordNumber()));

                Coordinates coordinates = new Coordinates(Integer.parseInt(x),
                        Double.parseDouble(y));
                if (!coordinatesValidator.isValid(String.valueOf(coordinates), false))
                    throw new IllegalArgumentException(
                            String.format("Invalid value for coordinates in row %d",
                                    record.getRecordNumber()));

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                calendar.setTime(sdf.parse(record.get(CSVManager.Headers.creationDate)));
                Date creationDate = calendar.getTime();

                if (!calendar.before(Calendar.getInstance()))
                    throw new IllegalArgumentException(
                            String.format("Invalid value for %s in row %d. Date hasn't come yet",
                                    String.valueOf(CSVManager.Headers.creationDate), record.getRecordNumber()));

                String price = record.get(CSVManager.Headers.price);
                if (!priceValidator.isValid(String.valueOf(price), true))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            String.valueOf(CSVManager.Headers.price), record.getRecordNumber()));

                String manufactureCost = record.get(CSVManager.Headers.manufactureCost);
                if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            String.valueOf(CSVManager.Headers.manufactureCost), record.getRecordNumber()));

                String unitOfMeasure = record.get(CSVManager.Headers.unitOfMeasure);
                if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            String.valueOf(CSVManager.Headers.unitOfMeasure), record.getRecordNumber()));

                String ownerName = record.get(CSVManager.Headers.ownerName);
                if (ownerName == null)
                    products.add(
                            createProduct(name, coordinates, creationDate,
                                    price != null ? Double.parseDouble(price) : null,
                                    Integer.parseInt(manufactureCost),
                                    unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                                    null));
                else {
                    String height = record.get(CSVManager.Headers.height);
                    if (!heightValidator.isValid(String.valueOf(height), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                String.valueOf(CSVManager.Headers.height), record.getRecordNumber()));

                    String eyeColor = record.get(CSVManager.Headers.eyeColor);
                    if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                String.valueOf(CSVManager.Headers.eyeColor), record.getRecordNumber()));

                    String hairColor = record.get(CSVManager.Headers.hairColor);
                    if (!hairValidator.isValid(String.valueOf(hairColor), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                String.valueOf(CSVManager.Headers.hairColor), record.getRecordNumber()));

                    String nationality = record.get(CSVManager.Headers.nationality);
                    if (!countryValidator.isValid(String.valueOf(nationality), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                String.valueOf(CSVManager.Headers.nationality), record.getRecordNumber()));

                    String locX = record.get(CSVManager.Headers.locX);
                    String locY = record.get(CSVManager.Headers.locY);
                    String locZ = record.get(CSVManager.Headers.locZ);
                    String locName = record.get(CSVManager.Headers.locName);
                    Location location = null;
                    if (locX != null) {
                        if (!doubleValidator.isValid(String.valueOf(locX), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    String.valueOf(CSVManager.Headers.locX), record.getRecordNumber()));

                        if (!integerValidator.isValid(String.valueOf(locY), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    String.valueOf(CSVManager.Headers.locY), record.getRecordNumber()));

                        if (!doubleValidator.isValid(String.valueOf(locZ), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    String.valueOf(CSVManager.Headers.locZ), record.getRecordNumber()));

                        if (!stringValidator.isValid(String.valueOf(locName), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    String.valueOf(CSVManager.Headers.locName), record.getRecordNumber()));

                        location = new Location(Double.parseDouble(locX),
                                Integer.parseInt(locY),
                                Double.parseDouble(locZ),
                                locName);

                        if (!locationValidator.isValid(String.valueOf(location), false))
                            throw new IllegalArgumentException(String.format("Invalid value for location in row %d",
                                    record.getRecordNumber()));
                    }

                    products.add(
                            createProduct(name, coordinates, creationDate,
                                    price != null ? Double.parseDouble(price) : null,
                                    Integer.parseInt(manufactureCost),
                                    unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                                    new Person(ownerName, Float.parseFloat(height),
                                            eyeColor != null ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                            HairColor.valueOf(hairColor.toUpperCase()),
                                            Country.valueOf(nationality.toUpperCase()),
                                            location)));
                }
            } catch (Exception e) {
                if (e.getMessage() != null)
                    System.out.println(e.getMessage());
                System.out.println("Skip row\n");
            }
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
     *         /**
     *         returns current date
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
            record.add(p.getId());
            record.add(p.getName());
            record.add(p.getCoordinates().getX());
            record.add(p.getCoordinates().getY());
            record.add(p.getCreationDate());
            record.add(p.getPrice());
            record.add(p.getManufactureCost());
            record.add(p.getUnitOfMeasure());
            if (p.getOwner() == null) {
                for (int index = 0; index < 8; index++) {
                    record.add(null);
                }
            } else {

                record.add(p.getOwner().getName());
                record.add(p.getOwner().getHeight());
                record.add(p.getOwner().getEyeColor());
                record.add(p.getOwner().getHairColor());
                record.add(p.getOwner().getNationality());
                if (p.getOwner().getLocation() == null) {
                    for (int index = 0; index < 4; index++) {
                        record.add(null);
                    }
                } else {
                    record.add(p.getOwner().getLocation().getX());
                    record.add(p.getOwner().getLocation().getY());
                    record.add(p.getOwner().getLocation().getZ());
                    record.add(p.getOwner().getLocation().getName());
                }
            }
            records.add(record);
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
            UnitOfMeasure unitOfMeasure, Person person) {
        Product p = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
        products.add(p);
    }

    /** update element of the collection, find element by id */
    public void updateElement(long existingId, String name, Coordinates coordinates, Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure, Person person) throws IndexOutOfBoundsException {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);

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
        existingProduct.setOwner(person);
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
        IdManager.removeId(existingId);
        System.out.println("Successfully deleted product with id " + existingId);
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
            UnitOfMeasure unitOfMeasure, Person person) {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
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
            UnitOfMeasure unitOfMeasure, Person person) {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
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
            UnitOfMeasure unitOfMeasure, Person person) {
        ArrayList<Long> greaterIds = new ArrayList<>();
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost,
                unitOfMeasure,
                person);

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
            int ordinal = p.getUnitOfMeasure() == null ? 1000000 : p.getUnitOfMeasure().ordinal();
            unitOfMeasures.add(ordinal);
            if (ordinal == 0)
                break;
        }
        Product wanted = null;

        for (Product p : products) {
            if ((p.getUnitOfMeasure() == null ? 100000 : p.getUnitOfMeasure().ordinal()) == unitOfMeasures.first()) {
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
            if (owner == null) {
                if (p.getOwner() != null)
                    ids.add(p.getId());
            } else {
                if (owner != null)
                    if (p.getOwner().compareTo(owner) == 1)
                        ids.add(p.getId());
            }
        }
        return ids;
    }

    /** create product example */
    private Product createProduct(String name, Coordinates coordinates, Date creationDate, Double price,
            Integer manufactureCost, UnitOfMeasure unitOfMeasure, Person owner) {
        long id = IdManager.getId();
        return new Product(id, name, coordinates, creationDate, price, manufactureCost, unitOfMeasure, owner);
    }
}
