package Objects.Parsers;

import Objects.Collection.Coordinates;
import Objects.Managers.CSVManager;
import Objects.Validators.CoordinatesValidator;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;
import org.apache.commons.csv.CSVRecord;

public class CoordinateParser extends Parser<Coordinates> {
    IntegerValidator integerValidator = new IntegerValidator();
    DoubleValidator doubleValidator = new DoubleValidator();
    CoordinatesValidator coordinatesValidator = new CoordinatesValidator();


    public Coordinates parse(CSVRecord record) {
        Coordinates coordinates = null;

        String x = record.get(CSVManager.Headers.x);
        if (!integerValidator.isValid(String.valueOf(x), false))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.x, record.getRecordNumber()));

        String y = record.get(CSVManager.Headers.y);
        if (!doubleValidator.isValid(String.valueOf(y), false))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.y, record.getRecordNumber()));

        coordinates = new Coordinates(Integer.parseInt(x),
                Double.parseDouble(y));
        if (!coordinatesValidator.isValid(String.valueOf(coordinates), false))
            throw new IllegalArgumentException(
                    String.format("Invalid value for coordinates in row %d",
                            record.getRecordNumber()));

        return coordinates;
    }
}
