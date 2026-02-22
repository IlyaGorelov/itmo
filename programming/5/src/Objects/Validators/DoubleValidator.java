package Objects.Validators;

import java.util.Scanner;

public class DoubleValidator extends Validator<Double> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            Double.parseDouble(value);
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
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();

        } while (!validate(value, canBeNull));

        if (value.isBlank())
            return null;

        return Double.parseDouble(value);
    }

}
