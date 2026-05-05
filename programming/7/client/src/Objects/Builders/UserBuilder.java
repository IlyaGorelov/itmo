package Objects.Builders;

import Objects.Collection.Coordinates;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.UserData.User;
import Objects.Validators.*;

import java.util.Date;
import java.util.Scanner;

public class UserBuilder extends Builder<User> {
    StringValidator stringValidator = new StringValidator();
    PasswordValidator passwordValidator = new PasswordValidator();

    @Override
    public User build(Scanner scanner) {
        String login = stringValidator.get(scanner, false, "Enter login: ");
        String password = passwordValidator.get(scanner, false, "Enter password: ");

        return new User(0,login,password) ;

    }

}
