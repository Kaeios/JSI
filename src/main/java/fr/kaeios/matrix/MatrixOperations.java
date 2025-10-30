package fr.kaeios.matrix;

import fr.kaeios.api.computation.BinaryOperator;
import fr.kaeios.api.computation.UnaryOperator;
import fr.kaeios.api.matrix.Matrix;
import fr.kaeios.matrix.algebra.*;
import fr.kaeios.matrix.checks.CheckSymmetry;
import fr.kaeios.matrix.operations.unary.MatrixInverse;
import fr.kaeios.matrix.operations.binary.MatrixAddition;
import fr.kaeios.matrix.operations.binary.MatrixMultiplication;
import fr.kaeios.matrix.operations.unary.MatrixTranspose;
import fr.kaeios.matrix.operations.binary.MatrixScalarMultiplication;
import fr.kaeios.matrix.operations.binary.MatrixSubtraction;
import fr.kaeios.matrix.processing.HardThresholding;
import fr.kaeios.matrix.processing.Convolve1D;
import fr.kaeios.matrix.processing.NormalizeColumns;
import fr.kaeios.matrix.processing.VerticalFlip;
import fr.kaeios.matrix.transformations.*;

public class MatrixOperations {

    public static final BinaryOperator<Matrix, Matrix, Matrix> MUL = new MatrixMultiplication();
    public static final BinaryOperator<Matrix, Matrix, Matrix> ADD = new MatrixAddition();
    public static final BinaryOperator<Matrix, Matrix, Matrix> SUB = new MatrixSubtraction();
    public static final BinaryOperator<Matrix, Matrix, Double> SMUL = new MatrixScalarMultiplication();

    public static final UnaryOperator<Matrix, Matrix> TRANSPOSE = new MatrixTranspose();
    public static final UnaryOperator<Matrix, Matrix> INVERSE = new MatrixInverse();

    public static final UnaryOperator<LUDecomposition.LUResult, Matrix> LU = new LUDecomposition();
    public static final UnaryOperator<RowEchelonDecomposition.RowEchelonResult, Matrix> ROW_ECHELON = new RowEchelonDecomposition();
    public static final UnaryOperator<QRDecomposition.QRResult, Matrix> QR = new QRDecomposition();
    public static final UnaryOperator<EVDecomposition.EVDResult, Matrix> EVD = new EVDecomposition();
    public static final UnaryOperator<SVDecomposition.SVDResult, Matrix> SVD = new SVDecomposition();
    public static final UnaryOperator<HessenbergDecomposition.HessenbergResult, Matrix> HESSENBERG = new HessenbergDecomposition();

    public static final UnaryOperator<Double, Matrix> DET = new MatrixDeterminant();
    public static final UnaryOperator<Double, Matrix> TRACE = new MatrixTrace();
    public static final UnaryOperator<Integer, Matrix> RANK = new MatrixRank();
    public static final UnaryOperator<Integer, Matrix> L0 = new MatrixNormL0();
    public static final UnaryOperator<Double, Matrix> L1 = new MatrixNormL1();
    public static final UnaryOperator<Double, Matrix> L2 = new MatrixNormL2();

    public static final UnaryOperator<Matrix, Matrix> NORM_COLS = new NormalizeColumns();
    public static final BinaryOperator<Matrix, Matrix, Double> THRESHOLD = new HardThresholding();
    public static final UnaryOperator<Matrix, Matrix> V_FLIP = new VerticalFlip();
    public static final BinaryOperator<Matrix, Matrix, Matrix> CONV_1D = new Convolve1D();

    public static final UnaryOperator<Boolean, Matrix> CHECK_SYM = new CheckSymmetry();

}
