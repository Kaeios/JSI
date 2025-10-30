# Matrix computation library

---

## Creating Matrix : 

The basic object manipulated is type `Matrix`, you can manipulate it through a standard interface. 
There are factory methods for creating matrix with predetermined coefficients.

**Usage :**

```JAVA
    Matrix a = Matrix.from(row_count, col_count, coefficient_supplier);
```

A coefficient supplier implement the interface `CoefficientSupplier` which contains 
a single method `supply(rowIndex, colIndex)` which can be implemented through a lambda expression

**Example of implementation for identity supplier :**

```(i, j) -> i==j ? 1 : 0;```

Some commonly used suppliers are already implement and accessible through static fields and methods in 
the CoefficientSupplier interface. This includes : 

| Field             | Description                                         |
|-------------------|-----------------------------------------------------|
| IDENTITY          | Put zero in first diagonal, zero elsewhere          |
| ZEROS             | Put zeros in all coefficients                       |
| ONES              | Put ones in all coefficients                        |
| diag(Double[])    | Put coefficients from provided list in the diagonal |
| from(Double[][])  | Put coefficients from provided list                 |

**Example creating a matrix with coefficients :**

```JAVA
Matrix a = Matrix.from(3, 3, CoefficientSupplier.from(new Double[][]{
        {1.0, 2.0, 3.0},
        {4.0, 5.0, 6.0},
        {7.0, 8.0, 9.0}
})); 
```

**Example creating an identity matrix :**

```JAVA
Matrix a = Matrix.from(3, 3, CoefficientSupplier.IDENTITY); 
```

---

## Performing operations

All operations on matrices are performed through four methods :

| Method                             | Description                                             |
|------------------------------------|---------------------------------------------------------|
| R apply(T, BinaryOperator)         | Perform the operation with matrix and first parameter   |
| R apply(UnaryOperator)             | Perform an operation on the matrix                      |
| Matrix dotApply(UnaryOperator)     | Apply operation on all coefficients of the matrix       |
| Matrix dotApply(T, BinaryOperator) | Apply operation with first parameter on all coefficient |

**Example of unary operation :**

```JAVA
    Matrix At = A.apply(MatrixOperations.TRANSPOSE);
    Double trace = A.apply(MatrixOperations.TRACE);
    Double norm = A.apply(MatrixOperations.L2);
```

**Example of binary operation :**

```JAVA
    Matrix C = A.apply(B, MatrixOperations.MUL); // matrix product
    Matrix D = A.apply(B, MatrixOperations.ADD); // sum matrix
    Matrix E = A.apply(10, MatrixOperations.SMUL); // Multiply by a scalar
```

**Example of coefficient operation :**

```JAVA
    Matrix half_A = A.dotApply(x -> x/2); // Divide all coefficient by two
```

## Performing transformations

The library also allows for computing plenty of common matrix transformations like EVD, SVD, LU, ...
Because by conception everything is unified, a matrix transformation is simply defined as an operator returning multiple
matrix. For convenience matrix part of a decomposition are stored in wrapper object that allows to identify clearly
the role of each matrix in the decomposition.

**Example of EVD decomposition :**

```JAVA
EVDecomposition.EVDResult EVD = A.apply(MatrixOperations.EVD);

Matrix D = EVD.D();
Matrix V = EVD.V();
Matrix Vinv = EVD.Vinv();
```

## Available operators / transformations

You can see available transformations / operations in the file matrix/MatrixOperations.java
You can also implement your own, just open the code of an already implemented transformation / operation
to see examples.

## Code example using this library

**Solving linear ODE system example :**

Solving an ODE system of the form **x' = Ax + B** where A, B, x, x' are matrix

```JAVA

Matrix A = Matrix.from(3, 3, CoefficientSupplier.from(new Double[][] {
        {-1.0,  0.0,  0.0},
        { 1.0, -2.0,  0.0},
        { 0.0,  2.0,  0.0}
}));

Matrix B = Matrix.from(3, 1, CoefficientSupplier.from(new Double[][] {
        {1.0},
        {0.0},
        {0.0}
}));

// EVD decomposition of A
EVDecomposition.EVDResult EVD = A.apply(MatrixOperations.EVD);

// Exponential coefficients are the singular values
Matrix exponents = EVD.D().extract(MatrixExtractor.diag());

// Linear combination weights are eigen vectors
Matrix constants = EVD.Vinv().apply(B, MatrixOperations.MUL);

// Solution of the system is a list of function (aka. unary operator) 
UnaryOperator<Double, Double>[] results = new UnaryOperator[A.getRowsCount()];

for (int i = 0; i < A.getRowsCount(); i++) {
    // Get corresponding eigen vector
    Matrix p = EVD.V().extract(MatrixExtractor.row(i));
    
    // Compute linear combination (in a lambda to create a function)
    results[i] = x -> {
        double sum = 0.0D;

        for (int j = 0; j < A.getRowsCount(); j++) {
            sum += p.getValues()[0][j] * constants.getValues()[j][0] * Math.exp(x * exponents.getValues()[0][j]);
        }

        return sum;
    };
}

// Additional code to save the results plotted on graph using JChart
XYSeriesCollection dataset = new XYSeriesCollection();

for (int i = 0; i < A.getRowsCount(); i++) {
    UnaryOperator<Double, Double> func = results[i];

    XYSeries signal = new XYSeries("Function " + i);
    for(Double x = 0.0; x < 4.0; x += 0.01)
        signal.add(x, func.compute(x));

    dataset.addSeries(signal);
}

JFreeChart xyLineChart = ChartFactory.createXYLineChart(
        "ODE Solving",
        "Time",
        "Amplitude",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        true
);

BufferedImage img = xyLineChart.createBufferedImage(1920, 1080);
File outputfile = new File("image.png");
try {
    ImageIO.write(img, "png", outputfile);
    System.out.println(outputfile.getAbsolutePath());
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

**Local Polynomial Interpolation Example :**

```JAVA
// Length of signal
int LENGTH = 1000;
// Max polynomial degree
int N = 2;
// Window size
int M = 15;
// Window center
int center = M / 2;

// Gen time steps
Matrix ty = Matrix.from(1, LENGTH, (i, j) -> j/(double)(LENGTH-1));
// generate a signal
Matrix y = ty.dotApply(t -> Math.sin(2 / (t + 0.05)));
// add noise to signal
y = y.dotApply(x -> x + RNG.nextGaussian() * 0.1);

// generate base matrix for LPA
Matrix t = Matrix.from(1, M, (i, j) -> j/(double)(M-1));
Matrix T = Matrix.from(M, N+1, (i, j) -> Math.pow(t.getValues()[0][i], j));

// Compute filter for convolution
QRDecomposition.QRResult QR = T.apply(MatrixOperations.QR);
Matrix Q = QR.Q();
Matrix row = Q.extract(MatrixExtractor.row(center));
Matrix g = row.apply(Q.apply(MatrixOperations.TRANSPOSE), MatrixOperations.MUL).apply(MatrixOperations.V_FLIP);

// Synthesis of signal
Matrix Shat = y.apply(g, MatrixOperations.CONV_1D);

// Additional code to export results on a plot
XYSeries signal = new XYSeries("Input Signal");
for(int i = 0; i < LENGTH; i++)
    signal.add(ty.getValues()[0][i], y.getValues()[0][i]);

XYSeries lpa = new XYSeries("LPA");
for(int i = 0; i < LENGTH; i++)
    lpa.add(ty.getValues()[0][i], Shat.getValues()[0][i]);

XYSeriesCollection dataset = new XYSeriesCollection();
dataset.addSeries(signal);
dataset.addSeries(lpa);

JFreeChart xyLineChart = ChartFactory.createXYLineChart(
        "Test XY Line",
        "Time",
        "Amplitude",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        true
);

BufferedImage img = xyLineChart.createBufferedImage(1920, 1080);
File outputfile = new File("image.png");
try {
    ImageIO.write(img, "png", outputfile);
    System.out.println(outputfile.getAbsolutePath());
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

**Denoising using DCT basis + Hard thresholding**

```JAVA
int n = 128; // Signal dimension

// Matrix of signal representing an ECG signal
Matrix S = Matrix.from(1, n, CoefficientSupplier.from(new Double[][]{{-0.13641258, -0.13226477, -0.11341983, -0.07905581, -0.04206108, -0.01768819
        , -0.00979344, -0.01241724, -0.01827551, -0.01885776, -0.00357864, 0.03055858
        , 0.07122674, 0.09996133, 0.10678794, 0.0927515, 0.06394207, 0.02806172
        , -0.00650283, -0.0329772, -0.04870975, -0.05591393, -0.06084836, -0.07112043
        , -0.09102107, -0.11822757, -0.1458072, -0.16767261, -0.18143243, -0.18702424
        , -0.18523038, -0.1783268, -0.1704353, -0.16566351, -0.16577884, -0.16920872
        , -0.17089564, -0.16390234, -0.14322408, -0.10387483, -0.02757768, 0.12429479
        , 0.38927256, 0.76710318, 1.20416533, 1.61263552, 1.90662918, 2.02927502
        , 1.95887483, 1.70429506, 1.30420649, 0.82873539, 0.37081521, 0.01846759
        , -0.18297819, -0.24912346, -0.23688403, -0.20531681, -0.18698051, -0.18502686
        , -0.19132366, -0.20268362, -0.21911435, -0.23574551, -0.24458687, -0.24273725
        , -0.2357971, -0.23316033, -0.23964559, -0.25089007, -0.25715264, -0.25267993
        , -0.24153984, -0.23259732, -0.22899466, -0.22615143, -0.2197977, -0.21079408
        , -0.20155194, -0.19249434, -0.1832539, -0.1742999, -0.1656533, -0.15481677
        , -0.13828439, -0.11684283, -0.09692626, -0.08316581, -0.07195401, -0.05622992
        , -0.03406039, -0.00918382, 0.01449033, 0.03647174, 0.05926605, 0.08555947
        , 0.11542531, 0.14630465, 0.17564972, 0.2029149, 0.22872453, 0.25266199
        , 0.27289231, 0.2882683, 0.29948681, 0.3072115, 0.3102532, 0.30669556
        , 0.29606422, 0.27946405, 0.25766283, 0.22975818, 0.19448151, 0.15268855
        , 0.10781889, 0.06384564, 0.02310768, -0.01409059, -0.04820841, -0.07902614
        , -0.10555369, -0.12713909, -0.14411799, -0.15726653, -0.16726245, -0.17495874
        , -0.1813658, -0.18678138}}));

// Add noise to signal
S = S.dotApply(t -> t + RNG.nextGaussian() * 0.1);

S = S.apply(MatrixOperations.TRANSPOSE);

// DCT basis
Matrix DCT = Matrix.from(n, n, (i, j) -> Math.cos(j * Math.PI * (2 * i + 1) / (2 * n)));
DCT = DCT.apply(MatrixOperations.NORM_COLS);

// Signal representation in DCT basis
Matrix X = DCT.apply(MatrixOperations.TRANSPOSE).apply(S, MatrixOperations.MUL);

// Hard thresholding
X = X.apply(0.5D, MatrixOperations.THRESHOLD);

// Synthesis
Matrix Shat = DCT.apply(X, MatrixOperations.MUL);

// Additional code to export results on a plot
XYSeries baseECG = new XYSeries("Base ECG Signal");
for(int i = 0; i < n; i++)
    baseECG.add(i, S.getValues()[i][0]);

XYSeries synthesis = new XYSeries("Synthesis Signal");
for(int i = 0; i < n; i++)
    synthesis.add(i, Shat.getValues()[i][0]);

XYSeriesCollection dataset = new XYSeriesCollection();
dataset.addSeries(baseECG);
dataset.addSeries(synthesis);

JFreeChart xyLineChart = ChartFactory.createXYLineChart(
        "Test XY Line",
        "Time",
        "Amplitude",
        dataset,
        PlotOrientation.VERTICAL,
        true,
        true,
        true
);

BufferedImage img = xyLineChart.createBufferedImage(1920, 1080);
File outputfile = new File("image.png");
try {
    ImageIO.write(img, "png", outputfile);
    System.out.println(outputfile.getAbsolutePath());
} catch (IOException e) {
    throw new RuntimeException(e);
}
```


