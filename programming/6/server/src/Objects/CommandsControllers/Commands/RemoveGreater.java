package Objects.CommandsControllers.Commands;

import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Managers.CollectionManager;
import Objects.Validators.IntegerValidator;
import Objects.Validators.PriceValidator;

import java.util.stream.IntStream;

/*remove elements greater than input */
public class RemoveGreater extends Command {
    public RemoveGreater(CollectionManager collectionManager, boolean hasArgument, boolean hasComplexArg) {
        super(collectionManager, hasArgument, hasComplexArg);
    }

    public RemoveGreater(CollectionManager collectionManager) {
        super(collectionManager);
    }

    @Override
    public void execute() {

    }

    @Override
    public void executeInline() {
        checkArgument();
        try {
            String complexArg = getComplexArgument();
            String[] origTokens = complexArg.replace("{", "").replace("}", "").replace(";", " ; ").split(";");
            String[] tokens;
            if (origTokens.length > 2) {
                tokens = IntStream.of(3, 4)
                        .mapToObj(i -> origTokens[i])
                        .toArray(String[]::new);
            } else
                tokens = origTokens;
            IntegerValidator integerValidator = new IntegerValidator();
            PriceValidator priceValidator = new PriceValidator();

            Product[] greaters = null;
            int tokenI = 0;

            String price = tokens[tokenI++].trim();
            if (!priceValidator.isValid(price, true))
                throw new IllegalArgumentException("Invalid value for price");

            String manufactureCost = tokens[tokenI++].trim();
            if (!integerValidator.isValid(manufactureCost, false))
                throw new IllegalArgumentException("Invalid value for manufacture cost");

            greaters = getCollectionManager().removeGreaters(
                    !price.isBlank() ? Double.parseDouble(price) : null,
                    Integer.parseInt(manufactureCost));

            CustomPackage pkg = new CustomPackage(this.getName(), null, greaters);
            answer(pkg, "Removed");

        } catch (IndexOutOfBoundsException e) {
            CustomPackage pkg = new CustomPackage(this.getName(), null, e);
            answer(pkg, "Invalid number of arguments!");
        } catch (Exception e) {
            if (e.getMessage() != null) {
                CustomPackage pkg = new CustomPackage(this.getName(), null, e);
                answer(pkg, e.getMessage());
            }
            // System.out.println("Skip\n");
        }

    }

    @Override
    public String getName() {
        return "remove_greater";
    }

    @Override
    public String getDescription() {
        return "remove elements greater than input ";
    }

    @Override
    public void checkArgument() {
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove_greater {Price(double>0 | null);Man Cost(int)}");
    }
}
