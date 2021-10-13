package general.exceptions;

public class NoSuchCommandException extends Exception {
    public NoSuchCommandException() {
        super( "no such command");
    }
}
