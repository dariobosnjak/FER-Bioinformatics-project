package lcsk;
import utils.Pair;

import java.util.Objects;

/**
 * Event object representation. Holds a pair which represents the start or the end of the k-match.
 */
public class Event {
    enum EventType {
        START, END
    }

    private Pair<Integer, Integer> pair;
    private EventType type;

    /**
     * Creates new event object.
     * @param pair location of the start/end of the k-match
     * @param type defines if this event is the start or the end of k-match
     */
    public Event(Pair<Integer, Integer> pair, EventType type) {
        this.pair = pair;
        this.type = type;
    }

    /**
     * Pair getter.
     * @return location of the start/end of this event
     */
    public Pair<Integer, Integer> getPair() {
        return pair;
    }

    /**
     * Pair setter.
     * @param pair sets this pair to the passed value.
     */
    public void setPair(Pair<Integer, Integer> pair) {
        this.pair = pair;
    }

    /**
     * Event type getter.
     * @return type of this event
     */
    public EventType getType() {
        return type;
    }

    /**
     * Event type setter.
     * @param type sets this event type to the passed value.
     */
    public void setType(EventType type) {
        this.type = type;
    }

    /**
     * This event is equal to the passed one if both have the same type (both are START or END) and both have the same
     * start or end location.
     * @param o the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(pair, event.pair) &&
                type == event.type;
    }

    /**
     * Returns a hash code value for the object.
     * @return hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(pair, type);
    }
}