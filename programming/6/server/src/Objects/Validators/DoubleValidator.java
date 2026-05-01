package Objects.Validators;

import java.util.Scanner;

public class DoubleValidator extends Validator<Double> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (Exception e) {
            if (value.isBlank() | value.trim().equals("null")) {
                if (canBeNull)
                    return true;
                else {
                    System.out.println("Invalid number format | " + e.getMessage());
                    return false;
                }
            }
            System.out.println("Invalid number format | " + e.getMessage());
            return false;
        }
    }

    @Override
    public Double get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();

        } while (!isValid(value, canBeNull));

        if (value.isBlank())
            return null;

        return Double.parseDouble(value.trim());
    }

}
