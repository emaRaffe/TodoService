package sys.exception;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(final String message) {
	super(message);
    }
}
