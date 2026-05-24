package core.Objects.Validators;

import gui.Objects.Helpers.ErrorMessageDeliverer;
import Localization.I18n;

import java.util.Scanner;

public class DoubleValidator extends Validator<Double> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Double.parseDouble(value.trim());
            return true;
        } catch (Exception e) {
            if (value.isBlank() | value.trim() == "null") {
                if (canBeNull)
                    return true;
                else {
                    ErrorMessageDeliverer.add(new IllegalArgumentException("Field is required"));
                    System.out.println(I18n.get("error.nformat") + " | " + e.getMessage());
                    return false;
                }
            }
            ErrorMessageDeliverer.add(new IllegalArgumentException("Invalid number format | " + e.getMessage()));
            System.out.println(I18n.get("error.nformat") + " | " + e.getMessage());
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
