package Objects.Managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CSVManager {

    public ArrayList<String> readFile(String path) {
        ArrayList<String> result = new ArrayList<>();
        try {
            String file = fileToString(path);
            CSVParser csvParser = CSVParser.parse(file, CSVFormat.DEFAULT);

            for (var i : csvParser) {
                System.out.println(i);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private String fileToString(String path) {
        String content = "";

        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                content += reader.nextLine() + "\n";
            }
            reader.close();
            return content;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}
