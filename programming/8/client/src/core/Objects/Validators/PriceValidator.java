package core.Objects.Validators;

import Localization.I18n;
import gui.Objects.Helpers.ErrorMessageDeliverer;

import java.util.Scanner;

public class PriceValidator extends Validator<Double> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Double x = Double.parseDouble(value.trim());
            if (x <= 0)
                throw new IllegalArgumentException(I18n.get("error.price"));
            return true;
        } catch (Exception e) {
            if (value.isBlank() || value.trim() == "null") {
                if (canBeNull)
                    return true;
                else {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
            ErrorMessageDeliverer.add(e);
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Double get(Scanner scanner, boolean canBeNull, String request) {
        Double value = null;
        DoubleValidator doubleValidator = new DoubleValidator();
        do {
            System.out.print(request);
            value = doubleValidator.get(scanner, canBeNull, "");
        } while (!isValid(String.valueOf(value), canBeNull));

        return value;
    }

}
