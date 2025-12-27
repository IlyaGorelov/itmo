package Objects.Rocket;

import java.util.ArrayList;
import java.util.Random;

import Exceptions.BadWeatherException;
import Exceptions.NoMapDeviceException;
import Exceptions.NoRepairingDeviceException;
import Exceptions.NotAllDevicesExistException;
import Exceptions.NotAllDevicesRepairedException;
import Interfaces.Crashable;
import Objects.FlightProgram;
import Objects.Devices.*;
import Objects.People.Man;
import Objects.Planets.Moon;

public class ControlMachine {
    private String name;
    private Device[] devices;
    private Crashable[] crashableDevices;
    private RepairingDevice repairingDevice;
    private MapDevice mapDevice;
    private double rotation = 90;
    private double speed = 0;
    private ArrayList<Man> people = new ArrayList<Man>();
    private ButtonCabin buttonCabin;

    private ArrayList<Integer> commands;
    private int currentCommandIndex = 0;

    public int getCountOfDevices() {
        return devices.length;
    }

    public ControlMachine(String name, Device[] devices) {
        this.name = name;
        this.devices = devices;
    }

    public void startFlight(FlightProgram fp) {
        try {
            if (currentCommandIndex == 0)
                launch(fp);

            for (int i = currentCommandIndex; i < commands.size(); i++) {
                if (i != 0) {
                    if (commands.get(i) == commands.get(i - 1)) {
                        changeSpeed(speed / 2);
                    } else {
                        changeSpeed(-speed / 2);
                        if (commands.get(i - 1) < commands.get(i))
                            changeRotation(-90);
                        else
                            changeRotation(90);
                        changeSpeed(speed / 2);
                    }
                }
                currentCommandIndex = i;
            }

            landing();
        } catch (NotAllDevicesRepairedException e) {
            System.out.println(e.getMessage());
            repairingDevice.useExtraRepairs();

            System.out.printf("%s продолжает работу\n", name);
            startFlight(fp);
        } catch (BadWeatherException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            emergencyLanding();
        }
    }

    public void addMan(Man man) {
        people.add(man);
    }

    public void removeMan(Man man) {
        people.remove(man);
    }

    private void changeSpeed(double speed) {
        if (this.speed != this.speed + speed) {
            System.out.printf("Скорость изменяется с %.1f км/ч до %.1f км/ч\n", this.speed, this.speed + speed);
            this.speed += speed;

            System.out.println();
            tryToEnterButtonCabin();
            System.out.println();

            System.out.println();
            repairingDevice.scanDevices(crashableDevices);
            System.out.println();
        }
    }

    private void changeRotation(double rotation) {
        if (this.rotation != this.rotation + rotation) {
            System.out.printf("Поворот изменяется с %.1f° на %.1f°\n", this.rotation, this.rotation + rotation);
            this.rotation += rotation;

            System.out.println();
            tryToEnterButtonCabin();
            System.out.println();

            System.out.println();
            repairingDevice.scanDevices(crashableDevices);
            System.out.println();
        }
    }

    private void launch(FlightProgram fp)
            throws NoRepairingDeviceException, NotAllDevicesExistException, NoMapDeviceException {
        checkWeather();
        if (devices[0].getClass() != RepairingDevice.class)
            throw new NoRepairingDeviceException();

        if (devices[1].getClass() != MapDevice.class)
            throw new NoMapDeviceException();

        if (devices.length != 7)
            throw new NotAllDevicesExistException();

        switchOnDevices();

        repairingDevice = (RepairingDevice) devices[0];

        mapDevice = (MapDevice) devices[1];

        crashableDevices = getCrashableDevices();

        System.out.println("Летим по такому маршруту:");
        mapDevice.printMap(fp);
        commands = mapDevice.getFlightCommands(fp);

        speed = 0;
        changeSpeed(100);

        switch (fp.base) {
            case North:
                changeRotation(0);
                break;
            case South:
                changeRotation(-180);
                break;
            case West:
                changeRotation(90);
                break;
            case East:
                changeRotation(-90);
                break;
        }
    }

    public Crashable[] getCrashableDevices() {
        Crashable[] crashables = new Crashable[devices.length - 1];
        for (int i = 1; i < devices.length; i++) {
            crashables[i - 1] = (Crashable) devices[i];
        }
        return crashables;
    }

    private void landing() {
        changeSpeed(0);
        switchOffDevices();
        System.out.println("Ракета успешно достигла своей цели\n");

        System.out.println("Данные о Луне:");
        System.out.println(Moon.getInfo());
    }

    private void checkWeather() {
        if (Math.random() < 0.1)
            throw new BadWeatherException();
    }

    private void switchOnDevices() {
        for (Device device : devices) {
            device.prepareForLaunch();
        }

        System.out.println();
    }

    private void switchOffDevices() {
        for (Device device : devices) {
            device.prepareForLanding();
        }

        System.out.println();
    }

    private void emergencyLanding() {

        System.out.print("Экстренная посадка на ");
        if (Moon.attractShip(currentCommandIndex, commands)) {
            System.out.print("Луну\n");
            Moon.getInfo();
        } else
            System.out.print("Землю");
        System.out.println();
        switchOffDevices();

    }

    public void giveAccessToButtonCabin(ButtonCabin buttonCabin) {
        this.buttonCabin = buttonCabin;
    }

    private void tryToEnterButtonCabin() {
        if (Math.random() <= 0.3) {
            Random random = new Random();
            int index = random.nextInt(people.size());
            Man randomMan = people.get(index);

            buttonCabin.enterButtonCabin(randomMan);

        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        ControlMachine cm = (ControlMachine) obj;
        return getCountOfDevices() == cm.getCountOfDevices();
    }

    @Override
    public int hashCode() {
        int result = getCountOfDevices() + (int) speed;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s с количеством устройств: %s", name, getCountOfDevices());
    }
}
