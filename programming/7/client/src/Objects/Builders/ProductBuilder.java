package Objects.Builders;

import Objects.Collection.Coordinates;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.AuthManager;
import Objects.Validators.*;

import java.util.Date;
import java.util.Scanner;

public class ProductBuilder extends Builder<Product> {
    StringValidator stringValidator = new StringValidator();
    CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
    PriceValidator priceValidator = new PriceValidator();
    IntegerValidator integerValidator = new IntegerValidator();
    UnitValidator unitValidator = new UnitValidator();
    PersonBuilder personBuilder = new PersonBuilder();

    @Override
    public Product build(Scanner scanner) {
        String name = stringValidator.get(scanner, false, "Enter product name: ");
        Coordinates coordinates = coordinatesValidator.get(scanner, false, "Enter coordinates:");
        Double price = priceValidator.get(scanner, true, "Enter price(double) or type nothing: ");
        Integer manufactureCost = integerValidator.get(scanner, false, "Enter manufacture cost(integer): ");
        UnitOfMeasure unitOfMeasure = unitValidator.get(scanner, true, "Choose unit of measure or type nothing: ");

        Person person = personBuilder.build(scanner);
        return new Product(0, name, coordinates, new Date(), price, manufactureCost, unitOfMeasure,
                person, AuthManager.getInstance().getUser());

    }

}
