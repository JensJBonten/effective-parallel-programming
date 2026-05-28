import java.util.Scanner;

/**
 * IN3030 - Oblig 2 Pre-code usage example
 *
 * @author Magnus Espeland <magnuesp@ifi.uio.no>
 * @date 2019.02.14
 */

public class Example {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Seed: ");
        int seed = scanner.nextInt();
        System.out.println("n: ");
        int n = scanner.nextInt();



        // Get the matrices
        double[][] a = Oblig2Precode.generateMatrixA(seed, n);
        double[][] b = Oblig2Precode.generateMatrixB(seed, n);

        // The result matrix
        double[][] c = new double[n][n];


        final double startTime = System.currentTimeMillis();
        //Multiplying the matrixes.
        MatrixMultiAndTranspose.matrixMultiplication(a,b,c,n);
        final double endTime = System.currentTimeMillis();

        System.out.println("Tiden brukt på vanlig matrix multiplikasjon er : "
                + ((endTime-startTime)) + " millisekunder" + "\n");


        // Save the result
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED, c);
    }

}