package Objects.Validators;

import Objects.Enums.UnitOfMeasure;

import java.util.Scanner;

public class UnitValidator extends Validator<UnitOfMeasure> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if (!enumContains(value.toUpperCase()))
                throw new IllegalArgumentException("No such unit of measure");
            return true;
        } catch (Exception e) {
            if (value == null || value.isBlank() || value == "null") {
                if (canBeNull) {
                    return true;
                } else {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public UnitOfMeasure get(Scanner scanner, boolean canBeNull, String request) {
        StringValidator stringValidator = new StringValidator();
        String unit = "";
        do {
            System.out.println(request);
            unit = stringValidator.get(scanner, true,
                    "Enter name:\n" + getChoiceMenu());

        } while (!isValid(String.valueOf(unit), canBeNull));

        if (unit == null)
            return null;

        return UnitOfMeasure.valueOf(unit.toUpperCase());
    }

    private String getChoiceMenu() {
        String result = "";
        int i = 1;
        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
            result += String.format("%d) %s\n", i++, unit);
        }
        return result;
    }

    private boolean enumContains(String value) {
        for (UnitOfMeasure c : UnitOfMeasure.values()) {
            if (c.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

}
