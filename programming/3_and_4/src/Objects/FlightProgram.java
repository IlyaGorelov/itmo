package Objects;

public class FlightProgram {
    public BaseType base;
    public LaunchTime time;

    public FlightProgram(BaseType baseType, LaunchTime launchTime) {
        this.base = baseType;
        this.time = launchTime;
    }
}