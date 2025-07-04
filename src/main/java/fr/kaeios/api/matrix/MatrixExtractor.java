package fr.kaeios.api.matrix;

public interface MatrixExtractor {

    Matrix extract(Matrix matrix);

    static MatrixExtractor row(int rowIndex) {
        return matrix -> Matrix.from(1, matrix.getColumnsCount(), (i, j) -> matrix.getValues()[rowIndex][j]);
    }

    static MatrixExtractor column(int colIndex) {
        return matrix -> Matrix.from(matrix.getRowsCount(), 1, (i, j) -> matrix.getValues()[i][colIndex]);
    }

    static MatrixExtractor sub(int startRow, int endRow, int startCol, int endCol) {
        return matrix -> Matrix.from(endRow - startRow, endCol - startCol, (i, j) -> matrix.getValues()[i + startRow][j + startCol]);
    }

    static MatrixExtractor diag() {
        return matrix -> Matrix.from(1, Math.min(matrix.getRowsCount(), matrix.getColumnsCount()), (i, j) -> matrix.getValues()[j][j]);
    }

}
