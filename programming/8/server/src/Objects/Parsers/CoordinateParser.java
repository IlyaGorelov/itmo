package Objects.Parsers;

import Commons.Collection.Coordinates;
import Objects.Managers.DBManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CoordinateParser extends Parser<Coordinates> {
    private final IntegerValidator integerValidator = new IntegerValidator();
    private final DoubleValidator doubleValidator = new DoubleValidator();
    private final CoordinatesValidator coordinatesValidator = new CoordinatesValidator();

    @Override
    public Coordinates parse(ResultSet resultSet) throws SQLException {
        int row = resultSet.getRow();

        String x = resultSet.getString(DBManager.Headers.x.column());
        if (!integerValidator.isValid(x, false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.x,
                    row
            ));
        }

        String y = resultSet.getString(DBManager.Headers.y.column());
        if (!doubleValidator.isValid(y, false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.y,
                    row
            ));
        }

        Coordinates coordinates = new Coordinates(
                Integer.parseInt(x),
                Double.parseDouble(y)
        );

        if (!coordinatesValidator.isValid(String.valueOf(coordinates), false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for coordinates in row %d",
                    row
            ));
        }

        return coordinates;
    }
}