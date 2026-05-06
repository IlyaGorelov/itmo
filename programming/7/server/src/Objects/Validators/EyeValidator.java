package Objects.Validators;

import Objects.Enums.EyeColor;

import java.util.Scanner;

public class EyeValidator extends Validator<EyeColor> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if (!enumContains(value.toUpperCase()))
                throw new IllegalArgumentException("No such eye color");
            return true;
        } catch (Exception e) {
            if (value == null) {
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
    public EyeColor get(Scanner scanner, boolean canBeNull, String request) {
        StringValidator stringValidator = new StringValidator();
        String unit = "";
        do {
            System.out.println(request);
            unit = stringValidator.get(scanner, true,
                    "Enter name:\n" + getChoiceMenu());

        } while (!isValid(String.valueOf(unit), canBeNull));

        if (unit == null)
            return null;

        return EyeColor.valueOf(unit.toUpperCase());
    }

    private String getChoiceMenu() {
        String result = "";
        int i = 1;
        for (EyeColor unit : EyeColor.values()) {
            result += String.format("%d) %s\n", i++, unit);
        }
        return result;
    }

    private boolean enumContains(String value) {
        for (EyeColor c : EyeColor.values()) {
            if (c.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

}
