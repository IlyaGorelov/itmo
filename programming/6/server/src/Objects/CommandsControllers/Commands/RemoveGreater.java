package Objects.CommandsControllers.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import Objects.Collection.Coordinates;
import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CollectionManager;
import Objects.Validators.*;

/*remove elements greater than input */
public class RemoveGreater extends Command {
    public RemoveGreater(CollectionManager collectionManager, boolean hasArgument,boolean hasComplexArg) {
        super(collectionManager, hasArgument,hasComplexArg);
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
        if(origTokens.length>2) {
            tokens = IntStream.of(3, 4)
                    .mapToObj(i -> origTokens[i])
                    .toArray(String[]::new);
        }
        tokens = origTokens;
        IntegerValidator integerValidator = new IntegerValidator();
        PriceValidator priceValidator = new PriceValidator();

        Product[] greaters = null;
        int tokenI = 0;

            String price = tokens[tokenI++].trim();
            if (!priceValidator.isValid(String.valueOf(price), true))
                throw new IllegalArgumentException("Invalid value for price");

            String manufactureCost = tokens[tokenI++].trim();
            if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
                throw new IllegalArgumentException("Invalid value for manufacture cost");

            greaters = getCollectionManager().removeGreaters(
                    !price.isBlank() ? Double.parseDouble(price) : null,
                    Integer.parseInt(manufactureCost));

            CustomPackage pkg = new CustomPackage(this.getName(), null, greaters);
            answer(pkg,"Removed");

        } catch (IndexOutOfBoundsException e){
            CustomPackage pkg = new CustomPackage(this.getName(), null, e);
            answer(pkg,"Invalid number of arguments!");
        }
        catch (Exception e) {
            if (e.getMessage() != null) {
                CustomPackage pkg = new CustomPackage(this.getName(), null, e);
                answer(pkg,e.getMessage());
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
    public  void checkArgument(){
        boolean actuallyHasArgument = getArgument() != null;
        boolean actuallyHasComplexArgument = getComplexArgument() != null;
        if (actuallyHasArgument != getHasArgument() || actuallyHasComplexArgument != getHasComplexArgument())
            throw new IllegalArgumentException("Invalid format, use:\n\tremove_greater {Price(double>0 | null);Man Cost(int)}");
    }
}
