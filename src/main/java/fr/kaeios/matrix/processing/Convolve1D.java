package fr.kaeios.matrix.processing;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.exceptions.MatrixShapeMismatchException;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;

public class Convolve1D implements BinaryOperator<Matrix, Matrix, Matrix> {

    @Override
    public boolean checkPreconditions(Matrix x, Matrix y) {
        if(x.getRowsCount() != 1 || y.getRowsCount() != 1)
            throw new MatrixShapeMismatchException("Both signal and filter should be 1D for 1D convolution");

        return true;
    }

    @Override
    public Matrix compute(Matrix signal, Matrix filter) {
        // TODO Assert 1 row

        int convCenter = filter.getColumnsCount() / 2;
        int signalLength = signal.getColumnsCount();

        Double[][] result = new Double[1][signalLength];

        for (int i = 0; i < signalLength; i++) {
            result[0][i] = 0.0D;
            for(int j = -convCenter; j <= convCenter; j++) {
                result[0][i] += filter.getValues()[0][convCenter + j] * getValueAt(signal, i + j);
            }
        }

        return Matrix.from(1, signalLength, CoefficientSupplier.from(result));
    }

    private static double getValueAt(Matrix matrix, int index) {
        if(index < 0 || index >= matrix.getColumnsCount()) return 0;
        return matrix.getValues()[0][index];
    }

}
