package fr.kaeios.matrix.operations.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.exceptions.MatrixShapeMismatchException;
import fr.kaeios.api.matrix.Matrix;

public class MatrixAddition implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public boolean checkPreconditions(Matrix x, Matrix y) {
        if(x.getRowsCount() != y.getRowsCount() || x.getColumnsCount() != y.getColumnsCount())
            throw new MatrixShapeMismatchException("Adding matrix of different shapes");

        return true;
    }

    @Override
    public Matrix compute(Matrix matrix, Matrix matrix2) {
        return matrix.dotApply(matrix2, Double::sum);
    }

}
