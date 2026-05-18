package Objects.Comparators;

import Commons.Collection.Person;

import java.util.Comparator;

/**
 * This comparator supports null persons
 */
public class PersonComparator implements Comparator<Person> {
    @Override
    public int compare(Person o1, Person o2) {
        if (o1 == null)
            return o2 != null ? 1 : 0;

        if (o2 == null)
            return 1;

        return o1.compareTo(o2);
    }

}
