package fr.kaeios.matrix.algebra;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;
import fr.kaeios.matrix.transformations.LUDecomposition;

public class MatrixRank implements UnaryOperator<Integer, Matrix> {

    @Override
    public Integer compute(Matrix operand) {

        LUDecomposition.LUResult reduced = operand.apply(MatrixOperations.LU);

        Matrix L = reduced.L();

        for (int i = 0; i < L.getRowsCount(); i++) {
            if(Math.abs(L.getValues()[i][i]) < 0.00001D) return i;
        }

        return L.getRowsCount();
    }

}
