package Objects.Validators;

import java.util.Scanner;

public class HeightValidator extends Validator<Float> {

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            Float height = Float.parseFloat(value);
            if (height <= 0)
                throw new IllegalArgumentException("Height must be greater than 0");

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
    public Float get(Scanner scanner, boolean canBeNull, String request) {
        String value = null;
        do {
            System.out.print(request);

            value = scanner.nextLine();

        } while (!isValid(value, canBeNull));

        if (value.isBlank())
            return null;

        return Float.parseFloat(value);
    }

}
