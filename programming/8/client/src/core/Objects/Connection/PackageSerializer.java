package core.Objects.Connection;

import Commons.CustomPackage;

import java.io.*;

public class PackageSerializer {
    public void write(DataOutputStream dos, CustomPackage customPackage) throws IOException {
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(customPackage);
            oos.flush();

            byte[] objectBytes = baos.toByteArray();

            dos.writeInt(objectBytes.length);
            dos.write(objectBytes);
            dos.flush();
        }
    }

    public CustomPackage[] read(DataInputStream dis) throws IOException, ClassNotFoundException {
        int answerLength = dis.readInt();

        byte[] answerBytes = new byte[answerLength];

        dis.readFully(answerBytes);

        ByteArrayInputStream bais = new ByteArrayInputStream(answerBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        Object[] rawPackages = (Object[]) ois.readObject();

        CustomPackage[] packages = new CustomPackage[rawPackages.length];

        for (int i = 0; i < rawPackages.length; i++) {
            packages[i] = (CustomPackage) rawPackages[i];
        }

        return packages;

    }
}
