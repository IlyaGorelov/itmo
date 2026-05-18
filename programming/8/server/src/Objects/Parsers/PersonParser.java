package Objects.Parsers;

import Commons.Collection.Location;
import Commons.Collection.Person;
import Objects.Enums.Country;
import Objects.Enums.EyeColor;
import Objects.Enums.HairColor;
import Objects.Managers.DBManager;
import Objects.Validators.CountryValidator;
import Objects.Validators.EyeValidator;
import Objects.Validators.HairValidator;
import Objects.Validators.HeightValidator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonParser extends Parser<Person> {
    private final HeightValidator heightValidator = new HeightValidator();
    private final EyeValidator eyeValidator = new EyeValidator();
    private final HairValidator hairValidator = new HairValidator();
    private final CountryValidator countryValidator = new CountryValidator();

    @Override
    public Person parse(ResultSet resultSet) throws SQLException {
        Person person = null;

        int row = resultSet.getRow();

        String ownerName = resultSet.getString(DBManager.Headers.ownerName.name());

        if (ownerName != null) {
            String height = resultSet.getString(DBManager.Headers.height.name());
            if (!heightValidator.isValid(height, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.height,
                        row
                ));
            }

            String eyeColor = resultSet.getString(DBManager.Headers.eyeColor.name());
            if (!eyeValidator.isValid(eyeColor, true)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.eyeColor,
                        row
                ));
            }

            String hairColor = resultSet.getString(DBManager.Headers.hairColor.name());
            if (!hairValidator.isValid(hairColor, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.hairColor,
                        row
                ));
            }

            String nationality = resultSet.getString(DBManager.Headers.nationality.name());
            if (!countryValidator.isValid(nationality, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.nationality,
                        row
                ));
            }

            LocationParser locationParser = new LocationParser();
            Location location = locationParser.parse(resultSet);

            person = new Person(
                    ownerName,
                    Float.parseFloat(height),
                    eyeColor != null ? EyeColor.valueOf(eyeColor.toUpperCase()) : null,
                    HairColor.valueOf(hairColor.toUpperCase()),
                    Country.valueOf(nationality.toUpperCase()),
                    location
            );
        }

        return person;
    }
}