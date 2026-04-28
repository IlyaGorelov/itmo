package Objects.Collection;

import Objects.Enums.UnitOfMeasure;

import java.io.Serializable;
import java.util.Locale;

/**
 * Class representing a Product - element of the collection
 */
public class Product implements Comparable<Product>, Serializable {
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть
    // уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.util.Date creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
    // автоматически
    private Double price; // Поле может быть null, Значение поля должно быть больше 0
    private Integer manufactureCost; // Поле не может быть null
    private UnitOfMeasure unitOfMeasure; // Поле может быть null
    private Person owner; // Поле может быть null

    public Product(long id, String name, Coordinates coordinates, java.util.Date creationDate,
                   Double price, Integer manufactureCost, UnitOfMeasure unitOfMeasure, Person owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.manufactureCost = manufactureCost;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public java.util.Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.util.Date creationDate) {
        this.creationDate = creationDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getManufactureCost() {
        return manufactureCost;
    }

    public void setManufactureCost(Integer manufactureCost) {
        this.manufactureCost = manufactureCost;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    /**
     * compares with other Coordinates
     *
     * @param o other coordinates
     * @return 1 if this object is greater than other, 0 if objects are equal, -1 -
     * if this object is less than other
     */
    @Override
    public int compareTo(Product o) {
        if (o == null) return 1;

        int result = Double.compare(price == null ? 0 : price, o.price == null ? 0 : o.price);
        if (result == 0)
            result = Integer.compare(manufactureCost, o.manufactureCost);

        return result;

    }

    @Override
    public String toString() {
        if (owner != null)
            return String.format(
                    "ID: %d\nName: %s\nCoordinates: %s\nCreation Date: %s\nPrice: %.4f\nManufacture Cost: %d\nUnit of Measure: %s\nOwner: \n%s",
                    id, name, coordinates.toString(), creationDate.toString(), price, manufactureCost,
                    unitOfMeasure, owner);
        else
            return String.format(
                    "ID: %d\nName: %s\nCoordinates: %s\nCreation Date: %s\nPrice: %.4f\nManufacture Cost: %d\nUnit of Measure: %s\nOwner: %s",
                    id, name, coordinates.toString(), creationDate.toString(), price, manufactureCost,
                    unitOfMeasure, owner);
    }

    public String getFuncString(boolean askForId) {
        if (askForId)
            return String.format(Locale.US, "$%d;%s;%d;%f;%s;%d;%s;%s", id, name, coordinates.getX(),
                    coordinates.getY(),
                    price == null ? "" : price, manufactureCost,
                    unitOfMeasure == null ? "" : String.valueOf(unitOfMeasure),
                    owner == null ? "" : owner.getFuncString());
        else
            return String.format(Locale.US, "%s;%d;%f;%s;%d;%s;%s", name, coordinates.getX(),
                    coordinates.getY(),
                    price == null ? "" : price, manufactureCost,
                    unitOfMeasure == null ? "" : String.valueOf(unitOfMeasure),
                    owner == null ? "" : owner.getFuncString());
    }
}
