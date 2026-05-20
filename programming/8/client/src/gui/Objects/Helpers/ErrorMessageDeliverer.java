package gui.Objects.Helpers;

import java.util.LinkedList;
import java.util.Queue;

public class ErrorMessageDeliverer {
    private static Queue<String> validationErrors = new LinkedList<>();

    public static void add(Exception e) {
        if (e.getMessage() != null) {
            validationErrors.add(e.getMessage());
        }
    }

    public static String poll() {
        return validationErrors.poll();
    }

    public static boolean hasNoErrors() {
        return validationErrors.size() == 0;
    }
}
