package core.Objects.Connection;


import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.ExecuteScript;
import core.Objects.CommandsControllers.Commands.Exit;
import core.Objects.CommandsControllers.Commands.Help;
import core.Objects.CommandsControllers.Commands.Logout;
import core.Objects.Managers.CommandManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandDispatcher {
    private Client.Mode mode;
    private Scanner scanner;

    private static final BlockingQueue<CustomPackage> guiCommands = new LinkedBlockingQueue<>();

    public CommandDispatcher(
            Client.Mode mode,
            Scanner scanner) {
        this.mode = mode;
        this.scanner = scanner;
    }

    public CustomPackage getCustomPackageForRequest() throws InterruptedException, IOException, ClassNotFoundException {
        CustomPackage clientCommand;
        switch (mode) {
            case CLI -> clientCommand = new CustomPackage(scanner.nextLine(), null, null);
            case GUI -> {
                return waitCommandFromGUI();
            }
            default -> {
                return null;
            }
        }
        try {
            return CommandManager.getRelevantPackage(clientCommand);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            }
            return null;
        }
    }

    private CustomPackage waitCommandFromGUI() throws InterruptedException, IOException, ClassNotFoundException {
        CustomPackage customPackage = guiCommands.take();
        if (isClientOnlyCommand(customPackage)) {
            return new CustomPackage(customPackage.getCommand(), null, CommandManager.getRelevantPackage(customPackage).getObject());
        }
        return customPackage;
    }

    public void putCommand(CustomPackage c) throws InterruptedException{
            guiCommands.put(c);
    }

    public boolean isExitCommand(CustomPackage pkg) throws IOException, InterruptedException {
        return pkg.getCommand().equals(new Exit().getName());
    }

    public boolean isExecuteScriptCommand(CustomPackage rawPkg) throws IOException, ClassNotFoundException, InterruptedException {
        String commandName = rawPkg.getCommand();
        return  commandName.startsWith(new ExecuteScript().getName());
    }

    public boolean isClientOnlyCommand(CustomPackage pkg) throws IOException, ClassNotFoundException {
        String commandName = pkg.getCommand();
        boolean isHelp = commandName.equals(new Help().getName());
        boolean isLogout = commandName.equals(new Logout().getName());
       return isHelp || isLogout;
    }
}
