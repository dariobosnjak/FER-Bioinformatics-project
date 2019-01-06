package lcsk;

import java.util.Comparator;

/**
 * Comparator for Event objects. Uses row-major ordering.
 * If passed events have same pairs and events have different types, one with END type will be less than other with START.
 */
class RowMajorEventComparator implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if (o2 == null) throw new NullPointerException();

        // o1 == o
        if (o1.equals(o2)) return 0;

        int i1 = o1.getPair().getFirstElement(); // row
        int j1 = o1.getPair().getSecondElement(); // column
        int i2 = o2.getPair().getFirstElement(); // row
        int j2 = o2.getPair().getSecondElement(); // column

        // o1 < o2
        if ((i1 < i2) || // row1 is "above" row2 in a matrix
                (i1 == i2 && j1 < j2) || // rows are the same but column1 is on the left side with respect to the column 2
                (j1 == j2 && i1 == i2 && o1.getType() == Event.EventType.END && o2.getType() == Event.EventType.START) // rows and columns are the same, but second event is of type START
        )
            return -1;

        // else o1 > o2
        return +1;
    }
}
