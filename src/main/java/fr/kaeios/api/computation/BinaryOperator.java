package fr.kaeios.api.computation;

public interface BinaryOperator<R, X, Y> {

    /**
     * Compute operation of x by y
     *
     * @param x left operand
     * @param y right operand
     *
     * @return result of x by y
     */
    R compute(X x, Y y);

    /**
     * Check of operation is valid for x and y
     * @param x left operand
     * @param y right operand
     *
     * @return true if operation is valid
     */
    default boolean checkPreconditions(X x, Y y) { return true; }

}
