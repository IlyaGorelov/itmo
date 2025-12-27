package Exceptions;

public class NoRepairingDeviceException extends Exception {
    @Override
    public String getMessage() {
        return "Устройство починки не найдено в Кабине управления. Или же оно не было подключено первым";
    }
}
