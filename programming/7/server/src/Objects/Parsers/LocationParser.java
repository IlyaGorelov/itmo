package Objects.Parsers;

import Objects.Collection.Location;
import Objects.Managers.DBManager;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.LocationValidator;
import Objects.Validators.StringValidator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationParser extends Parser<Location> {
    private final StringValidator stringValidator = new StringValidator();
    private final IntegerValidator integerValidator = new IntegerValidator();
    private final DoubleValidator doubleValidator = new DoubleValidator();
    private final LocationValidator locationValidator = new LocationValidator();

    @Override
    public Location parse(ResultSet resultSet) throws SQLException {
        Location location = null;

        int row = resultSet.getRow();

        String locX = resultSet.getString(DBManager.Headers.locX.name());
        String locY = resultSet.getString(DBManager.Headers.locY.name());
        String locZ = resultSet.getString(DBManager.Headers.locZ.name());
        String locName = resultSet.getString(DBManager.Headers.locName.name());

        if (locX != null) {
            if (!doubleValidator.isValid(locX, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.locX,
                        row
                ));
            }

            if (!integerValidator.isValid(locY, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.locY,
                        row
                ));
            }

            if (!doubleValidator.isValid(locZ, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.locZ,
                        row
                ));
            }

            if (!stringValidator.isValid(locName, false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for %s in row %d",
                        DBManager.Headers.locName,
                        row
                ));
            }

            location = new Location(
                    Double.parseDouble(locX),
                    Integer.parseInt(locY),
                    Double.parseDouble(locZ),
                    locName
            );

            if (!locationValidator.isValid(String.valueOf(location), false)) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value for location in row %d",
                        row
                ));
            }
        }

        return location;
    }
}