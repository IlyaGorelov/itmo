package Objects.Parsers;

import Objects.Collection.Location;
import Objects.Collection.Person;
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

        String ownerName = resultSet.getString(DBManager.Headers.ownerName.column());

        if (ownerName != null) {
            String height = resultSet.getString(DBManager.Headers.height.column());
            if (!heightValidator.isValid(height, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.height,
                        row
                ));
            }

            String eyeColor = resultSet.getString(DBManager.Headers.eyeColor.column());
            if (!eyeValidator.isValid(eyeColor, true)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.eyeColor,
                        row
                ));
            }

            String hairColor = resultSet.getString(DBManager.Headers.hairColor.column());
            if (!hairValidator.isValid(hairColor, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.hairColor,
                        row
                ));
            }

            String nationality = resultSet.getString(DBManager.Headers.nationality.column());
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