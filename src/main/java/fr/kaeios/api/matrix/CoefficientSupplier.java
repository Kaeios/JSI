package fr.kaeios.api.matrix;

import fr.kaeios.api.computation.BinaryOperator;

import java.util.Objects;

public interface CoefficientSupplier extends BinaryOperator<Double, Integer, Integer> {

    /**
     * Define coefficient value at x, y
     *
     * @param row row index of the coefficient to set
     * @param column column index of the coefficient to set
     *
     * @return the value of the coefficient at x, y
     */
    @Override
    Double compute(Integer row, Integer column);

    CoefficientSupplier IDENTITY = (row, col) -> Objects.equals(row, col) ? 1.0 : 0.0;
    CoefficientSupplier ZEROS = (row, col) -> 0.0;
    CoefficientSupplier ONES = (row, col) -> 1.0;

    static CoefficientSupplier diag(Double[] values) {
        return (row, col) -> Objects.equals(row, col) ? values[row] : 0.0;
    }

    static CoefficientSupplier from(Double[][] values) {
        return (row, col) -> values[row][col];
    }

}
