package Objects.Validators;

import java.util.Scanner;

public class IntegerValidator extends Validator<Integer> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            Integer.parseInt(value);
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
            System.out.println(e.getMessage() + " | Invalid format");
            return false;
        }
    }

    @Override
    public Integer get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();

        } while (!validate(value, canBeNull));

        return Integer.parseInt(value);
    }

}
