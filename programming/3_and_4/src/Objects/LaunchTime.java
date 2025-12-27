package Objects;

public enum LaunchTime {
    h0("00:00"), h3("03:00"),
    h6("06:00"), h9("09:00"),
    h12("12:00"), h15("15:00"),
    h18("18:00"), h21("21:00");

    public final String time;

    LaunchTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return time;
    }
}
