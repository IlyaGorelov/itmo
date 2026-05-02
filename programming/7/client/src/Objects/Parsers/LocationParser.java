package Objects.Parsers;

import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Validators.*;

import java.util.Arrays;

public class LocationParser extends Parser<Location> {
    StringValidator stringValidator = new StringValidator();
    IntegerValidator integerValidator = new IntegerValidator();
    DoubleValidator doubleValidator = new DoubleValidator();
    LocationValidator locationValidator = new LocationValidator();

    public Location parse(String input) {
        Location location = null;

        String[] tokens;
        String[] rawTokens = input
                .replace("{", "")
                .replace("}", "")
                .replace(";", " ; ")
                .split(";");

        int countOfFields = Location.getCountOfEditableFields();

        if (rawTokens.length < countOfFields) {
            tokens = Arrays.copyOf(rawTokens, countOfFields);
        } else {
            tokens = rawTokens;
        }

        int tokenCounter = 0;

        String locX = tokens[tokenCounter++].trim();
        if (!locX.isBlank()) {
            if (!doubleValidator.isValid(locX, false))
                throw new IllegalArgumentException("Invalid value for locX");

            String locY = tokens[tokenCounter++].trim();
            String locZ = tokens[tokenCounter++].trim();
            String locName = tokens[tokenCounter++].trim();

            if (!integerValidator.isValid(locY, false))
                throw new IllegalArgumentException("Invalid value for locY");

            if (!doubleValidator.isValid(locZ, false))
                throw new IllegalArgumentException("Invalid value for locZ");

            if (!stringValidator.isValid(locName, false))
                throw new IllegalArgumentException("Invalid value for locName");

            location = new Location(Double.parseDouble(locX),
                    Integer.parseInt(locY),
                    Double.parseDouble(locZ),
                    locName);

            if (!locationValidator.isValid(String.valueOf(location), false))
                throw new IllegalArgumentException("Invalid value for location");
        }

        return location;
    }
}
