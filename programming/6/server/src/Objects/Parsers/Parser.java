package Objects.Parsers;

import org.apache.commons.csv.CSVRecord;

public abstract class Parser<T> {
    public abstract T parse(CSVRecord record);
}
