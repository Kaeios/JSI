package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.MatrixExtractor;
import fr.kaeios.matrix.MatrixOperations;

public class QRDecomposition implements UnaryOperator<QRDecomposition.QRResult, Matrix> {

    @Override
    public QRResult compute(Matrix operand) {
        int m = operand.getRowsCount();
        int n = operand.getColumnsCount();

        Matrix R = Matrix.from(m, n, CoefficientSupplier.from(operand.getValues()));
        Matrix Qt = Matrix.from(m, m, CoefficientSupplier.IDENTITY);

        int steps = Math.min(m, n);

        for (int k = 0; k < steps; k++) {

            // Compute norm of the vector a[k:m][k]
            Matrix km = R.extract(MatrixExtractor.sub(k, m, k, k+1));
            double norm = km.apply(MatrixOperations.L2);

            if (norm < 1e-12) continue;

            // Compute Householder vector u
            Matrix u = Matrix.from(1, m, CoefficientSupplier.ZEROS);
            u.getValues()[0][k] = R.getValues()[k][k] + (R.getValues()[k][k] >= 0 ? norm : -norm);
            for (int i = k + 1; i < m; i++) {
                u.getValues()[0][i] = R.getValues()[i][k];
            }

            // Normalize u
            double uNorm = u.apply(MatrixOperations.L2);
            if (uNorm < 1e-12) continue;
            u = u.dotApply(x -> x/uNorm);

            // Apply reflection to R
            for (int j = k; j < n; j++) {
                double dot = 0;
                for (int i = k; i < m; i++) {
                    dot += u.getValues()[0][i] * R.getValues()[i][j];
                }
                for (int i = k; i < m; i++) {
                    R.getValues()[i][j] -= 2 * u.getValues()[0][i] * dot;
                }
            }

            // Apply reflection to Qt
            for (int j = 0; j < m; j++) {
                double dot = 0;
                for (int i = k; i < m; i++) {
                    dot += u.getValues()[0][i] * Qt.getValues()[i][j];
                }
                for (int i = k; i < m; i++) {
                    Qt.getValues()[i][j] -= 2 * u.getValues()[0][i] * dot;
                }
            }
        }

        Matrix Q = Qt.apply(MatrixOperations.TRANSPOSE);

        return new QRResult(m, n, Q, R);
    }

    public static final class QRResult {

        private final int m;
        private final int n;

        private final Matrix Q;
        private final Matrix R;

        public QRResult(int m, int n, Matrix Q, Matrix R) {
            this.m = m;
            this.n = n;
            this.Q = Q;
            this.R = R;
        }

        public Matrix fullQ() {
            return Q;
        }

        public Matrix fullR() {
            return R;
        }

        public Matrix Q() {
            return Q.extract(MatrixExtractor.sub(0, m, 0, n));
        }

        public Matrix R() {
            return R.extract(MatrixExtractor.sub(0, n, 0, n));
        }

    }
}
