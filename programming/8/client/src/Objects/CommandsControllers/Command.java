package Objects.CommandsControllers;

import java.io.Serializable;
import java.util.Scanner;

import Objects.Connection.CustomPackage;

/**
 * Abstract class representing a command
 */
public abstract class Command implements Serializable {
    private String argument;
    private String complexArgument;

    private boolean hasArgument = false;

    private transient Scanner scanner;

    public Command(boolean hasArgument) {
        this.hasArgument = hasArgument;
    }

    public Command() {
        super();
        this.hasArgument = false;
    }

    public abstract String getName();
    public abstract String getDescription();

    public boolean getHasArgument() {
        return hasArgument;
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
