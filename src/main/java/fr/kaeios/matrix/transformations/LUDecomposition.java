package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;

public class LUDecomposition implements UnaryOperator<LUDecomposition.LUResult, Matrix> {

    @Override
    public LUResult compute(Matrix operand) {
        int size = operand.getRowsCount();
        if (size != operand.getColumnsCount()) {
            throw new IllegalArgumentException("Matrix must be square for LU decomposition");
        }

        Matrix lower = Matrix.from(size, size, CoefficientSupplier.IDENTITY);
        Matrix upper = Matrix.from(size, size, CoefficientSupplier.from(operand.getValues()));
        Matrix permutations = Matrix.from(size, size, CoefficientSupplier.IDENTITY);

        double tolerance = 1e-10;

        int detP = 1;

        for (int i = 0; i < size; i++) {

            // Partial pivoting: find pivot row with max abs value in column i from rows i..end
            int pivotRow = i;
            double maxPivot = Math.abs(upper.getValues()[i][i]);
            for (int row = i + 1; row < size; row++) {
                double val = Math.abs(upper.getValues()[row][i]);
                if (val > maxPivot) {
                    maxPivot = val;
                    pivotRow = row;
                }
            }

            if (maxPivot < tolerance) {
                throw new ArithmeticException("Matrix is singular or nearly singular");
            }

            if (pivotRow != i) {
                // Swap rows i and pivotRow in upper
                Double[] tempRow = upper.getValues()[i];
                upper.getValues()[i] = upper.getValues()[pivotRow];
                upper.getValues()[pivotRow] = tempRow;

                // Swap rows i and pivotRow in permutations
                tempRow = permutations.getValues()[i];
                permutations.getValues()[i] = permutations.getValues()[pivotRow];
                permutations.getValues()[pivotRow] = tempRow;
                detP *= -1;

                // Swap first i columns of rows i and pivotRow in lower
                for (int col = 0; col < i; col++) {
                    double temp = lower.getValues()[i][col];
                    lower.getValues()[i][col] = lower.getValues()[pivotRow][col];
                    lower.getValues()[pivotRow][col] = temp;
                }
            }

            // Elimination below pivot
            for (int row = i + 1; row < size; row++) {
                double multiplier = upper.getValues()[row][i] / upper.getValues()[i][i];
                lower.getValues()[row][i] = multiplier;

                for (int col = i; col < size; col++) {
                    upper.getValues()[row][col] -= multiplier * upper.getValues()[i][col];
                }
            }
        }

        return new LUResult(detP, permutations, lower, upper);
    }

    public static final class LUResult {

        private final int detP;

        private final Matrix P;
        private final Matrix L;
        private final Matrix U;

        private LUResult(int detP, Matrix p, Matrix l, Matrix u) {
            this.detP = detP;
            P = p;
            L = l;
            U = u;
        }


        public Matrix P() {
            return P;
        }

        public Matrix L() {
            return L;
        }

        public Matrix U() {
            return U;
        }

        public int getDetP() {
            return detP;
        }

    }

}
