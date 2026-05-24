package core.Objects.Parsers;

import Commons.Collection.Coordinates;
import core.Objects.Validators.*;

import java.util.Arrays;

public class CoordinateParser extends Parser<Coordinates> {
    IntegerValidator integerValidator = new IntegerValidator();
    DoubleValidator doubleValidator = new DoubleValidator();
    CoordinatesValidator coordinatesValidator = new CoordinatesValidator();

    public Coordinates parse(String input) {
        Coordinates coordinates = null;

        String[] tokens;
        String[] rawTokens = input
                .replace("{", "")
                .replace("}", "")
                .replace(";", " ; ")
                .split(";");

        int countOfFields = Coordinates.getCountOfEditableFields();

        if (rawTokens.length < countOfFields) {
            tokens = Arrays.copyOf(rawTokens, countOfFields);
        } else {
            tokens = rawTokens;
        }

        int tokenCounter = 0;

        String x = tokens[tokenCounter++].trim();
        if (!integerValidator.isValid(x, false))
            throw new IllegalArgumentException("Invalid value for x");

        String y = tokens[tokenCounter++].trim();
        if (!doubleValidator.isValid(y, false))
            throw new IllegalArgumentException("Invalid value for y");

        coordinates = new Coordinates(Integer.parseInt(x),
                Double.parseDouble(y));
        if (!coordinatesValidator.isValid(coordinates, false))
            throw new IllegalArgumentException("Invalid value for coordinates");


        return coordinates;
    }
}
