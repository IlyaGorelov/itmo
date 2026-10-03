package objects;

public class PointChecker {
    public boolean checkPoint(Point point) {
        return checkBottomLeftCorner(point.x(), point.y(), point.r()) ||
                checkBottomRightCorner(point.x(), point.y(), point.r()) ||
                checkUpperLeftCorner(point.x(), point.y(), point.r());
    }

    private boolean checkUpperLeftCorner(int x, double y, double r) {
        if (x <= 0 && x >= -r / 2 && y >= 0) {
            return (r * r) / 4 >= x * x + y * y;
        }
        return false;
    }

    private boolean checkBottomLeftCorner(int x, double y, double r) {
        if (x <= 0 && x >= -r && y <= 0) {
            return y >= -x - r;
        }
        return false;
    }

    private static boolean checkBottomRightCorner(int x, double y, double r) {
        if (x >= 0 && x <= r && y <= 0 && y >= -r / 2) {
            return true;
        }

        return false;
    }
}
