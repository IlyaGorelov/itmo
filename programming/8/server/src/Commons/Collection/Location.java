package Commons.Collection;

import java.io.Serializable;
import java.util.Locale;

/**
 * Class representing location of a person
 */
public class Location implements Comparable<Location>, Serializable, Cloneable {
    private Double x; // Поле не может быть null
    private Integer y; // Поле не может быть null
    private double z;
    private String name; // Длина строки не должна быть больше 479, Поле не может быть null

    /**
     * Constructor
     *
     * @param x    coordinate x
     * @param y    coordinate y
     * @param z    coordinate z
     * @param name name of the location
     */
    public Location(Double x, Integer y, double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public static int getCountOfEditableFields() {
        return 4;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * compares with other Location
     *
     * @param o other location
     * @return 1 if this object is greater than other, 0 if objects are equal, -1 -
     * if this object is less than other
     */
    @Override
    public int compareTo(Location o) {
        int result = Double.compare(x, o.x);
        if (result == 0)
            result = Integer.compare(y, o.y);
        if (result == 0)
            result = z > o.z ? (z == o.z ? 0 : 1) : -1;
        return result;
    }

    @Override
    public String toString() {
        return String.format("x: %.2f; y: %d; z: %.2f; name: %s", x, y, z, name);
    }

    /**
     * Parse toString (@see toString of this class) into new Location
     *
     * @param input String represantation of this object that is got from toString
     * @return new Coordinates
     */
    public static Location parse(String input) throws NumberFormatException, NullPointerException {
        String[] parts = input.split("; ");
        Double x = Double.parseDouble(parts[0].replace(",", ".").split(": ")[1]);
        Integer y = Integer.parseInt(parts[1].replace(",", ".").split(": ")[1]);
        double z = Double.parseDouble(parts[2].replace(",", ".").split(": ")[1]);
        String name = parts[3].split(": ")[1];

        return new Location(x, y, z, name);

    }

    public String getFuncString() {
        return String.format(Locale.US, "%f;%d;%f;%s", x, y, z, name);
    }

    @Override
    public Location clone() {
        try {
            Location clone = (Location) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
