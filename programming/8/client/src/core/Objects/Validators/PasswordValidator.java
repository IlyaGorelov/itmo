package core.Objects.Validators;

import gui.Objects.Helpers.ErrorMessageDeliverer;

import java.util.Scanner;

public class PasswordValidator extends Validator<String> {
    @Override
    public boolean isValid(String value, boolean canBeNull) {
        try {
            if(!hasUpperCase(value))
                throw new IllegalArgumentException("Password must contain at least 1 Upper Case character");

            if(!hasDigit(value))
                throw new IllegalArgumentException("Password must contain at least 1 number");

            if(!hasSpecSymbol(value))
                throw new IllegalArgumentException("Password must contain at least 1 spec symbol: $%!@ ...");

            if(!hasDecentLength(value))
                throw new IllegalArgumentException("Password length must be at least 8");
            return true;
        } catch (Exception e) {
            ErrorMessageDeliverer.add(e);
            System.out.println(e.getMessage());
            return false;
        }

    }

    private boolean hasUpperCase(String string){
        return string.chars().anyMatch(Character::isUpperCase);
    }

    private boolean hasDigit(String string){
        return string.chars().anyMatch(Character::isDigit);
    }

    private boolean hasSpecSymbol(String string){
        return !string.chars().allMatch(Character::isLetterOrDigit);
    }

    private boolean hasDecentLength(String string){
        return string.length()>=8;
    }

    @Override
    public String get(Scanner scanner, boolean canBeNull, String request) {
        String password = "";
        do {
            System.out.print(request);
            password = scanner.nextLine();
        } while (!isValid(password, canBeNull));

        if (password.isBlank())
            return null;

        return password;
    }

}
