package Objects.Parsers;

import Objects.Collection.Location;
import Objects.Managers.CSVManager;
import Objects.Validators.DoubleValidator;
import Objects.Validators.IntegerValidator;
import Objects.Validators.LocationValidator;
import Objects.Validators.StringValidator;
import org.apache.commons.csv.CSVRecord;

public class LocationParser extends Parser<Location> {
    StringValidator stringValidator = new StringValidator();
    IntegerValidator integerValidator = new IntegerValidator();
    DoubleValidator doubleValidator = new DoubleValidator();
    LocationValidator locationValidator = new LocationValidator();

    public Location parse(CSVRecord record) {
        Location location = null;

        String locX = record.get(CSVManager.Headers.locX);
        String locY = record.get(CSVManager.Headers.locY);
        String locZ = record.get(CSVManager.Headers.locZ);
        String locName = record.get(CSVManager.Headers.locName);
        if (locX != null) {
            if (!doubleValidator.isValid(locX, false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.locX, record.getRecordNumber()));

            if (!integerValidator.isValid(String.valueOf(locY), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.locY, record.getRecordNumber()));

            if (!doubleValidator.isValid(String.valueOf(locZ), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.locZ, record.getRecordNumber()));

            if (!stringValidator.isValid(String.valueOf(locName), false))
                throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                        CSVManager.Headers.locName, record.getRecordNumber()));

            location = new Location(Double.parseDouble(locX),
                    Integer.parseInt(locY),
                    Double.parseDouble(locZ),
                    locName);

            if (!locationValidator.isValid(String.valueOf(location), false))
                throw new IllegalArgumentException(String.format("Invalid value for location in row %d",
                        record.getRecordNumber()));
        }
        return location;
    }
}
