package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;

public class SymmetricEVDecomposition implements UnaryOperator<SymmetricEVDecomposition.EVDResult, Matrix> {

    @Override
    public EVDResult compute(Matrix operand) {
        int size = operand.getRowsCount();

        Matrix A = Matrix.from(size, size, CoefficientSupplier.from(operand.getValues()));
        Matrix V = Matrix.from(size, size, CoefficientSupplier.ZEROS);
        Matrix D = Matrix.from(size, size, CoefficientSupplier.ZEROS);

        int maxIter = 1000;
        double tol = 1e-10;

        for (int i = 0; i < size; i++) {
            Double[] b = new Double[size];
            for (int j = 0; j < size; j++) b[j] = Math.random();

            normalize(b);
            Double[] bNew = new Double[size];
            double lambdaOld = 0.0;

            for (int iter = 0; iter < maxIter; iter++) {
                // Multiply A * b
                for (int j = 0; j < size; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < size; k++) {
                        sum += A.getValues()[j][k] * b[k];
                    }
                    bNew[j] = sum;
                }

                normalize(bNew);

                double lambda = 0.0;
                for (int j = 0; j < size; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < size; k++) {
                        sum += A.getValues()[j][k] * bNew[k];
                    }
                    lambda += bNew[j] * sum;
                }

                if (Math.abs(lambda - lambdaOld) < tol) {
                    for (int j = 0; j < size; j++) {
                        V.getValues()[j][i] = bNew[j];
                    }
                    D.getValues()[i][i] = lambda;
                    break;
                }

                lambdaOld = lambda;
                System.arraycopy(bNew, 0, b, 0, size);
            }

            // Deflate: A = A - λ * v * v^T
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    A.getValues()[r][c] -= D.getValues()[i][i] * V.getValues()[r][i] * V.getValues()[c][i];
                }
            }
        }

        return new EVDResult(V, D);
    }

    private void normalize(Double[] v) {
        double norm = 0.0;
        for (Double val : v) norm += val * val;
        norm = Math.sqrt(norm);
        for (int i = 0; i < v.length; i++) v[i] /= norm;
    }

    public static final class EVDResult {

        private final Matrix V;
        private final Matrix D;

        public EVDResult(Matrix V, Matrix D) {
            this.V = V;
            this.D = D;
        }

        public Matrix V() {
            return V;
        }

        public Matrix D() {
            return D;
        }

        public Matrix Vinv() {
            return V.apply(MatrixOperations.INVERSE);
        }
    }

}
