package general.exceptions;

public class IncorrectFieldException extends Exception {
    public IncorrectFieldException(Object details) {
        super(details.toString());
    }
}
