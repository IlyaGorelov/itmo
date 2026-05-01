package Objects.Parsers;

import Objects.Collection.Location;
import Objects.Collection.Person;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Managers.CSVManager;
import Objects.Validators.CountryValidator;
import Objects.Validators.EyeValidator;
import Objects.Validators.HairValidator;
import Objects.Validators.HeightValidator;
import org.apache.commons.csv.CSVRecord;

public class PersonParser extends Parser<Person> {
    HeightValidator heightValidator = new HeightValidator();
    EyeValidator eyeValidator = new EyeValidator();
    HairValidator hairValidator = new HairValidator();
    CountryValidator countryValidator = new CountryValidator();

    public Person parse(CSVRecord record) {
        Person person = null;

        String ownerName = record.get(CSVManager.Headers.ownerName);

        if (ownerName != null) {
            String height = record.get(CSVManager.Headers.height);
            if (!heightValidator.isValid(String.valueOf(height), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.height, record.getRecordNumber()));

            String eyeColor = record.get(CSVManager.Headers.eyeColor);
            if (!eyeValidator.isValid(String.valueOf(eyeColor), true))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.eyeColor, record.getRecordNumber()));

            String hairColor = record.get(CSVManager.Headers.hairColor);
            if (!hairValidator.isValid(String.valueOf(hairColor), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.hairColor, record.getRecordNumber()));

            String nationality = record.get(CSVManager.Headers.nationality);
            if (!countryValidator.isValid(String.valueOf(nationality), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.nationality, record.getRecordNumber()));

            LocationParser locationParser = new LocationParser();
            Location location = locationParser.parse(record);

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
