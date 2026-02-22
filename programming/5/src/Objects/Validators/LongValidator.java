package Objects.Validators;

import java.io.InputStream;
import java.util.Scanner;

public class LongValidator extends Validator<Long> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            Long.parseLong(value);
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
    public Long get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();
            if (!validate(value, canBeNull))
                System.out.println("Incorrect input");

        } while (!validate(value, canBeNull));

        return Long.parseLong(value);
    }

}
