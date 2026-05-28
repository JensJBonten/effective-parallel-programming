import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TransposeB {
    public static void main(String[] args) throws IOException {

        //getting seed and n from command line.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seed: ");
        int seed = scanner.nextInt();
        System.out.println("n: ");
        int n = scanner.nextInt();

        List<Double> timeList = new ArrayList<>();

        //Having multiple runs of the program  to exclude outliers.
        for (int index = 0; index < 7; index++) {
            //the matrix which will contain the multipled matrices.
            double[][] matrixA = Oblig2Precode.generateMatrixA(seed, n);
            double[][] matrixB = Oblig2Precode.generateMatrixB(seed, n);
            double[][] correctnesCheck = new double[n][n];

            double[][] bTransposed;
            double[][] matrixC = new double[n][n];

            //final double startTime = System.currentTimeMillis();
            //transposing b.
            bTransposed = MatrixMultiAndTranspose.transposeMatrix(matrixB);

            final double startTime = System.currentTimeMillis();
            //matrix multiplication
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        matrixC[i][j] += matrixA[i][k] * bTransposed[j][k];
                    }
                }
            }

            //adding all times to a list.
            final double endTime = System.currentTimeMillis();
            double timeUsages = endTime - startTime;
            timeList.add(timeUsages);


            //Checking if the multiplication has been implemented correctly by comparing Matrix C with SeqNotTransposed.
            if (!CorrectnesCheck.checkWithLoop(MatrixMultiAndTranspose.matrixMultiplication(matrixA, matrixB, correctnesCheck, n)
                    , matrixC, n)) {
                return;
            } else {
                Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_B_TRANSPOSED, matrixC);
            }
        }

        Collections.sort(timeList);
        double medianTime = timeList.get(timeList.size() / 2);
        System.out.println("median time usages: " + medianTime + " miliseconds");
    }
}