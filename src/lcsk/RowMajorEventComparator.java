package lcsk;

import java.util.Comparator;

/**
 * Comparator for Event objects. Uses row-major ordering.
 * If passed events have same pairs and events have different types, one with END type will be less than other with START.
 */
public class RowMajorEventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if (o2 == null) throw new NullPointerException();

        // o1 == o
        if (o1.equals(o2)) return 0;

        int i1 = o1.getPair().getFirstElement();
        int j1 = o1.getPair().getSecondElement();
        int i2 = o2.getPair().getFirstElement();
        int j2 = o2.getPair().getSecondElement();

        // o1 < o2
        if ((j1 < j2) ||
                (j1 == j2 && i1 < i2) ||
                (i1 == i2 && j1 == j2 && o1.getType() == Event.EventType.END && o2.getType() == Event.EventType.START)
        )
            return -1;

        // else o1 > o2
        return +1;
    }
}
