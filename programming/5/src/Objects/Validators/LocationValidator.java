package Objects.Validators;

import java.util.Scanner;

import Objects.Collection.Location;

public class LocationValidator extends Validator<Location> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            var location = Location.parse(value);
            if (location.getName().length() > 479)
                throw new IllegalArgumentException("Name length must be less than 479");
            return true;
        } catch (Exception e) {
            if (value.isBlank()) {
                if (canBeNull)
                    return true;
                else {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Location get(Scanner scanner, boolean canBeNull, String request) {
        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        StringValidator stringValidator = new StringValidator();
        Location location = new Location(null, null, 0, request);
        do {
            System.out.println(request);

            do {
                Double x = doubleValidator.get(scanner, true,
                        "Enter x coordinate (double). Type nothing if want to leave location as null: ");
                location.setX(x);
                if (x == null)
                    break;
            } while (!isValid(location.toString(), true));
            if (location.getX() == null)
                return null;

            do {
                Integer y = integerValidator.get(scanner, false, "Enter y coordinate (integer): ");
                location.setY(y);
            } while (!isValid(location.toString(), canBeNull));

            do {
                double z = doubleValidator.get(scanner, false, "Enter z coordinate (double): ");
                location.setZ(z);
            } while (!isValid(location.toString(), canBeNull));

            do {
                String name = stringValidator.get(scanner, false, "Enter name: ");
                location.setName(name);
            } while (!isValid(location.toString(), canBeNull));

        } while (!isValid(location.toString(), canBeNull));

        return location;
    }

}
