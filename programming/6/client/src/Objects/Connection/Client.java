package Objects.Connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Objects.CommandsControllers.Commands.Exit;
import Objects.Managers.CommandManager;

public class Client {
    public void connect() {
        try (Socket socket = new Socket("localhost", 3345);
                Scanner reader = new Scanner(System.in);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());) {

            System.out.println("Client connected to server.");
            System.out.println();
            System.out.println("Type \"help\" to see all available commands.");
            CommandManager commandManager = new CommandManager(reader, in, out);

            while (!socket.isOutputShutdown()) {
                String clientCommand = reader.nextLine();
                CustomPackage realCommand = null;
                try {
                    realCommand = commandManager.getRelevantPackage(clientCommand);
                } catch (Exception e) {
                    if (e.getMessage() != null)
                        System.out.println(e.getMessage());
                    continue;
                }

                out.writeObject(realCommand);
                out.flush();
                // System.out.println("Client sent message \"" + realCommand + "\" to server.");

                if (realCommand.getCommand().equals(new Exit().getName())) {

                    System.out.println("Client kill connections");

                    Object[] answer = (Object[]) in.readObject();
                    String realAnswer = commandManager.getRelevantAnswer(answer);
                    System.out.println(realAnswer);

                    break;
                }

                System.out.println();
                Object[] answer = (Object[]) in.readObject();
                String realAnswer = commandManager.getRelevantAnswer(answer);
                System.out.println(realAnswer);

            }
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (ConnectException e) {
            System.out.println("Server doesn't listen on this port or server is not started yet.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
