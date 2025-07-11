package fr.kaeios.matrix.operations.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.exceptions.MatrixShapeMismatchException;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;

public class MatrixMultiplication implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public boolean checkPreconditions(Matrix x, Matrix y) {
        if(x.getColumnsCount() != x.getRowsCount())
            throw new MatrixShapeMismatchException("Multiplying matrix of incompatible shapes");

        return true;
    }

    @Override
    public Matrix compute(Matrix matrix, Matrix matrix2) {
        Matrix result = Matrix.from(matrix.getRowsCount(), matrix2.getColumnsCount(), CoefficientSupplier.ZEROS);

        for (int i = 0; i < result.getRowsCount(); i++) {
            for (int j = 0; j < result.getColumnsCount(); j++) {
                for(int k = 0; k < matrix2.getRowsCount(); k++) {
                    result.getValues()[i][j] += matrix.getValues()[i][k] * matrix2.getValues()[k][j];
                }
            }
        }

        return result;
    }

}
