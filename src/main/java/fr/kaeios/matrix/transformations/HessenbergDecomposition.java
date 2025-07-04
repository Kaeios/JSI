package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.MatrixExtractor;
import fr.kaeios.matrix.MatrixOperations;

public class HessenbergDecomposition implements UnaryOperator<HessenbergDecomposition.HessenbergResult, Matrix> {

    @Override
    public HessenbergResult compute(Matrix operand) {
        int n = operand.getRowsCount();

        // Copy input matrix to H
        Matrix H = operand.extract(MatrixExtractor.sub(0, n, 0, n));
        // Initialize Q as Identity
        Matrix Q = Matrix.from(n, n, CoefficientSupplier.IDENTITY);

        for (int k = 0; k < n - 2; k++) {
            // Compute norm of the vector below the diagonal in column k
            double norm = 0.0;
            for (int i = k + 1; i < n; i++) {
                double val = H.getValues()[i][k];
                norm += val * val;
            }
            norm = Math.sqrt(norm);

            if (norm < 1e-12) continue; // Already zero, skip

            // Form Householder vector v
            double[] v = new double[n];
            double x1 = H.getValues()[k + 1][k];
            double sign = (x1 >= 0) ? 1.0 : -1.0;
            double u1 = x1 + sign * norm;

            v[k + 1] = u1;
            for (int i = k + 2; i < n; i++) {
                v[i] = H.getValues()[i][k];
            }

            // Normalize v (only from k+1 to n-1)
            double vNorm = 0.0;
            for (int i = k + 1; i < n; i++) {
                vNorm += v[i] * v[i];
            }
            vNorm = Math.sqrt(vNorm);

            for (int i = k + 1; i < n; i++) {
                v[i] /= vNorm;
            }

            // Apply Householder from the left: H = H - 2 v (v^T H)
            for (int j = 0; j < n; j++) {
                double dot = 0.0;
                for (int i = k + 1; i < n; i++) {
                    dot += v[i] * H.getValues()[i][j];
                }
                double coeff = 2.0 * dot;
                for (int i = k + 1; i < n; i++) {
                    H.getValues()[i][j] -= coeff * v[i];
                }
            }

            // Apply Householder from the right: H = H - 2 (H v) v^T
            for (int i = 0; i < n; i++) {
                double dot = 0.0;
                for (int j = k + 1; j < n; j++) {
                    dot += H.getValues()[i][j] * v[j];
                }
                double coeff = 2.0 * dot;
                for (int j = k + 1; j < n; j++) {
                    H.getValues()[i][j] -= coeff * v[j];
                }
            }

            // Accumulate Q: Q = Q (I - 2 v v^T)
            for (int i = 0; i < n; i++) {
                double dot = 0.0;
                for (int j = k + 1; j < n; j++) {
                    dot += Q.getValues()[i][j] * v[j];
                }
                double coeff = 2.0 * dot;
                for (int j = k + 1; j < n; j++) {
                    Q.getValues()[i][j] -= coeff * v[j];
                }
            }
        }

        return new HessenbergResult(H, Q);
    }

    public static final class HessenbergResult {

        private final Matrix H;
        private final Matrix Q;

        private HessenbergResult(Matrix H, Matrix Q) {
            this.H = H;
            this.Q = Q;
        }

        public Matrix H() {
            return H;
        }

        public Matrix Q() {
            return Q;
        }

        public Matrix Qt() {
            return Q.apply(MatrixOperations.TRANSPOSE);
        }

    }

}
