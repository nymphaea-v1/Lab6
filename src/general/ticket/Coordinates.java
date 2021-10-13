package general.ticket;

import general.exceptions.IncorrectFieldException;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

public class Coordinates implements Serializable {
    private final Long x;
    private final Integer y;

    public Coordinates(Long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    protected Long getX() {
        return x;
    }

    protected Integer getY() {
        return y;
    }

    public static Long readX(Iterator<String> iterator) throws IncorrectFieldException {
        String xString = iterator.next().trim();
        long x;

        try {
            x = Long.parseLong(xString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(xString);
        }

        if (x > 565) throw new IncorrectFieldException(x);

        return x;
    }

    public static Integer readY(Iterator<String> iterator) throws IncorrectFieldException {
        String yString = iterator.next().trim();
        int y;

        try {
            y = Integer.parseInt(yString);
        } catch (NumberFormatException e) {
            throw new IncorrectFieldException(yString);
        }

        if (y > 907) throw new IncorrectFieldException(y);

        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Coordinates coordinates = (Coordinates) object;

        return x.equals(coordinates.x) && y.equals(coordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}

