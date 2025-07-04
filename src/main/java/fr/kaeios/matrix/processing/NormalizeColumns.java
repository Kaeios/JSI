package fr.kaeios.matrix.processing;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;

public class NormalizeColumns implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix operand) {

        double[] sums = new double[operand.getColumnsCount()];

        for(int j = 0; j < operand.getColumnsCount(); j++) {
            for (int i = 0; i < operand.getRowsCount(); i++) {
                sums[j] += (operand.getValues()[i][j] * operand.getValues()[i][j]);
            }
        }

        for (int i = 0; i < sums.length; i++) {
            sums[i] = Math.sqrt(sums[i]);
        }

        Double[][] values = new Double[operand.getRowsCount()][operand.getColumnsCount()];

        for(int i = 0; i < operand.getRowsCount(); i++) {
            for(int j = 0; j < operand.getColumnsCount(); j++) {
                values[i][j] = operand.getValues()[i][j] / sums[j];
            }
        }

        return Matrix.from(operand.getRowsCount(), operand.getColumnsCount(), CoefficientSupplier.from(values));
    }

}
