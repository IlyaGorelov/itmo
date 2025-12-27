package Exceptions;

public class NoMapDeviceException extends Exception {
    @Override
    public String getMessage() {
        return "Навигация не найдена в Кабине управления. Или же оно не было подключено вторым";
    }
}
