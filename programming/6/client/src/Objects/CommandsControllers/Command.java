package Objects.CommandsControllers;

import java.io.Serializable;
import java.util.Scanner;

import Objects.Connection.CustomPackage;

/** Abstract class representing a command */
public abstract class Command implements Serializable {
    private static final long serialVersionUID = 2L;
    /** An argument of a command presented in one line format */
    private String argument;
    /** Boolean indicates this command requires argument or not */
    private boolean hasArgument = false;
    private boolean hasComplexArgument = false;

    private transient Scanner scanner;

    /**
     * Constructor with 2 parameters
     * 
     * @param collectionManager to set and to control collection
     * @param hasArgument       to set if this command requires an argument
     */
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

    /**
     * Checks if there is an argument when you don't need it or there is to much
     * arguments or there is no required argument
     */
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
