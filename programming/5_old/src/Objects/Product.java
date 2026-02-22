package Objects;

import Objects.Enums.UnitOfMeasure;

public class Product implements Comparable<Product> {
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

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for coordinates
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

    @Override
    public int compareTo(Product o) {
        int result = Double.compare(price, o.price);
        if (result == 0)
            result = this.creationDate.compareTo(o.creationDate);
        if (result == 0)
            result = this.coordinates.compareTo(o.coordinates);
        if (result == 0)
            result = Integer.compare(manufactureCost, o.manufactureCost);
        if (result == 0)
            result = owner.compareTo(o.owner);

        return result;

    }
}
