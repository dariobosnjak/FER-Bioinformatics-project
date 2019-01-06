package utils;

import java.util.Objects;

/**
 * Represents a pair or 2-tuple object.
 * @param <X> object type for the first element of the pair
 * @param <Y> object type for the second element of the pair
 */
public class Pair<X, Y> {
    private X firstElement;
    private Y secondElement;

    /**
     * Constructs new Pair object.
     * @param firstElement first element of the pair
     * @param secondElement second element of the pair
     */
    public Pair(X firstElement, Y secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    /**
     * Getter for the first element of the pair.
     * @return first element of the pair
     */
    public X getFirstElement() {
        return firstElement;
    }

    /**
     * Getter for the second element of the pair.
     * @return second element of the pair
     */
    public Y getSecondElement() {
        return secondElement;
    }

    @Override
    public String toString() {
        return "Pair(" +
                firstElement +
                ", " + secondElement +
                ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(firstElement, pair.firstElement) &&
                Objects.equals(secondElement, pair.secondElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }
}