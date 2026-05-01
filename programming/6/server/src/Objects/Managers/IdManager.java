package Objects.Managers;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Controls id
 */
public class IdManager {
    /**
     * list of all ids
     */
    static ArrayList<Long> ids = new ArrayList<>();

    /**
     * get unique id
     *
     * @return unique id
     */
    public static long getId() {
        Long start = ids.stream().max(Comparator.naturalOrder()).orElse(1L);
        while (ids.contains(start)) {
            start++;
        }
        ids.add(start);
        return start;
    }

    public static void removeId(long id) {
        ids.remove(id);
    }

    public static boolean isIdIn(Long id) {
        return ids.contains(id);
    }

    public static void addId(Long id) {
        ids.add(id);
    }

    public static void clear() {
        ids.clear();
    }

}
