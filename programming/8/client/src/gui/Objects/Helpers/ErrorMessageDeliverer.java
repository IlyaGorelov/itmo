package gui.Objects.Helpers;

import java.util.LinkedList;
import java.util.Queue;

public enum ErrorMessageDeliverer {
    validation, response;

    private static Queue<String> validationErrors = new LinkedList<>();
    private static Queue<String> responseErrors = new LinkedList<>();

    public static void add(Exception e, ErrorMessageDeliverer errorType) {
        if (e.getMessage() != null) {
            switch (errorType) {
                case validation -> validationErrors.add(e.getMessage());
                case response -> responseErrors.add(e.getMessage());
            }
        }
    }

    public static String poll(ErrorMessageDeliverer errorType) {
        switch (errorType) {
            case validation -> {
                return validationErrors.poll();
            }
            case response -> {
                return responseErrors.poll();
            }
        }

        return null;
    }

    public static boolean hasNoErrors(){
        return validationErrors.size()+responseErrors.size()==0;
    }
}
