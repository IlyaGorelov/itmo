package Objects.Managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIManager {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final StringBuilder currentLine = new StringBuilder();

    public String pollLine() throws IOException {
        while (reader.ready()) {
            int ch = reader.read();
            if (ch == -1) {
                if (currentLine.isEmpty()) {
                    return null;
                }
                String line = currentLine.toString();
                currentLine.setLength(0);
                return line;
            }

            if (ch == '\n') {
                String line = currentLine.toString();
                currentLine.setLength(0);
                return line;
            }

            currentLine.append((char) ch);
        }

        return null;
    }

    public void writeLine(String text) {
        System.out.println(text);
        System.out.flush();
    }
}
