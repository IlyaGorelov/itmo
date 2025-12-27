package Exceptions;

public class BadWeatherException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Сегодня плохая погода. Полеты запрещены";
    }
}
