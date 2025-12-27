package Exceptions;

public class NotAllDevicesRepairedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Не всё оборудование может быть исправлено";
    }
}
