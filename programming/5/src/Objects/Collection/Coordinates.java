package Objects.Collection;

/** Class representing coordinates */
public class Coordinates implements Comparable<Coordinates> {
    private Integer x; // Поле не может быть null
    private double y; // Значение поля должно быть больше -990

    /**
     * Constructor for class
     * 
     * @param x first coordinate
     * @param y second coordinate
     */
    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        if (x == null) {
            throw new IllegalArgumentException("x cannot be null");
        }
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * compares with other Coordinates
     * 
     * @param o other coordinates
     * @return 1 if this object is greater than other, 0 if objects are equal, -1 -
     *         if this object is less than other
     */
    @Override
    public int compareTo(Coordinates o) {
        int result = Integer.compare(x, o.x);
        if (result == 0)
            result = y > o.y ? (y == o.y ? 0 : 1) : -1;

        return result;
    }

    @Override
    public String toString() {
        return String.format("x: %d; y: %.2f", x, y);
    }

    /**
     * Parse toString (@see toString of this class) into new Coordinates
     * 
     * @param input String represantation of this object that is got from toString
     * @return new Coordinates
     */
    public static Coordinates parse(String input) throws NumberFormatException, NullPointerException {
        String[] parts = input.split("; ");
        Integer x = Integer.parseInt(parts[0].split(": ")[1]);
        double y = Double.parseDouble(parts[1].replace(",", ".").split(": ")[1]);

        return new Coordinates(x, y);

    }

}
