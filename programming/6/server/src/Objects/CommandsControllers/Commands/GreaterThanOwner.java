package Objects.CommandsControllers.Commands;

import java.util.ArrayList;

import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.HairColor;
import Objects.Managers.CollectionManager;
import Objects.Validators.*;

/** get all elements where owner is biger than input one */
public class GreaterThanOwner extends Command {
    public GreaterThanOwner(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArgument) {
        super(collectionManager, hasArgument, hasComplexArgument);
    }

    public GreaterThanOwner(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {
        setComplexArgument("{}");
        executeInline();
    }

    @Override
    public void executeInline() {
        checkArgument();
        String complexArg = getComplexArgument()==null ? "" : getComplexArgument();
        String[] tokens = complexArg.replace("{", "").replace("}", "").split(";");
        ArrayList<Long> ids = new ArrayList<>();
        if (tokens.length == 0)
            throw new IllegalArgumentException("No args found. Use {} to compare with null owner");

        HeightValidator heightValidator = new HeightValidator();

        try {
            String ownerName = tokens[0];
            if (ownerName.isBlank())
                ids = getCollectionManager()
                        .getIdsGreaterThanOwner(null);
            else {
                String height = tokens[1];
                if (!heightValidator.isValid(String.valueOf(height), false))
                    throw new IllegalArgumentException("Invalid value for height");

                ids = getCollectionManager()
                        .getIdsGreaterThanOwner(
                                new Person(
                                        ownerName,
                                        Float.parseFloat(height),
                                        null,
                                        HairColor.BLACK, Country.USA,
                                        null));

            }
            ArrayList<Product> products = new ArrayList<>();
            if (!ids.isEmpty()) {
                // System.out.println("All products with owner greater than input:\n");
                for (Long id : ids) {
                    products.add(getCollectionManager().getById(id));
                }
                // System.out.println("END OF LIST");
            }

            CustomPackage pkg = new CustomPackage(this.getName(), null, products.toArray());
            answer(pkg,  products.toArray(new Product[0]));
        } catch (Exception e) {
            if (e.getMessage() != null)
                answer(null,e.getMessage());
            // System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "filter_greater_than_owner";
    }

    @Override
    public String getDescription() {
        return "show all elements where, firstly, owner's name is longer than input, secondly, owner's height is greater than input";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
       // boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument())
            throw new IllegalArgumentException(String.format("Invalid format, use:\n\t%s {name(String);height(float>0)}",getName()));
    }

}
