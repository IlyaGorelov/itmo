package Objects.Validators;

import Commons.Enums.HairColor;

import java.util.Scanner;

public class HairValidator extends Validator<HairColor> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if (!enumContains(value.toUpperCase()))
                throw new IllegalArgumentException("No such hair color");
            return true;
        } catch (Exception e) {
            if (value.isBlank() || value.equals("null")) {
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
    public HairColor get(Scanner scanner, boolean canBeNull, String request) {
        StringValidator stringValidator = new StringValidator();
        String unit = "";
        do {
            System.out.println(request);
            unit = stringValidator.get(scanner, true,
                    "Enter name:\n" + getChoiceMenu());

        } while (!isValid(String.valueOf(unit), canBeNull));

        if (unit == null)
            return null;

        return HairColor.valueOf(unit.toUpperCase());
    }

    private String getChoiceMenu() {
        String result = "";
        int i = 1;
        for (HairColor unit : HairColor.values()) {
            result += String.format("%d) %s\n", i++, unit);
        }
        return result;
    }

    private boolean enumContains(String value) {
        for (HairColor c : HairColor.values()) {
            if (c.name().equals(value)) {
                return true;
            }
        }

        return false;
    }

}
