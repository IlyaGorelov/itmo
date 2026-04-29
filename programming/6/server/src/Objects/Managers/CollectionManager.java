package Objects.Managers;

import Objects.Collection.*;
import Objects.CommandsControllers.Commands.*;
import Objects.CommandsControllers.History;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Validators.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class that controls collection
 */
public class CollectionManager {
    /**
     * colllection we work with
     */
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
                String id = record.get(CSVManager.Headers.id);
                try {
                    if (IdManager.isIdIn(Long.parseLong(id)))
                        throw new IllegalArgumentException(String.format("Id is taken\nInvalid value for %s in row %d",
                                CSVManager.Headers.id, record.getRecordNumber()));

                    if (Long.parseLong(id) < 0)
                        throw new IllegalArgumentException(
                                String.format("Id must be non negative\nInvalid value for %s in row %d",
                                        CSVManager.Headers.id, record.getRecordNumber()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            String.format("Id must be number\nInvalid value for %s in row %d",
                                    CSVManager.Headers.id, record.getRecordNumber()));
                }

                String name = record.get(CSVManager.Headers.name);
                if (!stringValidator.isValid(String.valueOf(name), false))
                    throw new IllegalArgumentException("Product name can't be null");

                String x = record.get(CSVManager.Headers.x);
                if (!integerValidator.isValid(String.valueOf(x), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            CSVManager.Headers.x, record.getRecordNumber()));

                String y = record.get(CSVManager.Headers.y);
                if (!doubleValidator.isValid(String.valueOf(y), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            CSVManager.Headers.y, record.getRecordNumber()));

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
                                    CSVManager.Headers.creationDate, record.getRecordNumber()));

                String price = record.get(CSVManager.Headers.price);
                if (!priceValidator.isValid(String.valueOf(price), true))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            CSVManager.Headers.price, record.getRecordNumber()));

                String manufactureCost = record.get(CSVManager.Headers.manufactureCost);
                if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            CSVManager.Headers.manufactureCost, record.getRecordNumber()));

                String unitOfMeasure = record.get(CSVManager.Headers.unitOfMeasure);
                if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true))
                    throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                            CSVManager.Headers.unitOfMeasure, record.getRecordNumber()));

                String ownerName = record.get(CSVManager.Headers.ownerName);
                if (ownerName == null) {
                    products.add(
                            new Product(Long.parseLong(id), name, coordinates, creationDate,
                                    price != null ? Double.parseDouble(price) : null,
                                    Integer.parseInt(manufactureCost),
                                    unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                                    null));
                    IdManager.addId(Long.parseLong(id));
                } else {
                    String height = record.get(CSVManager.Headers.height);
                    if (!heightValidator.isValid(String.valueOf(height), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                CSVManager.Headers.height, record.getRecordNumber()));

                    String eyeColor = record.get(CSVManager.Headers.eyeColor);
                    if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                CSVManager.Headers.eyeColor, record.getRecordNumber()));

                    String hairColor = record.get(CSVManager.Headers.hairColor);
                    if (!hairValidator.isValid(String.valueOf(hairColor), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                CSVManager.Headers.hairColor, record.getRecordNumber()));

                    String nationality = record.get(CSVManager.Headers.nationality);
                    if (!countryValidator.isValid(String.valueOf(nationality), false))
                        throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                CSVManager.Headers.nationality, record.getRecordNumber()));

                    String locX = record.get(CSVManager.Headers.locX);
                    String locY = record.get(CSVManager.Headers.locY);
                    String locZ = record.get(CSVManager.Headers.locZ);
                    String locName = record.get(CSVManager.Headers.locName);
                    Location location = null;
                    if (locX != null) {
                        if (!doubleValidator.isValid(locX, false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    CSVManager.Headers.locX, record.getRecordNumber()));

                        if (!integerValidator.isValid(String.valueOf(locY), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    CSVManager.Headers.locY, record.getRecordNumber()));

                        if (!doubleValidator.isValid(String.valueOf(locZ), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    CSVManager.Headers.locZ, record.getRecordNumber()));

                        if (!stringValidator.isValid(String.valueOf(locName), false))
                            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                                    CSVManager.Headers.locName, record.getRecordNumber()));

                        location = new Location(Double.parseDouble(locX),
                                Integer.parseInt(locY),
                                Double.parseDouble(locZ),
                                locName);

                        if (!locationValidator.isValid(String.valueOf(location), false))
                            throw new IllegalArgumentException(String.format("Invalid value for location in row %d",
                                    record.getRecordNumber()));
                    }

                    products.add(
                            new Product(Long.parseLong(id), name, coordinates, creationDate,
                                    price != null ? Double.parseDouble(price) : null,
                                    Integer.parseInt(manufactureCost),
                                    unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                                    new Person(ownerName, Float.parseFloat(height),
                                            eyeColor != null ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                                            HairColor.valueOf(hairColor.toUpperCase()),
                                            Country.valueOf(nationality.toUpperCase()),
                                            location)));
                    IdManager.addId(Long.parseLong(id));
                }
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
    public Product addElement(
            String name,
            Coordinates coordinates,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person person) {
        Product p = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
        String command = new Add(this).getName() + " {" + p.getFuncString(true) + "}";
        String antiCommand = new Remove(this).getName() + " " + p.getId();
        History.add(command, antiCommand);
        if (CommandManager.getCountOfUnrecorded() == 0)
            History.clearUndoHistory();
        CommandManager.minusUnrecordedCommand();
        products.add(p);
        return p;
    }

    /**
     * add element to collection with id
     */
    public Product addElement(
            Long id,
            String name,
            Coordinates coordinates,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person person) {
        Product p = createProduct(id, name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
        String command = new Add(this).getName() + " {" + p.getFuncString(true) + "}";
        String antiCommand = new Remove(this).getName() + " " + p.getId();
        History.add(command, antiCommand);
        if (CommandManager.getCountOfUnrecorded() == 0)
            History.clearUndoHistory();
        CommandManager.minusUnrecordedCommand();
        products.add(p);
        return p;
    }

    /**
     * update element of the collection, find element by id
     */
    public Product updateElement(
            long existingId,
            String name,
            Coordinates coordinates,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person person) throws IndexOutOfBoundsException {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);

        String command = new Update(this).getName() + " " + existingId + " {" + newProduct.getFuncString(false) + "}";
        Product existingProduct = null;
        existingProduct = products.stream()
                .filter(p -> p.getId() == existingId)
                .findFirst()
                .orElse(null);

        if (existingProduct == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");

        String antiCommand = new Update(this).getName() + " " + existingId + " {" + existingProduct.getFuncString(false)
                + "}";

        History.add(command, antiCommand);

        existingProduct.setName(newProduct.getName());
        existingProduct.setCoordinates(coordinates);
        existingProduct.setPrice(price);
        existingProduct.setManufactureCost(manufactureCost);
        existingProduct.setUnitOfMeasure(unitOfMeasure);
        if (CommandManager.getCountOfUnrecorded() == 0)
            History.clearUndoHistory();
        CommandManager.minusUnrecordedCommand();
        existingProduct.setOwner(person);

        return existingProduct;
    }

    /**
     * delete element by its id
     *
     * @param existingId id of a product
     * @throws IndexOutOfBoundsException if id isn't in collection
     */
    public Product deleteById(long existingId) throws IndexOutOfBoundsException {
        String command = new Remove(this).getName() + " " + existingId;
        Product existingProduct = products.stream()
                .filter(p -> p.getId() == existingId)
                .findFirst()
                .orElse(null);

        if (existingProduct == null)
            throw new IndexOutOfBoundsException("There is no element with such id!");
        String antiCommand = new Add(null).getName() + " {" + existingProduct.getFuncString(true) + "}";
        History.add(command, antiCommand);
        products.remove(existingProduct);
        IdManager.removeId(existingId);
        if (CommandManager.getCountOfUnrecorded() == 0)
            History.clearUndoHistory();
        CommandManager.minusUnrecordedCommand();
        // System.out.println("Successfully deleted product with id " + existingId);
        return existingProduct;
    }

    /**
     * clear all collection
     */
    public void clear() {
        String antiCommand = "";
        for (Product product : products) {
            antiCommand += new Add(null).getName() + " {" + product.getFuncString(true) + "}\n";
        }
        products.clear();
        IdManager.clear();
        String command = new Clear(null).getName();
        if (CommandManager.getCountOfUnrecorded() == 0)
            History.clearUndoHistory();
        CommandManager.minusUnrecordedCommand();
        History.add(command, antiCommand);
    }

    /**
     * Is an element gonna be max
     *
     * @return boolean is the element gonna be max
     */
    public boolean isMax(
            String name,
            Coordinates coordinates,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person person) {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
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
    public boolean isMin(
            String name,
            Coordinates coordinates,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person person) {
        Product newProduct = createProduct(name, coordinates, getCurrentDate(), price, manufactureCost, unitOfMeasure,
                person);
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
    public Product[] removeGreaters(
            Double price,
            Integer manufactureCost) {
        Product newProduct = createProduct("", new Coordinates(0, 0), getCurrentDate(), price, manufactureCost,
                null,
                null);

        String command = new RemoveGreater(null).getName() + " {" + newProduct.getFuncString(false) + "}";
        StringBuilder antiCommand = new StringBuilder();

        Product[] productsToRemove = products.stream()
                .filter(p -> p.compareTo(newProduct) == 1)
                .toArray(Product[]::new);

        Arrays.stream(productsToRemove)
                .forEach(p -> {
                    products.remove(p);
                    IdManager.removeId(p.getId());
                    antiCommand.append(new Add(null).getName())
                            .append(" {")
                            .append(p.getFuncString(false))
                            .append("}")
                            .append("\n");
                    if (CommandManager.getCountOfUnrecorded() == 0)
                        History.clearUndoHistory();
                    CommandManager.minusUnrecordedCommand();
                });

        History.add(command, antiCommand.toString());
        return productsToRemove;
    }

    /**
     * get ids with the same unit of measure
     *
     * @return Array of result Products
     */
    public Product[] removeByUnitOfMeasure(UnitOfMeasure comparing) {
        String command = new RemoveByUnitOfMeasure(null).getName() + " " + comparing;
        StringBuilder antiCommand = new StringBuilder();

        Product[] result = products.stream()
                .filter(p -> Objects.equals(p.getUnitOfMeasure(), comparing))
                .toArray(Product[]::new);

        Arrays.stream(result).forEach(p -> {
            products.remove(p);
            IdManager.removeId(p.getId());
            antiCommand.append(new Add(null).getName())
                    .append(" {")
                    .append(p.getFuncString(true))
                    .append("}");

            if (CommandManager.getCountOfUnrecorded() == 0) {
                History.clearUndoHistory();
            }
            CommandManager.minusUnrecordedCommand();
        });

        History.add(command, antiCommand.toString());
        return result;
    }

    /**
     * get any minimal product with minimal unit of measure
     *
     * @return information about minimal product
     */
    public Product getMinByUnitOfMeasure() {
        TreeSet<Integer> unitOfMeasures = new TreeSet<>();

        for (Product p : products) {
            int ordinal = p.getUnitOfMeasure() == null ? 1000000 : p.getUnitOfMeasure().ordinal();
            unitOfMeasures.add(ordinal);
            if (ordinal == 0)
                break;
        }

        Product wanted = products.stream().filter(
                        p -> (p.getUnitOfMeasure() == null ? 1000000 : p.getUnitOfMeasure().ordinal()) == unitOfMeasures
                                .first())
                .findFirst().orElse(null);

        return wanted;
    }

    /**
     * get ids of elements with bigger owner
     *
     * @return ArrayList of ids
     */
    public ArrayList<Long> getIdsGreaterThanOwner(Person owner) {
        var ids = products.stream().filter(p -> {
            if (owner == null)
                return p.getOwner() != null;
            else
                return p.getOwner().compareTo(owner) == 1;

        }).map(Product::getId).collect(Collectors.toCollection(ArrayList<Long>::new));
        return ids;

    }

    /**
     * create product example
     */
    private Product createProduct(
            String name,
            Coordinates coordinates,
            Date creationDate,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person owner) {
        long id = IdManager.getId();
        return new Product(id, name, coordinates, creationDate, price, manufactureCost, unitOfMeasure, owner);
    }

    /**
     * create product example with give id
     */
    private Product createProduct(
            long id,
            String name,
            Coordinates coordinates,
            Date creationDate,
            Double price,
            Integer manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Person owner) {
        IdManager.addId(id);
        return new Product(id, name, coordinates, creationDate, price, manufactureCost, unitOfMeasure, owner);
    }
}
