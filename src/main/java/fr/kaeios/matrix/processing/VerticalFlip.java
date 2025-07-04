package fr.kaeios.matrix.processing;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class VerticalFlip implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix operand) {
        int colCount = operand.getColumnsCount();

        return Matrix.from(operand.getRowsCount(), colCount, (i, j) -> operand.getValues()[i][colCount - j - 1]);
    }

}
