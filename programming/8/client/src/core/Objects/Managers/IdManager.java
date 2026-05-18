package core.Objects.Managers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import Commons.Collection.Product;
import core.Objects.CommandsControllers.Commands.GetById;
import Commons.CustomPackage;

/**
 * Controls id
 */
public class IdManager {
    static OutputStream out;
    static InputStream in;

    public static boolean isIdIn(Long id) {
        Product answer = getProductById(id);
        if(!AuthManager.getInstance().getUser().equals(answer.getAuthor()))
            throw new IllegalArgumentException("It's not your product");

        return answer != null;
    }

    public static Product getProductById(long id) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(new CustomPackage(new GetById().getName(), String.valueOf(id), null));
            oos.flush();

            byte[] objectBytes = baos.toByteArray();
            dos.writeInt(objectBytes.length);
            dos.write(objectBytes);
            dos.flush();

            DataInputStream dis = new DataInputStream(in);

            int answerLength = dis.readInt();
            byte[] answerBytes = new byte[answerLength];
            dis.readFully(answerBytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);

            Object[] answer = (Object[]) ois.readObject();
            if (answer.length == 1)
                return (Product) ((CustomPackage) answer[0]).getObject();
            else
                throw new IOException("Wrong answer from server while checking id");
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found while checking id");
            return null;
        } catch (IOException e) {
            System.out.println("IO was interrupted while checking id");
            return null;
        }
    }

    public static void setIO(InputStream in, OutputStream out) {
        IdManager.out = out;
        IdManager.in = in;
    }

}
