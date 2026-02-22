package Objects;

public class Location implements Comparable<Location> {
    private Double x; // Поле не может быть null
    private Integer y; // Поле не может быть null
    private double z;
    private String name; // Длина строки не должна быть больше 479, Поле не может быть null

    public Location(Double x, Integer y, double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
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

    @Override
    public int compareTo(Location o) {
        int result = Double.compare(x, o.x);
        if (result == 0)
            result = Integer.compare(y, o.y);
        if (result == 0)
            result = z > o.z ? (z == o.z ? 0 : 1) : -1;
        return result;
    }
}
