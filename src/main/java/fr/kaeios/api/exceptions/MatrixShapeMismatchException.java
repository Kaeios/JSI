package fr.kaeios.api.exceptions;

public class MatrixShapeMismatchException extends OperatorPreconditionException {

    public MatrixShapeMismatchException() {
    }

    public MatrixShapeMismatchException(String s) {
        super(s);
    }

    public MatrixShapeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixShapeMismatchException(Throwable cause) {
        super(cause);
    }
}
