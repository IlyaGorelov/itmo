package Objects.Parsers;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Parser<T> {
    public abstract T parse(ResultSet resultSet) throws SQLException;
}
