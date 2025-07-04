package fr.kaeios.matrix.operations.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixTranspose implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix operand) {
        return Matrix.from(operand.getColumnsCount(), operand.getRowsCount(), (x, y) -> operand.getValues()[y][x]);
    }

}
