package general.exceptions;

public class IncorrectArgumentException extends Exception {
    public IncorrectArgumentException(String details) {
        super("Incorrect argument (" + details + ").");
    }
}
