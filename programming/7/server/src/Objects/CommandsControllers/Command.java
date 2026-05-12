package Objects.CommandsControllers;

import Objects.Collection.Product;
import Objects.Connection.CustomPackage;
import Objects.Connection.Receiver;
import Objects.Managers.CollectionManager;
import Objects.UserData.User;

import java.nio.channels.SocketChannel;

public abstract class Command {
    private CollectionManager collectionManager;
    private User user;
    private Receiver receiver;
    private SocketChannel client;

    private String argument;
    private Object complexArgument;

    private boolean hasArgument = false;
    private boolean hasComplexArgument = false;
    private boolean isCLIMode = false;

    /**
     * Constructor with 2 parameters
     *
     * @param collectionManager to set and to control collection
     * @param hasArgument       to set if this command requires an argument
     */
    public Command(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        this.collectionManager = collectionManager;
        this.hasArgument = hasArgument;
        this.hasComplexArgument = hasComplexArgument;
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

    public boolean getHasComplexArgument() {
        return hasComplexArgument;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute();


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

    public Command setUser(User user) {
        this.user = user;
        return this;
    }

    public User getUser() {
        return user;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    public Object getComplexArgument() {
        return complexArgument;
    }

    public void setComplexArgument(Object argument) {
        this.complexArgument = argument;
    }

    /**
     * Checks if there is an argument when you don't need it or there are too many
     * arguments or there is no required argument
     */
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != hasArgument || actuallyHasComplexArgument != hasComplexArgument)
            throw new IllegalArgumentException(String.format("Invalid format, use:\n\t%s", getName()));
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public void answer(CustomPackage toClient, String toCLI) {
        if (!getIsCLIMode())
            getReceiver()
                    .addToAnswer(getCLient(), toClient);
        else
            getReceiver().addAnswerForCLI(toCLI);
    }

    public void answer(CustomPackage toClient, Product... toCLI) {
        if (!getIsCLIMode())
            getReceiver()
                    .addToAnswer(getCLient(), toClient);
        else {
            for (var p : toCLI) {
                getReceiver().addAnswerForCLI(p.toString());
            }
        }
    }

}
