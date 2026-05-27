package Objects.Parsers;

import Commons.Collection.Coordinates;
import Commons.Collection.Person;
import Commons.Collection.Product;
import Commons.Enums.UnitOfMeasure;
import Commons.UserData.User;
import Objects.DAOs.UserDAO;
import Objects.Managers.DBManager;
import Objects.Managers.IdManager;
import Objects.Validators.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class ProductParser extends Parser<Product> {
    private final static Logger logger = LoggerFactory.getLogger(ProductParser.class);

    private final InitializedIdValidator idValidator = new InitializedIdValidator();
    private final StringValidator stringValidator = new StringValidator();
    private final IntegerValidator integerValidator = new IntegerValidator();
    private final PriceValidator priceValidator = new PriceValidator();
    private final UnitValidator unitValidator = new UnitValidator();

    @Override
    public Product parse(ResultSet resultSet) throws SQLException {
        int row = resultSet.getRow();

        Long id = resultSet.getLong(DBManager.Headers.id.name());
        if (resultSet.wasNull() || !idValidator.isValid(String.valueOf(id), false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.id,
                    row
            ));
        }

        String name = resultSet.getString(DBManager.Headers.name.name());
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

        String price = resultSet.getString(DBManager.Headers.price.name());
        if (!priceValidator.isValid(String.valueOf(price), true)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.price,
                    row
            ));
        }

        String manufactureCost = resultSet.getString(DBManager.Headers.manufactureCost.name());
        if (!integerValidator.isValid(manufactureCost, false)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.manufactureCost,
                    row
            ));
        }

        String unitOfMeasure = resultSet.getString(DBManager.Headers.unitOfMeasure.name());
        if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true)) {
            throw new IllegalArgumentException(String.format(
                    "Invalid value for %s in row %d",
                    DBManager.Headers.unitOfMeasure,
                    row
            ));
        }

        PersonParser personParser = new PersonParser();
        Person person = personParser.parse(resultSet);

        long authorId = resultSet.getLong(DBManager.Headers.authorId.name());

        try {
            User author = UserDAO.getUserById(authorId);

            Product product = new Product(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    price != null ? Double.parseDouble(price) : null,
                    Integer.parseInt(manufactureCost),
                    unitOfMeasure != null ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                    person,
                    author
            );

            IdManager.addId(id);

            return product;
        } catch (SQLException | IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Date getDate(ResultSet resultSet) throws SQLException {
        int row = resultSet.getRow();

        Timestamp timestamp = resultSet.getTimestamp(DBManager.Headers.creationDate.name());

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
