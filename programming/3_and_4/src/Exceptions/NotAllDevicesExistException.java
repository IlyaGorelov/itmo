package Exceptions;

public class NotAllDevicesExistException extends Exception {
    @Override
    public String getMessage() {
        return "Не все устройства подключены к Кабине управления. Их должно быть 7";
    }
}
