package objects;

public class PointValidator {
    public boolean isValid(Point point){
        return isXValid(point.x()) && isYValid(point.y()) && isRValid(point.r());
    }

    private  boolean isXValid(int x){
        return -5<=x && x<=3;
    }

    private   boolean isYValid(double y){
        return -5<=y && y<=3;
    }

    private  boolean isRValid(double r){
        return 2<=r && r<=5;
    }
}
