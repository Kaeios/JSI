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

