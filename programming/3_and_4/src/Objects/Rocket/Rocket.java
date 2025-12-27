package Objects.Rocket;

import java.util.ArrayList;

import Objects.FlightProgram;
import Objects.People.Captain;
import Objects.People.Passenger;

public class Rocket {
    private Captain captain;
    private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
    private String name;
    private ControlCabin controlCabin = new ControlCabin("Кабина управления");
    private ButtonCabin buttonCabin = new ButtonCabin("Кнопочная кабина");

    public Rocket(String name) {
        this.name = name;
    }

    public void setCaptain(Captain captain) {
        this.captain = captain;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptainName() {
        return captain.toString();
    }

    public void addCaptain(Captain captain) {
        this.captain = captain;
        controlCabin.getControlMachine().addMan(captain);
    }

    public void printPassengers() {
        int i = 1;
        for (Passenger p : passengers) {
            System.out.print(i + ". ");
            System.out.println(p.toString());
            i++;
        }
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        controlCabin.getControlMachine().addMan(passenger);
    }

    public void removePassenger(Passenger passenger) {
        passengers.remove(passenger);
        controlCabin.getControlMachine().removeMan(passenger);
    }

    public int getCountOfPassengers() {
        return passengers.size();
    }

    public void launch(FlightProgram program) {
        System.out.printf("Ракета \"%s\" начала подготовку к запуску\n", name);
        System.out.printf("На борту присутствуют: Капитан %s", captain.toString());

        for (Passenger passenger : passengers) {
            System.out.printf(", пассажир %s", passenger.toString());
        }
        System.out.println();

        controlCabin.getControlMachine().giveAccessToButtonCabin(buttonCabin);

        buttonCabin.startControlMachine(controlCabin.getControlMachine(), program);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Rocket r = (Rocket) obj;
        return name == r.name;
    }

    @Override
    public int hashCode() {
        int result = name.length();
        return result;
    }

    @Override
    public String toString() {
        return "Ракета с именем: " + name;
    }
}
