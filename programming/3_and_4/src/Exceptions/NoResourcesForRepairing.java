package Exceptions;

public class NoResourcesForRepairing extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ресурсы для починки закончились :(";
    }
}
