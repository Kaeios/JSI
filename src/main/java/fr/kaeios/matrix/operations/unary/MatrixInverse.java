package fr.kaeios.matrix.operations.unary;

import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.CoefficientSupplier;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.MatrixOperations;
import fr.kaeios.matrix.transformations.LUDecomposition;

public class MatrixInverse implements UnaryOperator<Matrix, Matrix> {

    @Override
    public Matrix compute(Matrix matrix) {
        Matrix operand = Matrix.from(matrix.getRowsCount(), matrix.getColumnsCount(), CoefficientSupplier.from(matrix.getValues()));
        int size = operand.getRowsCount();

        LUDecomposition.LUResult LU = operand.apply(MatrixOperations.LU);

        Matrix P = LU.P();
        Matrix L = LU.L();
        Matrix U = LU.U();

        Matrix x = Matrix.from(size, size, CoefficientSupplier.ZEROS);
        Matrix y = Matrix.from(size, size, CoefficientSupplier.ZEROS);

        Matrix b = Matrix.from(size, size, CoefficientSupplier.IDENTITY);
        Matrix Pb = P.apply(MatrixOperations.TRANSPOSE).apply(b, MatrixOperations.MUL);

         // Forward substitution to solve Ly = P^T b
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                double sum = Pb.getValues()[i][j];
                for (int k = 0; k < i; ++k)
                    sum -= L.getValues()[i][k] * y.getValues()[k][j];
                y.getValues()[i][j] = sum;
            }
        }

        // Backward substitution to solve Ux = y
        for (int i = size - 1; i >= 0; --i) {
            for (int j = 0; j < size; ++j) {
                double sum = y.getValues()[i][j];
                for (int k = i + 1; k < size; ++k)
                    sum -= U.getValues()[i][k] * x.getValues()[k][j];
                x.getValues()[i][j] = sum / U.getValues()[i][i];
            }
        }

        return x;
    }

}
