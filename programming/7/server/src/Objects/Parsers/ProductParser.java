package Objects.Parsers;

import Objects.Collection.Coordinates;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.DBManager;
import Objects.Managers.IdManager;
import Objects.Validators.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class ProductParser extends Parser<Product> {
    private final InitializedIdValidator idValidator = new InitializedIdValidator();
    private final StringValidator stringValidator = new StringValidator();
    private final IntegerValidator integerValidator = new IntegerValidator();
    private final PriceValidator priceValidator = new PriceValidator();
    private final UnitValidator unitValidator = new UnitValidator();

    @Override
    public Product parse(ResultSet resultSet) throws SQLException {
        int row = resultSet.getRow();

        Long id = resultSet.getLong(DBManager.Headers.id.toString());
        if (resultSet.wasNull() || !idValidator.isValid(String.valueOf(id), false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.id,
                    row
            ));
        }

        String name = resultSet.getString(DBManager.Headers.name.toString());
        if (!stringValidator.isValid(name, false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.name,
                    row
            ));
        }

        CoordinateParser coordinateParser = new CoordinateParser();
        Coordinates coordinates = coordinateParser.parse(resultSet);

        Date creationDate = getDate(resultSet);

        String price = resultSet.getString(DBManager.Headers.price.toString());
        if (!priceValidator.isValid(String.valueOf(price), true)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.price,
                    row
            ));
        }

        String manufactureCost = resultSet.getString(DBManager.Headers.manufactureCost.toString());
        if (!integerValidator.isValid(manufactureCost, false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.manufactureCost,
                    row
            ));
        }

        String unitOfMeasure = resultSet.getString(DBManager.Headers.unitOfMeasure.toString());
        if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.unitOfMeasure,
                    row
            ));
        }

        PersonParser personParser = new PersonParser();
        Person person = personParser.parse(resultSet);

        Product product = new Product(
                id,
                name,
                coordinates,
                creationDate,
                price != null ? Double.parseDouble(price) : null,
                Integer.parseInt(manufactureCost),
                unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                person
        );

        IdManager.addId(id);

        return product;
    }

    private Date getDate(ResultSet resultSet) throws SQLException {
        int row = resultSet.getRow();

        Timestamp timestamp = resultSet.getTimestamp(DBManager.Headers.creationDate.toString());

        if (timestamp == null) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.creationDate,
                    row
            ));
        }

        Date creationDate = new Date(timestamp.getTime());

        Calendar creationCalendar = Calendar.getInstance();
        creationCalendar.setTime(creationDate);

        Calendar now = Calendar.getInstance();

        if (!creationCalendar.before(now)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d. Date hasn't come yet",
                    DBManager.Headers.creationDate,
                    row
            ));
        }

        return creationDate;
    }
}
