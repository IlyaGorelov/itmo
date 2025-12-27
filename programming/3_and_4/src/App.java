import java.io.Console;

import Objects.BaseType;
import Objects.FlightProgram;
import Objects.LaunchTime;
import Objects.People.Captain;
import Objects.People.Passenger;
import Objects.Rocket.Rocket;

public class App {
    public static void main(String[] args) throws Exception {
        Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }

        Rocket rocket = new Rocket("Орешник");
        rocket.addCaptain(new Captain("Николай"));

        FlightProgram fp = new FlightProgram(BaseType.North, LaunchTime.h0);

        while (true) {
            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1. Управление ракетой");
            System.out.println("2. Пассажиры");
            System.out.println("3. Маршрут");
            System.out.println("4. Симуляция полёта");
            System.out.println("5. Выход");

            int choice = getInt(c, 1, 5);
            System.out.println();

            switch (choice) {
                case 1:
                    showRocketMenu(c, rocket);
                    break;
                case 2:
                    showPassengerMenu(c, rocket);
                    break;
                case 3:
                    showRouteMenu(c, fp);
                    break;
                case 4:
                    rocket.launch(fp);
                    break;
                default:
                    return;
            }
        }

    }

    public static void showRocketMenu(Console c, Rocket rocket) {
        while (true) {

            System.out.println("\n=== УПРАВЛЕНИЕ РАКЕТОЙ ===");
            System.out.println("1. Информация");
            System.out.println("2. Переименовать");
            System.out.println("3. Поменять капитана");
            System.out.println("4. Назад");

            int choice = getInt(c, 1, 4);
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println(rocket.toString());
                    System.out.println("Капитана зовут " + rocket.getCaptainName());
                    break;
                case 2:
                    changeRocketName(c, rocket);
                    break;
                case 3:
                    changeCaptain(c, rocket);
                    break;
                default:
                    return;
            }
        }
    }

    public static void showPassengerMenu(Console c, Rocket rocket) {
        while (true) {

            System.out.println("\n=== ПАССАЖИРЫ ===");
            System.out.println("1. Вывести список");
            System.out.println("2. Добавить");
            System.out.println("3. Удалить");
            System.out.println("4. Назад");

            int choice = getInt(c, 1, 4);
            System.out.println();

            switch (choice) {
                case 1:
                    rocket.printPassengers();
                    break;
                case 2:
                    addPassengers(c, rocket);
                    break;
                case 3:
                    removePassenger(c, rocket);
                    break;
                default:
                    return;
            }
        }
    }

    public static void showRouteMenu(Console c, FlightProgram fp) {
        while (true) {
            System.out.println("\n=== МАРШРУТ ===");
            System.out.println("1. Вывести детали");
            System.out.println("2. Изменить место запуска");
            System.out.println("3. Изменить время запуска");
            System.out.println("4. Назад");

            int choice = getInt(c, 1, 4);

            switch (choice) {
                case 1:
                    System.out.println("Летим с базы: " + fp.base);
                    System.out.println("Время запуска: " + fp.time);
                    break;
                case 2:
                    fp.base = getBaseType(c);
                    break;
                case 3:
                    fp.time = getLaunchTime(c);
                    break;
                default:
                    return;
            }
        }
    }

    public static void changeRocketName(Console c, Rocket rocket) {
        String input = c.readLine("Введите новое имя: ");
        rocket.setName(input);
    }

    public static void changeCaptain(Console c, Rocket rocket) {
        String input = c.readLine("Введите новое имя капитана: ");
        rocket.setCaptain(new Captain(input));
    }

    public static void addPassengers(Console c, Rocket rocket) {
        String name = c.readLine("Введите имя пасажира: ");
        rocket.addPassenger(new Passenger(name.strip()));
    }

    public static void removePassenger(Console c, Rocket rocket) {
        String name = c.readLine("Введите имя пасажира: ");
        rocket.removePassenger(new Passenger(name));
    }

    public static BaseType getBaseType(Console c) {
        String input = c.readLine("Выберите с какой земной базы будем лететь:" + //
                "\n\t1.Север" + //
                "\n\t2.Юг" + //
                "\n\t3.Запад" + //
                "\n\t4.Восток" + //
                "\nВведите цифру: ");
        do {
            try {
                int inputInt = Integer.parseInt(input);
                switch (inputInt) {
                    case 1:
                        return BaseType.North;
                    case 2:
                        return BaseType.South;
                    case 3:
                        return BaseType.West;
                    case 4:
                        return BaseType.East;
                    default:
                        throw new IllegalArgumentException("Цифра вне диапазона");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input = c.readLine("Введите корректную цифру: ");
            }
        } while (true);
    }

    public static LaunchTime getLaunchTime(Console c) {
        String input = c.readLine("Выберите время запуска:" + //
                "\n\t1)00:00\t2)03:00" + //
                "\n\t3)06:00\t4)09:00" + //
                "\n\t5)12:00\t6)15:00" + //
                "\n\t7)18:00\t8)21:00" + //
                "\nВведите цифру: ");
        do {
            try {
                int inputInt = Integer.parseInt(input);
                switch (inputInt) {
                    case 1:
                        return LaunchTime.h0;
                    case 2:
                        return LaunchTime.h3;
                    case 3:
                        return LaunchTime.h6;
                    case 4:
                        return LaunchTime.h9;
                    case 5:
                        return LaunchTime.h12;
                    case 6:
                        return LaunchTime.h15;
                    case 7:
                        return LaunchTime.h18;
                    case 8:
                        return LaunchTime.h21;
                    default:
                        throw new IllegalArgumentException("Цифра вне диапазона");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input = c.readLine("Введите корректную цифру: ");
            }
        } while (true);
    }

    public static int getInt(Console c, int min, int max) {
        String input = c.readLine("Ваш выбор: ");
        do {
            try {
                int inputInt = Integer.parseInt(input);

                if (inputInt > max || inputInt < min)
                    throw new IllegalArgumentException("Цифра вне диапазона");

                return inputInt;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                input = c.readLine("Введите корректную цифру: ");
            }
        } while (true);
    }
}
