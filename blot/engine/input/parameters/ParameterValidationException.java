package blot.engine.input.parameters;

/**
 * Example uses: to make sure someone types in a number, to make sure someone types anything at all
 */
public class ParameterValidationException extends Exception {
    private String message;

    public ParameterValidationException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessageForDisplay() {
        return message;
    }
}
