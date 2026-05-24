package core.Objects.Validators;

import Localization.I18n;

import java.util.Scanner;

public class LongValidator extends Validator<Long> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
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
            System.out.println(I18n.get("error.nformat") + " | " + e.getMessage());
            return false;
        }
    }

    @Override
    public Long get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();
            if (!isValid(value, canBeNull))
                System.out.println("Incorrect input");

        } while (!isValid(value, canBeNull));

        return Long.parseLong(value);
    }

}
