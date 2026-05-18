package core.Objects.Validators;

import gui.Objects.Helpers.ErrorMessageDeliverer;

import java.util.Scanner;

public class StringValidator extends Validator<String> {
    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if (value.isBlank()) {
                if (canBeNull)
                    return true;
                else {
                    throw new IllegalArgumentException("It can't be null");
                }
            }
            return true;
        } catch (Exception e) {
            ErrorMessageDeliverer.add(e,ErrorMessageDeliverer.validation);
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public String get(Scanner scanner, boolean canBeNull, String request) {
        String name = "";
        do {
            System.out.print(request);
            name = scanner.nextLine();
        } while (!isValid(name, canBeNull));

        if (name.isBlank())
            return null;

        return name.trim();
    }

}
