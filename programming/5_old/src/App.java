import Objects.Managers.CSVManager;

public class App {

    public static final String PATH = "lab5";

    public static void main(String[] args) throws Exception {

        CSVManager csvManager = new CSVManager();
        csvManager.readFile("C:\\itmo\\programming\\5\\lab5.csv");
        System.out.println("Hello, World!");
    }
}
