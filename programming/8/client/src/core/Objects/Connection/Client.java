package core.Objects.Connection;

import java.awt.*;
import java.net.ConnectException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.*;
import core.Objects.Managers.CommandManager;
import gui.Objects.Elements.Commons.ResultDialog;
import gui.Objects.Frames.MainFrame;

import javax.swing.*;

public class Client {
    ConnectionManager connectionManager;
    ServerListener serverListener;
    PackageSerializer packageSerializer;
    static ResponseHandler responseHandler;
    private static CommandDispatcher commandDispatcher;

    private CommandManager commandManager;
    private static Mode mode;
    private static MainFrame mainFrame;

    private Scanner scanner = new Scanner(System.in);

    public enum Mode {CLI, GUI}

    private static final BlockingQueue<CustomPackage> guiAnswers = new LinkedBlockingQueue<>();
    private final BlockingQueue<CustomPackage[]> serverAnswers = new LinkedBlockingQueue<>();

    public Client(Mode mode) {
        Client.mode = mode;
    }

    public static Mode getMode() {
        return mode;
    }

    public void start(String host, int port) {
        commandDispatcher = new CommandDispatcher(
                mode,
                scanner);
        connectionManager = new ConnectionManager(host, port);
        packageSerializer = new PackageSerializer();
        responseHandler = new ResponseHandler(mainFrame);

        serverListener = new ServerListener(
                connectionManager,
                packageSerializer,
                responseHandler,
                serverAnswers
        );

        if (connectionManager.connect(mode)) {
            serverListener.setListening(true);
            new Thread(serverListener).start();
            work();
        }
        ;
    }

    public static void openFrame(JFrame newFrame) {
        for (Frame frame : Frame.getFrames()) {
            frame.dispose();
        }
        if (newFrame instanceof MainFrame) {
            mainFrame = (MainFrame) newFrame;
            responseHandler.setMainFrame(mainFrame);
        }
        newFrame.setVisible(true);
    }

    private void work() {
        try {
            commandManager = new CommandManager(scanner);
            System.out.println("Client connected to server.");
            System.out.println();
            System.out.println("Log in to an existing account or create a new one to use all commands");
            System.out.println("Type \"help\" to see all available commands.");
            while (true) {
                CustomPackage customPackage = commandDispatcher.getCustomPackageForRequest();
                if (customPackage == null) {
                    continue;
                }

                if (commandDispatcher.isExitCommand(customPackage)) {
                    packageSerializer.write(
                            connectionManager.getOutput(),
                            customPackage);
                    System.out.println(getStringAnswer());
                    break;
                }

                if (commandDispatcher.isExecuteScriptCommand(customPackage)) {
                    StringBuilder info = new StringBuilder();

                    ExecuteScript.isProcessing = true;
                    CustomPackage pkg = CommandManager.getRelevantPackage(customPackage);
                    Object[] pkgs = (Object[]) pkg.getObject();
                    for (Object pkgObject : pkgs) {
                        CustomPackage singlePkg = (CustomPackage) pkgObject;
                        packageSerializer.write(
                                connectionManager.getOutput(),
                                singlePkg);
                        String singleAnswer = getStringAnswer();
                        info.append(singleAnswer);
                        info.append("\n");
                        System.out.println(singleAnswer);
                    }
                    ExecuteScript.isProcessing = false;
                    ResultDialog.showInfo(info.toString());

                    continue;
                }

                if (commandDispatcher.isClientOnlyCommand(customPackage)) {
                    String answer = (String) customPackage.getObject();
                    System.out.println(answer);
                    continue;
                }

                packageSerializer.write(
                        connectionManager.getOutput(),
                        customPackage);

                String answer = getStringAnswer();
                System.out.println();
                System.out.println(answer);
            }
            connectionManager.closeConnection();
            serverListener.setListening(false);
            System.out.println("Closing connections & channels on clientSide - DONE.");
        } catch (ConnectException e) {
            System.out.println("Server doesn't listen on this port or server is not started yet.");
        } catch (IndexOutOfBoundsException | NoSuchElementException e) {
            System.out.println("User input is not detected, but all data was saved");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            serverListener.setListening(false);
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
        }
    }

    public static void putAnswer(CustomPackage c) {
        try {
            guiAnswers.put(c);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }

    public static void putCommand(CustomPackage c) {
        try {
            commandDispatcher.putCommand(c);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }

    private String getStringAnswer() throws InterruptedException {
        while (true) {
            CustomPackage[] answer = serverAnswers.poll(200, TimeUnit.MILLISECONDS);
            if (answer != null) {
                return commandManager.getRelevantAnswer(answer);
            }
        }
    }

    public static CustomPackage getAnswer() {
        try {
            return guiAnswers.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            ResultDialog.showError(e.getMessage());
            return null;
        }
    }

}