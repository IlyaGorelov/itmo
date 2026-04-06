package Objects.Connection;

import java.io.Serializable;

import Objects.Collection.Person;
import Objects.Collection.Product;
import Objects.CommandsControllers.Command;

public class CustomPackage implements Serializable {
    String command;
    Object argument;
    Object object;

    public CustomPackage(Command command, Object argument, Object object) {
        this.command = command.getName();
        this.argument = argument;
        this.object = object;
    }

    public String getCommand() {
        return command;
    }

    public Object getObject() {
        return object;
    }

    public Object getArgument() {
        return argument;
    }

    @Override
    public String toString() {
        String result = command + " ";
        if (argument != null) {
            result += argument + " ";
        }

        if (object != null) {
            if (object instanceof Product)
                result += "{" + ((Product) object).getFuncString(false) + "}";
            else if (object instanceof Person)
                result += "{" + ((Person) object).getFuncString() + "}";
        }

        return result.trim();
    }
}
