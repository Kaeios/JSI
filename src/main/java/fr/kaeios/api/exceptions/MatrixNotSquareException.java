package fr.kaeios.api.exceptions;

public class MatrixNotSquareException extends OperatorPreconditionException {

    public MatrixNotSquareException() {
    }

    public MatrixNotSquareException(String s) {
        super(s);
    }

    public MatrixNotSquareException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixNotSquareException(Throwable cause) {
        super(cause);
    }
}
