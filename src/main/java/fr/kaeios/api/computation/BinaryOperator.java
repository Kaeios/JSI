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

}
