package blot.engine.input.parameters;

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
