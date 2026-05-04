package Objects.Managers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class DBManager {
    private final String dbUrl;
    private final String pathToProps;

    public DBManager(String url, String propsPath) {
        this.dbUrl = url;
        this.pathToProps = propsPath;
    }

    public enum Headers {
        id("id"), name("name"), x("x"), y("y"),
        creationDate("creation_date"), price("price"),
        manufactureCost("manufacture_cost"),
        unitOfMeasure("unit_of_measure"),
        ownerName("owner_name"), height("height"),
        eyeColor("eye_color"), hairColor("hair_color"),
        nationality("nationality"), locX("loc_x"),
        locY("loc_y"), locZ("loc_Z"), locName("loc_name");

        private final String columnName;

        Headers(String columnName) {
            this.columnName = columnName;
        }

        public String column() {
            return columnName;
        }

        public String selectWithAlias() {
            return columnName + " AS " + name();
        }
    }

    public static String buildSelectColumns() {
        return Arrays.stream(Headers.values())
                .map(Headers::selectWithAlias)
                .collect(Collectors.joining(","));
    }

    public static String buildInsertColumns() {
        return Arrays.stream(Headers.values())
                .filter(h -> h != Headers.id)
                .map(Headers::column)
                .collect(Collectors.joining(","));
    }

    public static String buildInsertColumnsWithId() {
        return Arrays.stream(Headers.values())
                .map(Headers::column)
                .collect(Collectors.joining(","));
    }

    public static String buildUpdateColumns() {
        return Arrays.stream(Headers.values())
                .filter(h -> h != Headers.id)
                .map(Headers::column)
                .collect(Collectors.joining("=?,"));
    }

    public Connection getConnection() throws IOException, SQLException {
        Properties props = new Properties();
        props.load(new FileInputStream(pathToProps));

        return DriverManager.getConnection(dbUrl, props);
    }
}
