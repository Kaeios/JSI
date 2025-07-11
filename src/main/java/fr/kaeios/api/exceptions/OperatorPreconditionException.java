package fr.kaeios.api.exceptions;

public class OperatorPreconditionException extends IllegalArgumentException {

    public OperatorPreconditionException() {
    }

    public OperatorPreconditionException(String s) {
        super(s);
    }

    public OperatorPreconditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperatorPreconditionException(Throwable cause) {
        super(cause);
    }
}
