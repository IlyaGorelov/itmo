package Objects.Rocket;

import Objects.FlightProgram;
import Objects.People.Captain;
import Objects.People.Man;

public class ButtonCabin extends Cabin {

    public ButtonCabin(String name) {
        super(name);
    }

    public void startControlMachine(ControlMachine cm, FlightProgram fp) {
        System.out.printf("%s начала свою работу. Летим с базы: %s \n\n", name, fp.base);

        cm.startFlight(fp);
    }

    public void enterButtonCabin(Man man) {
        System.out.printf("%s пытается войти в Кнопочную кабину ", man.toString());

        if (man.getClass() == Captain.class) {
            System.out.print("и у него получается\n");
            pushButton();
        } else {
            System.out.print("и у него НЕ получается\n");
        }
    }

    private void pushButton() {
        if (Math.random() < 0.8) {
            System.out.println("Кнопка нажата...   и...   происходит...");
            System.out.println("НИЧЕГО!!!");
        }
    }
}
