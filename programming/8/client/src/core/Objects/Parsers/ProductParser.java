package core.Objects.Parsers;

import Commons.Collection.*;
import core.Objects.Enums.UnitOfMeasure;
import core.Objects.Managers.AuthManager;
import core.Objects.Validators.*;

import java.util.Arrays;
import java.util.Date;

public class ProductParser extends Parser<Product> {
    StringValidator stringValidator = new StringValidator();
    IntegerValidator integerValidator = new IntegerValidator();
    PriceValidator priceValidator = new PriceValidator();
    UnitValidator unitValidator = new UnitValidator();

    public Product parse(String input) {
        Product product = null;

        String[] tokens;
        String[] rawTokens = input
                .replace("{", "")
                .replace("}", "")
                .replace(";", " ; ")
                .split(";");

        int countOfEditableFields = Product.getCountOfEditableFields(false);

        if (rawTokens.length < countOfEditableFields) {
            tokens = Arrays.copyOf(rawTokens, countOfEditableFields);
        } else {
            tokens = rawTokens;
        }

        int tokenCounter = 0;

        String name = tokens[tokenCounter++].trim();
        if (!stringValidator.isValid(name, false))
            throw new IllegalArgumentException("Product name can't be null");

        String coordinatesInline = String.format("{%s;%s}", tokens[tokenCounter++].trim(), tokens[tokenCounter++].trim());
        CoordinateParser coordinateParser = new CoordinateParser();

        Coordinates coordinates = coordinateParser.parse(coordinatesInline);

        String price = tokens[tokenCounter++].trim();
        if (!priceValidator.isValid(price, true))
            throw new IllegalArgumentException("Invalid value for price");

        String manufactureCost = tokens[tokenCounter++].trim();
        if (!integerValidator.isValid(manufactureCost, false))
            throw new IllegalArgumentException("Invalid value for manufacture cost");

        String unitOfMeasure = tokens[tokenCounter++].trim();
        if (!unitValidator.isValid(unitOfMeasure, true))
            throw new IllegalArgumentException(
                    "Invalid value for unit of measure. Should be one of KILOGRAMS, LITERS, METERS, MILLILITERS");

        String personInline = String.format("{%s;%s;%s;%s;%s;%s;%s;%s;%s}", tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++]);
        PersonParser personParser = new PersonParser();

        Person person = personParser.parse(personInline);

        product = new Product(
                0,
                name,
                coordinates,
                new Date(),
                !price.isBlank() ? Double.parseDouble(price) : null,
                Integer.parseInt(manufactureCost),
                !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                person,
                AuthManager.getInstance().getUser());

        return product;
    }
}
