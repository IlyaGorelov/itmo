package Objects.Validators;

import java.util.Scanner;

import Objects.Connection.Receiver;

/** check input on exceptions and other mistakes */
public abstract class Validator<T> {
    /**
     * checks String by casting it to the needed type
     * 
     * @param value     string for checking
     * @param canBeNull should method allow null value or not
     */
    public abstract boolean isValid(String value, boolean canBeNull);

    /**
     * gives guaranteed output of needed type
     * 
     * @return needed type
     */
    public abstract T get(Scanner scanner, boolean canBeNull, String request);
}
