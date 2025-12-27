package Tests;

public enum DayOfWeek {
    SUNDAY("Sunday"),
    MONDAY("Monday");

    private String title;

    DayOfWeek(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return title;
    }
}
