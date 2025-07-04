package fr.kaeios.matrix.operations.binary;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixScalarMultiplication implements BinaryOperator<Matrix, Matrix, Double> {

    @Override
    public Matrix compute(Matrix matrix, Double scalar) {
        return matrix.dotApply(x -> x * scalar);
    }

}
