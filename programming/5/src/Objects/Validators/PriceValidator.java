package Objects.Validators;

import java.io.InputStream;
import java.util.Scanner;

public class PriceValidator extends Validator<Double> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            Double x = Double.parseDouble(value);
            if (x <= 0)
                throw new IllegalArgumentException();
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
    public Double get(Scanner scanner, boolean canBeNull, String request) {
        Double value = null;
        DoubleValidator doubleValidator = new DoubleValidator();
        do {
            System.out.print(request);
            value = doubleValidator.get(scanner, canBeNull, "Enter double: ");
            if (!validate(value.toString(), canBeNull))
                System.out.println("Incorrect input");

        } while (!validate(value.toString(), canBeNull));

        return value;
    }

}
