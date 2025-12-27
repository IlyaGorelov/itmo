package Objects.Devices;

public abstract class Device {
    private String name = "Неизвестно";
    private double condition = 100;

    public Device(String name, double condition) {
        this.name = name;

        if (condition > 100)
            this.condition = 100;
        else if (condition < 0)
            this.condition = 0;
        else
            this.condition = condition;
    }

    protected String getName() {
        return name;
    }

    protected double getCondition() {
        return condition;
    }

    protected void setCondition(double condition) {
        if (condition > 100)
            this.condition = 100;
        else if (condition < 0)
            this.condition = 0;
        else
            this.condition = condition;
    }

    public void prepareForLaunch() {
        System.out.printf("%s подготовлен к запуску.\tСостояние: %.1f %%\n", name, condition);
    };

    public void prepareForLanding() {
        System.out.printf("%s подготовлен к приземлению.\tСостояние: %.1f %%\n", name, condition);
    };

}
