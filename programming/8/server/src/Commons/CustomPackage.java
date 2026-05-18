package Commons;

import Commons.Collection.Person;
import Commons.Collection.Product;
import Commons.UserData.User;

import java.io.Serializable;

public class CustomPackage implements Serializable {
    String command;
    Object argument;
    Object object;
    User author;

    public CustomPackage(String command, Object argument, Object object) {
        this.command = command;
        this.argument = argument;
        this.object = object;
    }

    public CustomPackage(String command, Object argument, Object object, User author) {
        this.command = command;
        this.argument = argument;
        this.object = object;
        this.author = author;
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

    public User getAuthor() {
        return author;
    }
}
