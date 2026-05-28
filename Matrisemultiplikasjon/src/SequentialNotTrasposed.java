import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SequentialNotTrasposed {

    public static void main(String[] args) {

        //getting the seed and n from the command line.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Seed: ");
        int seed = scanner.nextInt();
        System.out.println("n: ");
        int n = scanner.nextInt();

        List<Double> timeList = new ArrayList<>();


        for (int i = 0; i < 7; i++) {
            final double startTime = System.currentTimeMillis();
            // creating the matrices which will get multiplied.
            double[][] matrixA = Oblig2Precode.generateMatrixA(seed, n);
            double[][] matrixB = Oblig2Precode.generateMatrixB(seed, n);
            double[][] correctMatrix;

            // The result matrix
            correctMatrix = new double[n][n];



            //Multiplying the matrixes.
            MatrixMultiAndTranspose.matrixMultiplication(matrixA, matrixB, correctMatrix, n);

            final double endTime = System.currentTimeMillis();
            double timeUsages = endTime - startTime;
            timeList.add(timeUsages);

            // Save the result
            Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED, correctMatrix);
        }

        Collections.sort(timeList);

        double medianTime = timeList.get(timeList.size() / 2);
        System.out.println("median time usages: " + medianTime + " miliseconds");
    }
}