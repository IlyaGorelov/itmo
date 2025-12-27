package Objects.Devices;

import Exceptions.NoResourcesForRepairing;
import Exceptions.NotAllDevicesRepairedException;
import Interfaces.Crashable;

public class RepairingDevice extends Device {
    private int countOfRepairs = 5;
    private int extraRepairs = 10;

    public void useExtraRepairs() {
        System.out.printf("Использованы доп ресурсы для починки\n");
        countOfRepairs += extraRepairs;
        extraRepairs = 0;
    }

    public RepairingDevice(String name, double condition, int countOfRepairings) {
        super(name, condition);
        countOfRepairs = countOfRepairings;
    }

    public void scanDevices(Crashable[] devices) {
        for (Crashable device : devices) {
            device.scan();

            if (device.isNeedRepairing() && countOfRepairs > 0) {
                device.repair();
                countOfRepairs--;
            } else if (device.isNeedRepairing() && countOfRepairs <= 0) {
                if (extraRepairs == 0)
                    throw new NoResourcesForRepairing();
                throw new NotAllDevicesRepairedException();
            }
        }
    }
}
