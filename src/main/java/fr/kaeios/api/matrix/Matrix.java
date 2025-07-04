package fr.kaeios.api.matrix;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;

public interface Matrix {

    /**
     * Get row dimension of the matrix
     * @return number of rows
     */
    int getRowsCount();

    /**
     * Get column dimension of the matrix
     * @return number of columns
     */
    int getColumnsCount();

    /**
     * Get Matrix ad a 2D array
     * @return a Double array corresponding to matrix
     */
    Double[][] getValues();

    /**
     * Apply a function between two matrix term by term
     *
     * @param other right operand of the calculus
     * @param operator operation to apply
     *
     * @return result of the computation as a new Matrix
     */
    Matrix dotApply(Matrix other, BinaryOperator<Double, Double, Double> operator);

    /**
     * Apply a function to each coefficient of a matrix
     *
     * @param operator operation to apply
     *
     * @return result of the computation as a new matrix
     */
    Matrix dotApply(UnaryOperator<Double, Double> operator);

    /**
     * Apply an operation to the matrix
     *
     * @param operator operation to apply
     *
     * @return Result of the calculus with type R
     *
     * @param <R> Type of the return value
     */
    <R> R apply(UnaryOperator<R, Matrix> operator);

    /**
     * Apply an operation between the matrix and a right operand
     *
     * @param other right operand of the calculus
     * @param operator operation to apply between operands
     *
     * @return result of the calculus
     *
     * @param <R> type of the return value
     * @param <O> type of the right operand
     */
    <R, O> R apply(O other, BinaryOperator<R, Matrix, O> operator);

    Matrix extract(MatrixExtractor extractor);

    static Matrix from(int row, int col, CoefficientSupplier supplier) {
        Double[][] values = new Double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                values[i][j] = supplier.compute(i, j);
            }
        }

        return new Impl(row, col, values);
    }

    class Impl implements Matrix {

        private final int rowsCount;
        private final int columnsCount;

        private final Double[][] values;

        private Impl(final int rowsCount, final int columnsCount, final Double[][] values) {
            this.rowsCount = rowsCount;
            this.columnsCount = columnsCount;
            this.values = values;
        }

        @Override
        public int getRowsCount() {
            return rowsCount;
        }

        @Override
        public int getColumnsCount() {
            return columnsCount;
        }

        @Override
        public Double[][] getValues() {
            return values;
        }

        @Override
        public Matrix dotApply(Matrix other, BinaryOperator<Double, Double, Double> operator) {
            // TODO Error if size does not match

            Double[][] newValues = new Double[rowsCount][columnsCount];

            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < columnsCount; j++) {
                    newValues[i][j] = operator.compute(this.values[i][j], other.getValues()[i][j]);
                }
            }

            return new Impl(this.rowsCount, this.columnsCount, newValues);
        }

        @Override
        public Matrix dotApply(UnaryOperator<Double, Double> operator) {
            // TODO Error if size does not match

            Double[][] newValues = new Double[rowsCount][columnsCount];

            for (int i = 0; i < rowsCount; i++) {
                for (int j = 0; j < columnsCount; j++) {
                    newValues[i][j] = operator.compute(this.values[i][j]);
                }
            }

            return new Impl(this.rowsCount, this.columnsCount, newValues);

        }

        @Override
        public <R> R apply(UnaryOperator<R, Matrix> operator) {
            return operator.compute(this);
        }

        @Override
        public <R, O> R apply(O other, BinaryOperator<R, Matrix, O> operator) {
            return operator.compute(this, other);
        }

        @Override
        public Matrix extract(MatrixExtractor extractor) {
            return extractor.extract(this);
        }

    }

}
