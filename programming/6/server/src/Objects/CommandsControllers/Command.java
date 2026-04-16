package Objects.CommandsControllers;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;

/** Abstract class representing a command */
public abstract class Command {
    /** Collection manager so we can control a collection */
    private CollectionManager collectionManager;
    /** Receiver to send answers */
    private Receiver receiver;
    /** Socket channel to send answers */
    private SocketChannel client;
    /** An argument of a command presented in one line format */
    private String argument;
    /** Boolean indicates this command requires argument or not */
    private boolean hasArgument = false;
    private boolean isCLIMode = false;

    /**
     * Constructor with 2 parameters
     * 
     * @param collectionManager to set and to control collection
     * @param hasArgument       to set if this command requires an argument
     */
    public Command(CollectionManager collectionManager, boolean hasArgument) {
        this.collectionManager = collectionManager;
        this.hasArgument = hasArgument;
    }

    /**
     * Constructor with 1 parameter
     * hasArgument is set at false by default
     * 
     * @param collectionManager to set and to control collection
     */
    public Command(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.hasArgument = false;
    }

    public Command() {
        super();
    }

    public boolean getHasArgument() {
        return hasArgument;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute();

    public void executeFromScript(String complexArg) {
        execute();
    };

    public Command setReceiver(Receiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public Command setCLIMode(boolean isCLIMode) {
        this.isCLIMode = isCLIMode;
        return this;
    }

    public boolean getIsCLIMode() {
        return isCLIMode;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public Command setClient(SocketChannel client) {
        this.client = client;
        return this;
    }

    public SocketChannel getCLient() {
        return client;
    }

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

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

}
