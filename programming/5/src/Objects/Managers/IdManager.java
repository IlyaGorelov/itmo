package Objects.Managers;

import java.util.ArrayList;
import java.util.Collections;

/** Controls id */
public class IdManager {
    /** list of all ids */
    static ArrayList<Long> ids = new ArrayList<>();

    /**
     * get unique id
     * 
     * @return unique id
     */
    public static long getId() {
        if (ids.size() == 0) {
            ids.add((long) 1);
            return 1;
        } else {
            Collections.sort(ids);
            long maxId = ids.get(ids.size() - 1);
            long newId = maxId + 1;
            ids.add(newId);
            return newId;
        }
    }

    public static void removeId(long id) {
        ids.remove(id);
    }

    public static boolean isIdIn(Long id) {
        return ids.contains(id);
    }

}
