package fr.kaeios.matrix.algebra;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.exceptions.MatrixNotSquareException;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;
import fr.kaeios.matrix.transformations.LUDecomposition;

public class MatrixDeterminant implements UnaryOperator<Double, Matrix> {

    @Override
    public boolean checkPreconditions(Matrix operand) {
        if(operand.getRowsCount() != operand.getColumnsCount())
            throw new MatrixNotSquareException("Computing determinant of a non square matrix");

        return true;
    }

    @Override
    public Double compute(Matrix operand) {

        LUDecomposition.LUResult reduced = operand.apply(MatrixOperations.LU);

        Matrix U = reduced.U();

        double det = reduced.getDetP();

        for (int i = 0; i < U.getRowsCount(); i++) {
            det *= U.getValues()[i][i];
        }

        return det;
    }

}
