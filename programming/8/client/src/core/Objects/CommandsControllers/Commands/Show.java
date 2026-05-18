package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.AuthChecker;
import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/*Show all elements of collection */
public class Show extends Command implements AuthChecker {

    public Show(boolean hasArgument) {
        super(hasArgument);
    }

    public Show() {
        super();
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Show all elements of collection";
    }


    @Override
    public String getRelevantObject() {
        checkArgument();
        checkAuth();

        return null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        Object[] products = (Object[]) pack.getObject();
        String relevant = "All elements of collection: \n";

        if (products.length == 0)
            return "Collection is empty.";
        else {
            for (Object product : products) {
                relevant += product.toString() + "\n\n";
            }
            relevant += "END OF LIST\n";
        }

        return relevant;
    }

}
