package Objects.Planets;

import java.util.ArrayList;
import java.util.Random;

import Objects.LaunchTime;

public class Moon extends Planet {
    public static boolean attractShip(int currentCommand, ArrayList<Integer> commands) {
        double percent = (double) currentCommand / (double) commands.size() * 100;
        return percent >= 70;
    }

    public static String getInfo() {
        return String.format("Температура: %.2f\nВремя: %d:%d", new Random().nextDouble(-50, 10),
                new Random().nextInt(0, 23), new Random().nextInt(0, 60));
    }

    public static int[] getPosition(LaunchTime time) {
        return switch (time) {
            case h0 -> new int[] { 30, 15 };
            case h3 -> new int[] { 27, 23 };
            case h6 -> new int[] { 15, 30 };
            case h9 -> new int[] { 4, 23 };
            case h12 -> new int[] { 0, 15 };
            case h15 -> new int[] { 4, 7 };
            case h18 -> new int[] { 15, 0 };
            case h21 -> new int[] { 27, 7 };
        };
    }

}
