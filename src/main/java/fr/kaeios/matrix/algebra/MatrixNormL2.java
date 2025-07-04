package fr.kaeios.matrix.algebra;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class MatrixNormL2 implements UnaryOperator<Double, Matrix> {

    @Override
    public Double compute(Matrix operand) {
        double norm = 0;

        for (Double[] rows : operand.getValues()) {
            for (Double elements : rows) {
                norm += (elements * elements);
            }
        }

        return Math.sqrt(norm);
    }

}
