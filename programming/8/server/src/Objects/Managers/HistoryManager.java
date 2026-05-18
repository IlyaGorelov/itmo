package Objects.Managers;

import Commons.UserData.User;
import Objects.CommandsControllers.History;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HistoryManager {
    private static final Map<User, History> historyMap = new ConcurrentHashMap<>();

    public static boolean isAtStart(User user) {
        History history = historyMap.get(user);
        return history.isAtStart();
    }

    public static boolean isAtEnd(User user) {
        History history = historyMap.get(user);
        return history.isAtEnd();
    }

    /***
     * add a new history in map
     * @param user a key in map
     */
    public static void registerNewHistory(User user) {
        historyMap.putIfAbsent(user, new History());
    }

    public static void moveBack(User user) {
        History history = historyMap.get(user);
        history.moveBack();
    }

    public static void moveForward(User user) {
        History history = historyMap.get(user);
        history.moveForward();
    }

    public static void add(User user, History.HistoryObject historyObject) {
        History history = historyMap.get(user);
        history.add(historyObject);
    }

    public static History.HistoryObject getHistoryObject(User user) {
        History history = historyMap.get(user);
        return history.getHistoryObject();
    }
}
