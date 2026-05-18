package core.Objects.Validators;

import Commons.UserData.User;
import gui.Objects.Helpers.ErrorMessageDeliverer;

import java.util.Scanner;

public class UserValidator extends Validator<User> {
    StringValidator stringValidator = new StringValidator();
    PasswordValidator passwordValidator = new PasswordValidator();

    @Override
    public boolean isValid(String value, boolean canBeNull) {
        return false;
    }

    @Override
    public boolean isValid(User value, boolean canBeNull) {
        try {
            if (!stringValidator.isValid(value.getLogin(), false))
                throw new IllegalArgumentException();
            if (!passwordValidator.isValid(value.getPassword(), false))
                throw new IllegalArgumentException();
            return true;
        } catch (Exception e) {
           // ErrorMessageDeliverer.add(e);
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public User get(Scanner scanner, boolean canBeNull, String request) {
        return null;
    }

}
