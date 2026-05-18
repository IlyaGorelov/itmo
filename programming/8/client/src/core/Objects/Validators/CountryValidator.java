package core.Objects.Validators;

import java.util.Scanner;

import core.Objects.Enums.Country;

public class CountryValidator extends Validator<Country> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if (enumContains(value.toUpperCase()) == false)
                throw new IllegalArgumentException("No such country");
            return true;
        } catch (Exception e) {
            if (value.isBlank() || value == "null") {
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
    public Country get(Scanner scanner, boolean canBeNull, String request) {
        StringValidator stringValidator = new StringValidator();
        String unit = "";
        do {
            System.out.println(request);
            unit = stringValidator.get(scanner, true,
                    "Enter name:\n" + getChoiceMenu());

        } while (!isValid(String.valueOf(unit), canBeNull));

        if (unit == null)
            return null;

        return Country.valueOf(unit.toUpperCase());
    }

    private String getChoiceMenu() {
        String result = "";
        int i = 1;
        for (Country unit : Country.values()) {
            result += String.format("%d) %s\n", i++, unit);
        }
        return result;
    }

    private boolean enumContains(String value) {
        for (Country c : Country.values()) {
            if (c.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

}
