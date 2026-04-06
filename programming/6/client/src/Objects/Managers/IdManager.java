package Objects.Managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Objects.Collection.Product;
import Objects.CommandsControllers.Commands.GetById;
import Objects.Connection.CustomPackage;

/** Controls id */
public class IdManager {
    static ObjectOutputStream out;
    static ObjectInputStream in;

    public static boolean isIdIn(Long id) {
        try {
            out.writeObject(new CustomPackage(new GetById(), id, null));
            out.flush();

            Object[] answer = (Object[]) in.readObject();
            if (answer.length == 1)
                return ((CustomPackage) answer[0]).getObject() != null;
            else
                throw new IOException("Wrong answer from server while checking id");
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found while checking id");
            return false;
        } catch (IOException e) {
            System.out.println("IO was interrupted while checking id");
            return false;
        }
    }

    public static void setIO(ObjectInputStream in, ObjectOutputStream out) {
        IdManager.out = out;
        IdManager.in = in;
    }

}
