import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int NUMBER_OF_RUNS = 7;

    public static void main(String[] args) {
        int seed;
        int n;
        int antallTraader;

        try {
            // FORBEDRET: støtter både IntelliJ program arguments og vanlig scanner-input.
            seed = getSeed(args);
            n = getN(args);
            antallTraader = getAntallTraader(args, n);
        } catch (InputMismatchException e) {
            System.out.println("Wrong input used. Seed, n and threads must be integers.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Wrong input used. Seed, n and threads must be integers.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        // FORBEDRET: matrisene genereres én gang og brukes på alle variantene.
        double[][] matrixA = Oblig2Precode.generateMatrixA(seed, n);
        double[][] matrixB = Oblig2Precode.generateMatrixB(seed, n);

        double[][] matrixATransposed = MatrixMultiAndTranspose.transposeMatrix(matrixA);
        double[][] matrixBTransposed = MatrixMultiAndTranspose.transposeMatrix(matrixB);

        BenchmarkResult seqNotTransposed = runSeqNotTransposed(matrixA, matrixB, seed, n);
        BenchmarkResult seqATransposed = runSeqATransposed(matrixATransposed, matrixB, seed, n, seqNotTransposed.resultMatrix);
        BenchmarkResult seqBTransposed = runSeqBTransposed(matrixA, matrixBTransposed, seed, n, seqNotTransposed.resultMatrix);
        BenchmarkResult paraNotTransposed = runParaNotTransposed(matrixA, matrixB, seed, n, antallTraader, seqNotTransposed.resultMatrix);
        BenchmarkResult paraATransposed = runParaATransposed(matrixATransposed, matrixB, seed, n, antallTraader, seqNotTransposed.resultMatrix);
        BenchmarkResult paraBTransposed = runParaBTransposed(matrixA, matrixBTransposed, seed, n, antallTraader, seqNotTransposed.resultMatrix);

        System.out.println("Seed: " + seed);
        System.out.println("Matrix size: " + n + "x" + n);
        System.out.println("Threads: " + antallTraader);
        System.out.println("Runs: " + NUMBER_OF_RUNS);
        System.out.println();

        printResult(seqNotTransposed, seqNotTransposed.medianTime);
        printResult(seqATransposed, seqNotTransposed.medianTime);
        printResult(seqBTransposed, seqNotTransposed.medianTime);
        printResult(paraNotTransposed, seqNotTransposed.medianTime);
        printResult(paraATransposed, seqNotTransposed.medianTime);
        printResult(paraBTransposed, seqNotTransposed.medianTime);

        System.out.println();
        System.out.println("All correctness checks passed: true");
        System.out.println("Result files are written when n <= 100, using Oblig2Precode.");
    }

    private static BenchmarkResult runSeqNotTransposed(double[][] matrixA, double[][] matrixB, int seed, int n) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            MatrixMultiAndTranspose.matrixMultiplication(matrixA, matrixB, matrixC, n);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_NOT_TRANSPOSED, resultMatrix);
        return new BenchmarkResult("SEQ_NOT_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static BenchmarkResult runSeqATransposed(double[][] matrixATransposed, double[][] matrixB, int seed, int n,
                                                     double[][] correctSolution) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            MatrixMultiAndTranspose.matrixMultiplicationTransposedA(matrixATransposed, matrixB, matrixC, n);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        checkCorrectness(correctSolution, resultMatrix, n, "SEQ_A_TRANSPOSED");
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_A_TRANSPOSED, resultMatrix);

        return new BenchmarkResult("SEQ_A_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static BenchmarkResult runSeqBTransposed(double[][] matrixA, double[][] matrixBTransposed, int seed, int n,
                                                     double[][] correctSolution) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            MatrixMultiAndTranspose.matrixMultiplicationTransposedB(matrixA, matrixBTransposed, matrixC, n);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        checkCorrectness(correctSolution, resultMatrix, n, "SEQ_B_TRANSPOSED");
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.SEQ_B_TRANSPOSED, resultMatrix);

        return new BenchmarkResult("SEQ_B_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static BenchmarkResult runParaNotTransposed(double[][] matrixA, double[][] matrixB, int seed, int n,
                                                        int antallTraader, double[][] correctSolution) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            ParallelNotTransposed.multiply(matrixA, matrixB, matrixC, n, antallTraader);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        checkCorrectness(correctSolution, resultMatrix, n, "PARA_NOT_TRANSPOSED");
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_NOT_TRANSPOSED, resultMatrix);

        return new BenchmarkResult("PARA_NOT_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static BenchmarkResult runParaATransposed(double[][] matrixATransposed, double[][] matrixB, int seed, int n,
                                                      int antallTraader, double[][] correctSolution) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            ParallelTransposedA.multiply(matrixATransposed, matrixB, matrixC, n, antallTraader);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        checkCorrectness(correctSolution, resultMatrix, n, "PARA_A_TRANSPOSED");
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_A_TRANSPOSED, resultMatrix);

        return new BenchmarkResult("PARA_A_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static BenchmarkResult runParaBTransposed(double[][] matrixA, double[][] matrixBTransposed, int seed, int n,
                                                      int antallTraader, double[][] correctSolution) {
        List<Double> timeList = new ArrayList<>();
        double[][] resultMatrix = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            double[][] matrixC = new double[n][n];

            long startTime = System.nanoTime();
            ParallelTransposedB.multiply(matrixA, matrixBTransposed, matrixC, n, antallTraader);
            long endTime = System.nanoTime();

            timeList.add(toMilliseconds(startTime, endTime));
            resultMatrix = matrixC;
        }

        checkCorrectness(correctSolution, resultMatrix, n, "PARA_B_TRANSPOSED");
        Oblig2Precode.saveResult(seed, Oblig2Precode.Mode.PARA_B_TRANSPOSED, resultMatrix);

        return new BenchmarkResult("PARA_B_TRANSPOSED", median(timeList), resultMatrix);
    }

    private static int getSeed(String[] args) {
        if (args.length >= 1) {
            return Integer.parseInt(args[0]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Seed: ");
        return scanner.nextInt();
    }

    private static int getN(String[] args) {
        int n;

        if (args.length >= 2) {
            n = Integer.parseInt(args[1]);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("n: ");
            n = scanner.nextInt();
        }

        validateN(n);
        return n;
    }

    private static int getAntallTraader(String[] args, int n) {
        int antallTraader;

        if (args.length >= 3) {
            antallTraader = Integer.parseInt(args[2]);

            if (antallTraader <= 0) {
                antallTraader = Runtime.getRuntime().availableProcessors();
            }
        } else {
            antallTraader = Runtime.getRuntime().availableProcessors();
        }

        return Math.min(antallTraader, n);
    }

    private static void validateN(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be larger than 0.");
        }
    }

    private static void checkCorrectness(double[][] correctSolution, double[][] test, int n, String name) {
        if (!CorrectnesCheck.checkWithLoop(correctSolution, test, n)) {
            throw new IllegalStateException(name + " did not match the sequential solution.");
        }
    }

    private static double median(List<Double> values) {
        Collections.sort(values);
        return values.get(values.size() / 2);
    }

    private static double toMilliseconds(long startTime, long endTime) {
        return (endTime - startTime) / 1_000_000.0;
    }

    private static void printResult(BenchmarkResult result, double baseline) {
        double speedup = baseline / result.medianTime;

        System.out.printf("%-22s median: %10.3f ms. Speedup vs sequential: %.3f%n",
                result.name, result.medianTime, speedup);
    }

    private static class BenchmarkResult {
        private final String name;
        private final double medianTime;
        private final double[][] resultMatrix;

        public BenchmarkResult(String name, double medianTime, double[][] resultMatrix) {
            this.name = name;
            this.medianTime = medianTime;
            this.resultMatrix = resultMatrix;
        }
    }
}