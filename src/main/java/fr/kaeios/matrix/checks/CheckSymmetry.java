package fr.kaeios.matrix.checks;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;

public class CheckSymmetry implements UnaryOperator<Boolean, Matrix> {

    @Override
    public Boolean compute(Matrix operand) {
        int n = operand.getRowsCount();
        int m = operand.getColumnsCount();

        if(n != m) return false;

        for(int i = 0; i < m; i++) {
            for(int j = 0; j < i; j++) {
                if(Math.abs(operand.getValues()[i][j] - operand.getValues()[j][i]) > 1e-7) return false;
            }
        }

        return true;
    }

}
