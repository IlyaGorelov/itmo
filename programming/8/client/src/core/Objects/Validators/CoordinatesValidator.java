package core.Objects.Validators;

import java.util.Scanner;

import Commons.Collection.Coordinates;
import Localization.I18n;

public class CoordinatesValidator extends Validator<Coordinates> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        return false;
    }

    @Override
    public boolean isValid(Coordinates value, boolean canBeNull) {
        try {
            if (value.getY() <= -990)
                throw new IllegalArgumentException(I18n.get("error.y"));
            return true;
        } catch (Exception e) {
            if (value==null) {
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
        Coordinates coordinates = new Coordinates(0, 0);
        do {
            System.out.println(request);

            do {
                Integer x = integerValidator.get(scanner, false, "Enter x coordinate (integer): ");
                coordinates.setX(x);
            } while (!isValid(coordinates, canBeNull));

            do {
                Double y = doubleValidator.get(scanner, false, "Enter y coordinate (double, must be > -990): ");
                coordinates.setY(y);
            } while (!isValid(coordinates, canBeNull));

        } while (!isValid(coordinates, canBeNull));

        return coordinates;
    }

}
