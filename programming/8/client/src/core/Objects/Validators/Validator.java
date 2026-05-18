package core.Objects.Validators;

import java.util.Scanner;

/** check input on exceptions and other mistakes */
public abstract class Validator<T> {
    /**
     * checks String by casting it to the needed type
     * 
     * @param value     string for checking
     * @param canBeNull should method allow null value or not
     */
    public boolean isValid(String value, boolean canBeNull){
        return true;
    };

    public boolean isValid(T value, boolean canBeNull){
        return true;
    };

    /**
     * gives guaranteed output of needed type
     * 
     * @return needed type
     */
    public abstract T get(Scanner scanner, boolean canBeNull, String request);
}
