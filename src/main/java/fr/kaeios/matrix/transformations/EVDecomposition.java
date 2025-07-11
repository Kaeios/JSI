package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.api.matrix.MatrixExtractor;
import fr.kaeios.matrix.MatrixOperations;

// TODO Replace inverse with LU solving
public class EVDecomposition implements UnaryOperator<SymmetricEVDecomposition.EVDResult, Matrix> {

    @Override
    public SymmetricEVDecomposition.EVDResult compute(Matrix operand) {

        Matrix H;
        Matrix Qtot;

        if(operand.apply(MatrixOperations.CHECK_SYM)) {
            H = operand;
            Qtot = Matrix.from(operand.getRowsCount(), operand.getColumnsCount(), CoefficientSupplier.IDENTITY);
        } else {
            HessenbergDecomposition.HessenbergResult HQ = operand.apply(MatrixOperations.HESSENBERG);
            H = HQ.H();
            Qtot = HQ.Q();
        }

        for(int i = 0; i < 10000; i++) {
            QRDecomposition.QRResult QR = H.apply(MatrixOperations.QR);
            H = QR.fullR().apply(QR.fullQ(), MatrixOperations.MUL);
            Qtot = Qtot.apply(QR.fullQ(), MatrixOperations.MUL);

            if (isConverged(H, 1e-10)) {
                break;
            }
        }

        return refineEigenvectorsByRQI(operand, Qtot, 10);
    }

    private static class EigenPair implements Comparable<EigenPair> {
        double value;
        double[] vector;

        EigenPair(double v, double[] vec) {
            this.value = v;
            this.vector = vec;
        }

        @Override
        public int compareTo(EigenPair other) {
            // Sort descending by absolute value of eigenvalue
            return Double.compare(Math.abs(other.value), Math.abs(this.value));
        }
    }

    private SymmetricEVDecomposition.EVDResult refineEigenvectorsByRQI(Matrix A, Matrix Q, int maxRQIIter) {
        int n = A.getRowsCount();
        double[][] refinedVectors = new double[n][n];
        double[] refinedEigenvalues = new double[n];

        for (int col = 0; col < n; col++) {
            Matrix x = Q.extract(MatrixExtractor.column(col));

            double mu = 0.0;
            for (int iter = 0; iter < maxRQIIter; iter++) {
                // Rayleigh quotient mu = (x^T A x) / (x^T x)
                Matrix Ax = A.apply(x, MatrixOperations.MUL);
                double numerator = x.apply(MatrixOperations.TRANSPOSE).apply(Ax, MatrixOperations.MUL).getValues()[0][0];
                double denominator = x.apply(MatrixOperations.TRANSPOSE).apply(x, MatrixOperations.MUL).getValues()[0][0];

                mu = numerator / denominator;

                // Build shifted matrix (A - mu I)
                double finalMu = mu + 10e-8;
                Matrix shifted = A.apply(Matrix.from(n, n, CoefficientSupplier.IDENTITY).dotApply(t -> t * finalMu), MatrixOperations.SUB);

                // Solve (A - mu I) y = x
                // Use LU or stable solver instead of inverse for numerical stability
                Matrix y = shifted.apply(MatrixOperations.INVERSE).apply(x, MatrixOperations.MUL);

                // Normalize y
                double normY = y.apply(MatrixOperations.L2);
                y.dotApply(t -> t / normY);

                x = y;
            }

            // Save refined vector and eigenvalue
            refinedEigenvalues[col] = mu;
            for (int i = 0; i < n; i++) {
                refinedVectors[i][col] = x.getValues()[i][0];
            }
        }

        // Sort eigenvalues and eigenvectors together by eigenvalue magnitude
        EigenPair[] pairs = new EigenPair[n];
        for (int i = 0; i < n; i++) {
            double[] vec = new double[n];
            for (int j = 0; j < n; j++) {
                vec[j] = refinedVectors[j][i];
            }
            pairs[i] = new EigenPair(refinedEigenvalues[i], vec);
        }
        java.util.Arrays.sort(pairs);

        // Build sorted matrix Q
        Double[][] sortedVectors = new Double[n][n];
        Double[] sortedEigenvalues = new Double[n];
        for (int i = 0; i < n; i++) {
            sortedEigenvalues[i] = pairs[i].value;
            for (int j = 0; j < n; j++) {
                sortedVectors[j][i] = pairs[i].vector[j];
            }
        }

        // Copy sorted vectors back into matrix form
        Matrix refinedQ = Matrix.from(n, n, CoefficientSupplier.from(sortedVectors));
        // Optionally, normalize columns to unit length
        refinedQ = refinedQ.apply(MatrixOperations.NORM_COLS);

        return new SymmetricEVDecomposition.EVDResult(refinedQ, Matrix.from(n, n, CoefficientSupplier.diag(sortedEigenvalues)));
    }

    private boolean isConverged(Matrix H, double tol) {
        int n = H.getRowsCount();
        double sumSubDiag = 0.0;
        for (int i = 1; i < n; i++) {
            sumSubDiag += Math.abs(H.getValues()[i][i - 1]);
        }
        return sumSubDiag < tol;
    }



}
