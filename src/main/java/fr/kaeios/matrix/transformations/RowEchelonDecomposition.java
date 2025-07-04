package fr.kaeios.matrix.transformations;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;

public class RowEchelonDecomposition implements UnaryOperator<RowEchelonDecomposition.RowEchelonResult, Matrix> {

    @Override
    public RowEchelonResult compute(Matrix operand) {

        LUDecomposition.LUResult LU = operand.apply(MatrixOperations.LU);

        Matrix L = LU.L();
        Matrix U = LU.U();

        for (int i = 0; i < U.getRowsCount(); i++) {
            double coeff = U.getValues()[i][i];
            if(Math.abs(coeff) < 0.00001D) break;


            for (int k = i; k < U.getRowsCount(); k++) {
                U.getValues()[i][k] = U.getValues()[i][k] / coeff;
                L.getValues()[k][i] = L.getValues()[k][i] * coeff;
            }
        }

        return new RowEchelonResult(LU.getDetP(), LU.P(), L, U);
    }

    public static final class RowEchelonResult {

        private final int detP;

        private final Matrix P;
        private final Matrix L;
        private final Matrix U;

        private RowEchelonResult(int detP, Matrix p, Matrix l, Matrix u) {
            this.detP = detP;
            P = p;
            L = l;
            U = u;
        }

        public int getDetP() {
            return detP;
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

    }

}
