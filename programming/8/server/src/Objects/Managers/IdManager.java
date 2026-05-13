package Objects.Managers;

import java.util.ArrayList;

/**
 * Controls id
 */
public class IdManager {
    /**
     * list of all ids
     */
    static ArrayList<Long> ids = new ArrayList<>();

    public static void removeId(long id) {
        ids.remove(id);
    }

    public static boolean isIdIn(Long id) {
        return ids.contains(id);
    }

    public static void addId(Long id) {
        ids.add(id);
    }


}
