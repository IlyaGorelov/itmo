package Objects.Devices;

import Interfaces.Crashable;

public class ZeroGravityDevice extends Device implements Crashable {

    public ZeroGravityDevice(String name, double condition) {
        super(name, condition);
    }

    @Override
    public void crash() {
        double breakChance = Math.pow(1.0 - (getCondition() / 100.0), 2);
        if (Math.random() < breakChance) {
            setCondition(0);
            System.out.printf("%s сломалось\n", getName());
        }
    }

    @Override
    public void repair() {
        setCondition(100);
        System.out.printf("%s восстановлено\n", getName());
    }

    @Override
    public void scan() {
        if (Math.random() < 0.4) {
            {
                setCondition(getCondition() - 40);
                System.out.printf("В ходе сканирования %s было выявлено ухудшение состояния\n", getName());
                crash();
            }
        }
    }

    @Override
    public boolean isNeedRepairing() {
        return getCondition() <= 0;
    }

}
