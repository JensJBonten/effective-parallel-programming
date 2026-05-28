public class MatrixMultiAndTranspose {

    /**
     * A method that will transpose a desired matrix.
     *
     * @param matrixToTranspose is the Matrix that will get transposed.
     * @return a transposed matrix.
     */
    static double[][] transposeMatrix(double[][] matrixToTranspose) {
        int n = matrixToTranspose.length;
        double[][] transposedMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transposedMatrix[i][j] = matrixToTranspose[j][i];
            }
        }

        return transposedMatrix;
    }

    /**
     * A method that will multiply two matrices with each other.
     *
     * @param matrixA which you want to multiply with matrixB.
     * @param matrixB which you want to multiply with matrixA.
     * @param matrixC where the answer is stored.
     * @param n       the size of the matrices.
     * @return matrixC which is the product of matrixA and matrixB.
     */
    public static double[][] matrixMultiplication(double[][] matrixA, double[][] matrixB, double[][] matrixC, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;

                for (int k = 0; k < n; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }

                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    /**
     * Multiplies A and B when A has already been transposed.
     *
     * @param matrixATransposed matrix A transposed.
     * @param matrixB normal matrix B.
     * @param matrixC where the answer is stored.
     * @param n size of the matrices.
     * @return matrixC which is the product of matrixA and matrixB.
     */
    public static double[][] matrixMultiplicationTransposedA(double[][] matrixATransposed, double[][] matrixB,
                                                             double[][] matrixC, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;

                for (int k = 0; k < n; k++) {
                    sum += matrixATransposed[k][i] * matrixB[k][j];
                }

                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }

    /**
     * Multiplies A and B when B has already been transposed.
     *
     * @param matrixA normal matrix A.
     * @param matrixBTransposed matrix B transposed.
     * @param matrixC where the answer is stored.
     * @param n size of the matrices.
     * @return matrixC which is the product of matrixA and matrixB.
     */
    public static double[][] matrixMultiplicationTransposedB(double[][] matrixA, double[][] matrixBTransposed,
                                                             double[][] matrixC, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;

                for (int k = 0; k < n; k++) {
                    sum += matrixA[i][k] * matrixBTransposed[j][k];
                }

                matrixC[i][j] = sum;
            }
        }

        return matrixC;
    }
}