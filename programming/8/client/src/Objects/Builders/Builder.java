package Objects.Builders;

import java.util.Scanner;

public abstract class Builder<T> {
    public abstract T build(Scanner scanner);
}
