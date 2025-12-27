import java.util.Random;

public class LabOne {
    public static void main(String[] args) {
        long[] s = { 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18 };
        float[] x = new float[18];

        Random rand = new Random();

        for (int i = 0; i < 18; i++) {
            x[i] = rand.nextFloat(-15, 6);
        }

        double[][] n = new double[13][18];

        for (int si = 0; si < 13; si++) {
            for (int xi = 0; xi < 18; xi++) {
                n[si][xi] = getElement(s[si], x[xi]);
            }
        }

        print(n);
    }

    public static double getElement(long elementFromS, float elementFromX) {
        switch ((int) elementFromS) {
            case 12:
                return Math.asin((elementFromX - 4.5) / 21);
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                double temp = Math.tan(elementFromX);
                double temp1 = Math.pow(Math.PI * temp, 2);
                return Math.pow(temp1, 1.0 / 3.0);
            default:
                double temp2 = 2 / (3 * elementFromX);
                double temp3 = Math.pow(temp2, 2);
                double temp4 = Math.pow(Math.E, temp3);
                double temp5 = Math.pow(Math.E, temp4);
                return Math.tan(temp5);
        }
    }

    public static void print(double[][] l) {
        String result = "";
        for (int si = 0; si < 13; si++) {
            for (int xi = 0; xi < 18; xi++) {
                result += String.format("%.2f", l[si][xi]) + "\t";

            }
            result += "\n";
        }

        result = result.replace(',', '.');
        result = result.replace("NaN", ".");
        System.out.println(result);
    }
}
