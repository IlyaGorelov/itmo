package Objects.Validators;

import Objects.Managers.IdManager;

import java.util.Scanner;

public class IdValidator extends Validator<Long> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Long x = Long.parseLong(value);
            if (!IdManager.isIdIn(x))
                throw new IllegalArgumentException("There is no such id");
            if (x < 0)
                throw new IllegalArgumentException("Id must be >0");

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

            if (!isValid(id, canBeNull))
                System.out.println("Incorrect input");

        } while (!isValid(id, canBeNull));

        return Long.parseLong(id);
    }

}
