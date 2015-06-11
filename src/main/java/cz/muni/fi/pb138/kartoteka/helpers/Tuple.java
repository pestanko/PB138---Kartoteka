package cz.muni.fi.pb138.kartoteka.helpers;

/**
 * Simple Tuple implementation
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-07
 */
public class Tuple<T,U> {

    /**
     * First in tuple
     */
    private final T first;

    /**
     * Second in tuple
     */
    private final U second;

    /**
     * Basic constructor
     * @param first first part of tuple
     * @param second second part of tuple
     */
    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Getter for {@link Tuple#first}
     * @return first
     */
    public T getFirst() {
        return first;
    }

    /**
     * Getter for {@link Tuple#second}
     * @return second
     */
    public U getSecond() {
        return second;
    }

    /**
     * Checks whether two Tuples are equal (auto-generated)
     * @param o second object
     * @return true if the two objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if (!first.equals(tuple.first)) return false;
        return second.equals(tuple.second);

    }

    /**
     * Calculates hashcode
     * @return hash
     */
    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }

    /**
     * Writes Tuple info into String
     * @return formatted String
     */
    @Override
    public String toString() {
        return "First = " + first + ", Second = " + second;
    }
}
