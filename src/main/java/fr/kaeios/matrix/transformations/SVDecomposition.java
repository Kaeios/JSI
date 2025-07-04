package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;


public class SVDecomposition implements UnaryOperator<SVDecomposition.SVDResult, Matrix> {

    @Override
    public SVDResult compute(Matrix operand) {

        int m = operand.getRowsCount();
        int n = operand.getColumnsCount();

        Matrix At = operand.apply(MatrixOperations.TRANSPOSE);
        Matrix AtA = At.apply(operand, MatrixOperations.MUL);

        SymmetricEVDecomposition.EVDResult evd = AtA.apply(MatrixOperations.EVD);
        Matrix V = evd.V();
        Matrix D = evd.D();

        Double[] sigmaValues = new Double[Math.min(m, n)];
        for (int i = 0; i < Math.min(m, n); i++) {
            sigmaValues[i] = Math.sqrt(Math.max(0, D.getValues()[i][i]));
        }
        Matrix Sigma = Matrix.from(m, n, CoefficientSupplier.diag(sigmaValues));

        Matrix SigmaInv = Matrix.from(Sigma.getRowsCount(), Sigma.getRowsCount(), CoefficientSupplier.ZEROS);

        // Diagonal matrix pseudo inverse
        for (int i = 0; i < Sigma.getRowsCount(); i++) {
            double val = Sigma.getValues()[i][i];
            if (Math.abs(val) > 0.000001D) {
                SigmaInv.getValues()[i][i] = 1.0 / val;
            } else {
                SigmaInv.getValues()[i][i] = 0.0;
            }
        }

        Matrix U = operand.apply(V, MatrixOperations.MUL).apply(SigmaInv, MatrixOperations.MUL);

        return new SVDResult(U, V, Sigma);
    }

    public static final class SVDResult {

        private final Matrix U;
        private final Matrix V;
        private final Matrix Sigma;

        private SVDResult(Matrix U, Matrix V, Matrix Sigma) {
            this.U = U;
            this.V = V;
            this.Sigma = Sigma;
        }

        public Matrix U() {
            return U;
        }

        public Matrix V() {
            return V;
        }

        public Matrix Sigma() {
            return Sigma;
        }

        public Matrix Vt() {
            return V.apply(MatrixOperations.TRANSPOSE);
        }

    }

}
