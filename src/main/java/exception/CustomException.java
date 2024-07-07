package exception;

public class CustomException extends IllegalArgumentException {
    public CustomException(String message, CustomException e) {
        super(message);
    }
}
