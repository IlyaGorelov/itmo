package Objects.Parsers;

import Objects.Collection.Coordinates;
import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.Enums.UnitOfMeasure;
import Objects.Managers.CSVManager;
import Objects.Managers.IdManager;
import Objects.Validators.*;
import org.apache.commons.csv.CSVRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProductParser extends Parser<Product> {
    InitializedIdValidator idValidator = new InitializedIdValidator();
    StringValidator stringValidator = new StringValidator();
    IntegerValidator integerValidator = new IntegerValidator();
    PriceValidator priceValidator = new PriceValidator();
    UnitValidator unitValidator = new UnitValidator();

    public Product parse(CSVRecord record) {
        Product product = null;

        String id = record.get(CSVManager.Headers.id);
        if (!idValidator.isValid(String.valueOf(id), false))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.id, record.getRecordNumber()));

        String name = record.get(CSVManager.Headers.name);
        if (!stringValidator.isValid(String.valueOf(name), false))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.id, record.getRecordNumber()));

        CoordinateParser coordinateParser = new CoordinateParser();
        Coordinates coordinates = coordinateParser.parse(record);

        Date creationDate = getDate(record);

        String price = record.get(CSVManager.Headers.price);
        if (!priceValidator.isValid(String.valueOf(price), true))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.price, record.getRecordNumber()));

        String manufactureCost = record.get(CSVManager.Headers.manufactureCost);
        if (!integerValidator.isValid(String.valueOf(manufactureCost), false))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.manufactureCost, record.getRecordNumber()));

        String unitOfMeasure = record.get(CSVManager.Headers.unitOfMeasure);
        if (!unitValidator.isValid(String.valueOf(unitOfMeasure), true))
            throw new IllegalArgumentException(String.format("Invalid value for %s in row %d",
                    CSVManager.Headers.unitOfMeasure, record.getRecordNumber()));


        PersonParser personParser = new PersonParser();
        Person person = personParser.parse(record);

        product = new Product(
                Long.parseLong(id),
                name,
                coordinates,
                creationDate,
                !price.isBlank() ? Double.parseDouble(price) : null,
                Integer.parseInt(manufactureCost),
                !unitOfMeasure.isBlank() ? UnitOfMeasure.valueOf(unitOfMeasure.toUpperCase()) : null,
                person);

        IdManager.addId(Long.parseLong(id));
        return product;
    }


    private Date getDate(CSVRecord record) {
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            calendar.setTime(sdf.parse(record.get(CSVManager.Headers.creationDate)));
            Date creationDate = calendar.getTime();

            if (!calendar.before(Calendar.getInstance())) {
                throw new IllegalArgumentException(
                        String.format("Invalid value for %s in row %d. Date hasn't come yet",
                                CSVManager.Headers.creationDate, record.getRecordNumber()));
            }

            return creationDate;
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid value for %s in row %d. Date hasn't come yet",
                            CSVManager.Headers.creationDate, record.getRecordNumber()));
        }
    }

}
