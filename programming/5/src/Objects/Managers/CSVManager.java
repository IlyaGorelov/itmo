package Objects.Managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CSVManager {

    /** Headers for .csv file */
    String[] headers = { "id", "name", "x", "y", "creationDate", "price", "manufactureCost",
            "unitOfMeasure",
            "owner" };

    /**
     * Reads .csv
     * 
     * @param path path to the file
     * @return ArrayList<CSVRecord> list of csv records
     */
    public ArrayList<CSVRecord> read(String path) {
        ArrayList<CSVRecord> result = new ArrayList<>();
        try {
            String file = fileToString(path);
            CSVParser csvParser = CSVParser.parse(file,
                    CSVFormat.DEFAULT.builder().setHeader().setNullString("").get());
            for (CSVRecord i : csvParser) {
                result.add(i);
            }

        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return result;
    }

    /**
     * Reads file with Scanner
     * 
     * @param path path to the file
     * @return String represenation of file
     */
    private String fileToString(String path) throws IllegalArgumentException {
        String content = "";

        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                content += reader.nextLine() + "\n";
            }
            reader.close();
            return content;
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalArgumentException("There is now such file");
        }
    }

    /**
     * writes array of strings as .csv file
     * 
     * @param path    path to the new file
     * @param records array of strings ready to transform into CSVRecord
     */
    public void write(String path, ArrayList<String> records) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

            CSVPrinter csvPrinter = new CSVPrinter(bufferedWriter,
                    CSVFormat.DEFAULT.builder().setHeader(headers).get());

            for (String i : records) {
                csvPrinter.printRecord(i.split(","));
            }

            csvPrinter.close();
            bufferedWriter.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
