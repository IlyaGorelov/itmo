package Objects;

public class Coordinates implements Comparable<Coordinates> {
    private Integer x; // Поле не может быть null
    private double y; // Значение поля должно быть больше -990

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
        if (y <= -990) {
            throw new IllegalArgumentException("y must be greater than -990");
        }
        this.y = y;
    }

    @Override
    public int compareTo(Coordinates o) {
        int result = Integer.compare(x, o.x);
        if (result == 0)
            result = y > o.y ? (y == o.y ? 0 : 1) : -1;

        return result;
    }

}
