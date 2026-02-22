package Objects.Validators;

import java.util.Scanner;
import Objects.Managers.IdManager;

public class IdValidator extends Validator<Long> {

    @Override
    public boolean validate(String value, boolean canBeNull) {
        try {
            Long x = Long.parseLong(value);
            if (!IdManager.isIdIn(x))
                throw new IllegalArgumentException("There is no such id");

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
        StringValidator stringValidator = new StringValidator();
        String id = null;
        do {
            System.out.println(request);
            id = stringValidator.get(scanner, false, "");

            if (!validate(id, canBeNull))
                System.out.println("Incorrect input");

        } while (!validate(id, canBeNull));

        return Long.parseLong(id);
    }

}
