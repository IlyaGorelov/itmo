package Objects.CommandsControllers;

import Commons.Collection.Product;
import Commons.CustomPackage;
import Commons.UserData.User;
import Objects.Connection.Server;
import Objects.Managers.CollectionManager;

import java.nio.channels.SocketChannel;

public abstract class Command {
    private CollectionManager collectionManager;
    private User user;
    private Server server;
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


    public Command setServer(Server server) {
        this.server = server;
        return this;
    }

    public Command setCLIMode(boolean isCLIMode) {
        this.isCLIMode = isCLIMode;
        return this;
    }

    public boolean getIsCLIMode() {
        return isCLIMode;
    }

    public Server getServer() {
        return server;
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
            getServer()
                    .addToAnswer(getCLient(), toClient);
        else
            getServer().addAnswerForCLI(toCLI);
    }

    public void answer(CustomPackage toClient, Product... toCLI) {
        if (!getIsCLIMode())
            getServer()
                    .addToAnswer(getCLient(), toClient);
        else {
            for (var p : toCLI) {
                getServer().addAnswerForCLI(p.toString());
            }
        }
    }

}
