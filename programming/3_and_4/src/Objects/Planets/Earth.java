package Objects.Planets;

import java.util.Random;

import Objects.BaseType;

public class Earth extends Planet {
    public static boolean attractShip(double distanceToMoon) {
        return distanceToMoon < 400;
    }

    public static String getInfo() {
        return String.format("Температура: %.2f\nВремя: %d:%d", new Random().nextDouble(-50, 10),
                new Random().nextInt(0, 23), new Random().nextInt(0, 60));
    }

    public static int[] getPosition(BaseType base) {
        return switch (base) {
            case North -> new int[] { 16, 15 }; // y,x
            case South -> new int[] { 14, 15 };
            case West -> new int[] { 15, 14 };
            case East -> new int[] { 15, 16 };
        };
    }
}
