package fr.kaeios.api.computation;

public interface UnaryOperator<R, O> {

    /**
     *
     * @param operand Operand to apply operation to
     *
     * @return result of the operation by operand
     */
    R compute(O operand);

    /**
     * Check of operation is valid for operand
     *
     * @param operand Operand to apply operation to
     *
     * @return true if operation is valid
     */
    default boolean checkPreconditions(O operand) { return true; }

}
