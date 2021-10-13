package general;

import general.exceptions.IncorrectFieldException;

import java.util.Iterator;

/**
 * Simple class representation of a named reader
 */
public class Reader {
    public final String name;
    public final Readable reader;

    public Reader(String name, Readable reader) {
        this.name = name;
        this.reader = reader;
    }

    public interface Readable {
        Object read(Iterator<String> iterator) throws IncorrectFieldException;
    }
}
