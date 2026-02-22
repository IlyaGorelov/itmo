package Objects.Validators;

import java.util.Scanner;

import Objects.Collection.Coordinates;

public class CoordinatesValidator extends Validator<Coordinates> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            var coords = Coordinates.parse(value);
            if (coords.getY() <= -990)
                throw new IllegalArgumentException("Y must be greater than -990");
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
    public Coordinates get(Scanner scanner, boolean canBeNull, String request) {
        IntegerValidator integerValidator = new IntegerValidator();
        DoubleValidator doubleValidator = new DoubleValidator();
        Coordinates coordinates = null;
        do {
            System.out.println(request);
            Integer x = integerValidator.get(scanner, false, "Enter x coordinate (integer): ");
            Double y = doubleValidator.get(scanner, false, "Enter y coordinate (double): ");

            coordinates = new Coordinates(x, y);
        } while (!validate(coordinates.toString(), canBeNull));

        return coordinates;
    }

}
