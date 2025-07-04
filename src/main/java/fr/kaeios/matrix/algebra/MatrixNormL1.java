package fr.kaeios.matrix.algebra;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixNormL1 implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {
        double norm = 0.0D;

        for (int i = 0; i < operand.getValues().length; i++) {
            for (int j = 0; j < operand.getValues()[i].length; j++) {
                norm += Math.abs(operand.getValues()[i][j]);
            }
        }

        return norm;
    }

}
