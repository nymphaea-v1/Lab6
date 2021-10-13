package general.exceptions;

public class CancelCommandException extends RuntimeException {
    public CancelCommandException(String message) {
        super("Current command execution has been cancelled (" + message + ")");
    }

    public CancelCommandException() {
        super("Current command execution has been cancelled");
    }
}
