package Objects.Rocket;

import Objects.Devices.*;

public class ControlCabin extends Cabin {
    private Device[] devices = {
            new RepairingDevice("Устройство починки", 100, 5),
            new MapDevice("Навигация", 100),
            new ZeroGravityDevice("Устройство нулевой гравитации", 100),
            new AccelerationDevice("Ускоритель", 100),
            new RotationDevice("Вращатель", 100),
            new EarthConnectionDevice("Связь с Землей", 100),
            new CommunicationDevice("Внутренняя коммуникация", 100),
    };

    private ControlMachine controlMachine = new ControlMachine("Машина управления", devices);

    public ControlCabin(String name) {
        super(name);
    }

    public ControlMachine getControlMachine() {
        return controlMachine;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        ControlCabin cb = (ControlCabin) obj;
        return getControlMachine().equals(cb.getControlMachine());
    }

    @Override
    public int hashCode() {
        int result = 5 * devices.length;
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

}
