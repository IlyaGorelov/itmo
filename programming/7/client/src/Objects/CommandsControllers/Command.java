package Objects.CommandsControllers;

import java.io.Serializable;
import java.util.Scanner;

import Objects.Connection.CustomPackage;

/**
 * Abstract class representing a command
 */
public abstract class Command implements Serializable {
    /**
     * An argument of a command presented in one line format
     */
    private String argument;
    private String complexArgument;
    /**
     * Boolean indicates this command requires argument or not
     */
    private boolean hasArgument = false;
    private boolean hasComplexArgument = false;

    private transient Scanner scanner;

    public Command(boolean hasArgument, boolean hasComplexArgument) {
        this.hasArgument = hasArgument;
        this.hasComplexArgument = hasComplexArgument;
    }

    public Command() {
        super();
        this.hasArgument = false;
    }

    public abstract String getName();

    public boolean getHasArgument() {
        return hasArgument;
    }

    public boolean getHasComplexArgument() {
        return hasComplexArgument;
    }

    public abstract Object getRelevantObject();

    public abstract String getRelevantAnswer(CustomPackage pack);

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public String getComplexArgument() {
        return complexArgument;
    }

    public void setComplexArgument(String argument) {
        this.complexArgument = argument;
    }

    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        if (actuallyHasArgument != hasArgument)
            throw new IllegalArgumentException("Invalid number of arguments!");
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public Scanner getScanner() {
        return scanner;
    }

}
