package fr.kaeios.matrix.operations.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixAddition implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix matrix, Matrix matrix2) {
        return matrix.dotApply(matrix2, Double::sum);
    }

}
