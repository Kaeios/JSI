package fr.kaeios.matrix.algebra;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixNormL0 implements UnaryOperator<Integer, Matrix> {

    @Override
    public Integer compute(Matrix operand) {
        int norm = 0;

        for (int i = 0; i < operand.getValues().length; i++) {
            for (int j = 0; j < operand.getValues()[i].length; j++) {
                norm += (Math.abs(operand.getValues()[i][j]) <= 0.00001D) ? 0 : 1 ;
            }
        }

        return norm;
    }

}