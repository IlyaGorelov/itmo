package Objects.Collection;

import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;

/** Class representing Person */
public class Person implements Comparable<Person> {
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Float height; // Поле не может быть null, Значение поля должно быть больше 0
    private EyeColor eyeColor; // Поле может быть null
    private HairColor hairColor; // Поле не может быть null
    private Country nationality; // Поле не может быть null
    private Location location; // Поле может быть null

    public Person(String name, Float height, EyeColor eyeColor, HairColor hairColor, Country nationality,
            Location location) {
        this.name = name;
        this.height = height;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
        this.location = location;
    }

    public Person(String name) {
        this.name = name;
        this.height = 180f;
        this.eyeColor = null;
        this.hairColor = HairColor.BLACK;
        this.nationality = Country.USA;
        this.location = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * compares with other Coordinates
     * 
     * @param o other coordinates
     * @return 1 if this object is greater than other, 0 if objects are equal, -1 -
     *         if this object is less than other
     */
    @Override
    public int compareTo(Person o) {
        int result = Integer.compare(name != null ? name.length() : 0, o.getName() != null ? o.getName().length() : 0);
        if (result == 0)
            result = height.compareTo(o.height);

        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "\tName: %s\n\tHeight: %.2f\n\tEye Color: %s\n\tHair Color: %s\n\tNationality: %s\n\tLocation: %s",
                name, height, eyeColor, hairColor, nationality, String.valueOf(location));
    }
}
