package core.Objects.Parsers;

import Commons.Collection.Location;
import Commons.Collection.Person;
import Commons.Enums.Country;
import Commons.Enums.EyeColor;
import Commons.Enums.HairColor;
import core.Objects.Validators.*;

import java.util.Arrays;

public class PersonParser extends Parser<Person> {
    HeightValidator heightValidator = new HeightValidator();
    EyeValidator eyeValidator = new EyeValidator();
    HairValidator hairValidator = new HairValidator();
    CountryValidator countryValidator = new CountryValidator();

    public Person parse(String input) {
        Person person = null;

        String[] tokens;
        String[] rawTokens = input
                .replace("{", "")
                .replace("}", "")
                .replace(";", " ; ")
                .split(";");

        int countOfFields = Person.getCountOfEditableFields();

        if (rawTokens.length < countOfFields) {
            tokens = Arrays.copyOf(rawTokens, countOfFields);
        } else {
            tokens = rawTokens;
        }

        int tokenCounter = 0;

        String ownerName = tokens[tokenCounter++].trim();
        if (ownerName.isBlank()) {
            return null;
        } else {
            String height = tokens[tokenCounter++].trim();
            if (!heightValidator.isValid(height, false))
                throw new IllegalArgumentException("Invalid value for height");

            String eyeColor = tokens[tokenCounter++].trim();
            if (!eyeValidator.isValid(eyeColor, true))
                throw new IllegalArgumentException(
                        "Invalid value for eye color. Should be one of GREEN, RED, ORANGE");

            String hairColor = tokens[tokenCounter++].trim();
            if (!hairValidator.isValid(hairColor, false))
                throw new IllegalArgumentException(
                        "Invalid value for hair color. Should be one of GREEN, BLACK, WHITE");

            String nationality = tokens[tokenCounter++].trim();
            if (!countryValidator.isValid(nationality, false))
                throw new IllegalArgumentException(
                        "Invalid value for country. Should be one of USA, VATICAN, THAILAND");

            String locInline = String.format("{%s;%s;%s;%s}", tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++], tokens[tokenCounter++]);
            LocationParser locParser = new LocationParser();

            Location location = locParser.parse(locInline);

            person = new Person(
                    ownerName,
                    Float.parseFloat(height),
                    !eyeColor.isBlank() ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                    HairColor.valueOf(hairColor.toUpperCase()),
                    Country.valueOf(nationality.toUpperCase()),
                    location);
        }
        return person;
    }
}
