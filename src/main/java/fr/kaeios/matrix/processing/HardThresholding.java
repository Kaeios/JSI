package fr.kaeios.matrix.processing;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class HardThresholding implements BinaryOperator<Matrix, Matrix, Double> {

    @Override
    public Matrix compute(Matrix matrix, Double treshold) {
        return Matrix.from(
                matrix.getRowsCount(),
                matrix.getColumnsCount(),
                (i, j) -> Math.abs(matrix.getValues()[i][j]) > treshold ? matrix.getValues()[i][j] : 0.0D
        );
    }

}
