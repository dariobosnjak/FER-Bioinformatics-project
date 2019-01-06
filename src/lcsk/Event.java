package lcsk;
import utils.Pair;

import java.util.Objects;

/**
 * Event object. Holds pair which represents start or end of the k-match.
 */
public class Event {
    enum EventType {
        START, END
    }

    private Pair<Integer, Integer> pair;
    private EventType type;

    public Event(Pair<Integer, Integer> pair, EventType type) {
        this.pair = pair;
        this.type = type;
    }

    public Pair<Integer, Integer> getPair() {
        return pair;
    }

    public void setPair(Pair<Integer, Integer> pair) {
        this.pair = pair;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(pair, event.pair) &&
                type == event.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, type);
    }
}