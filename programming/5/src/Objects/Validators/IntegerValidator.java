package Objects.Validators;

import java.util.Scanner;

public class IntegerValidator extends Validator<Integer> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            if (value.isBlank() || value.trim() == "null") {
                if (canBeNull)
                    return true;
                else {
                    System.out.println("Invalid number format | " + e.getMessage());
                    return false;
                }
            }
            System.out.println("Invalid number format | " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Integer get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();

        } while (!isValid(value, canBeNull));

        return Integer.parseInt(value.trim());
    }

}
