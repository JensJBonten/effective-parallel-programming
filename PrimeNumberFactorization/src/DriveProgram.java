import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DriveProgram {
    private static final int NUMBER_OF_RUNS = 7;

    public static void main(String[] args) {
        int n;
        int k;

        try {

            n = getN(args);
            k = getK(args);
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Wrong input used, n and k must be integers.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (n <= 16) {
            System.out.println("You have to enter a number larger than 16 to run the program.");
            return;
        }

        if (k <= 0) {
            k = Runtime.getRuntime().availableProcessors();
        }

        System.out.println("N: " + n);
        System.out.println("Threads: " + k);
        System.out.println("Runs: " + NUMBER_OF_RUNS);
        System.out.println();

        SequentialSoe sequentialSoe = new SequentialSoe(n, NUMBER_OF_RUNS);
        ParallelSoe parallelSoe = new ParallelSoe(n, k, NUMBER_OF_RUNS);

        sequentialSoe.run();
        parallelSoe.run();

        //Sjekker om paralell og sekvensiell har like primtall, samt om de faktoriserer likt under.
        if (!Arrays.equals(sequentialSoe.getPrimes(), parallelSoe.getPrimes())) {
            System.out.println("The sequential and parallel sieve did not generate the same prime numbers.");
            return;
        }


        if (!sequentialSoe.getFactorResults().equals(parallelSoe.getFactorResults())) {
            System.out.println("The sequential and parallel factorization did not generate the same factors.");
            return;
        }

        printResults(sequentialSoe, parallelSoe);

        sequentialSoe.writeFactors();
        parallelSoe.writeFactors();

        System.out.println();
        System.out.println("Prime numbers matched: true");
        System.out.println("Factorizations matched: true");
        System.out.println("Factor files written: Factors_" + n + "_sequential.txt and Factors_" + n + "_parallel.txt");
    }

    private static int getN(String[] args) {
        if (args.length >= 1) {
            return Integer.parseInt(args[0]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter n: ");
        return scanner.nextInt();
    }

    private static int getK(String[] args) {
        if (args.length >= 2) {
            return Integer.parseInt(args[1]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter k: ");
        return scanner.nextInt();
    }

    private static void printResults(SequentialSoe sequentialSoe, ParallelSoe parallelSoe) {
        double sieveSpeedup = sequentialSoe.getMedianSieveTime() / parallelSoe.getMedianSieveTime();
        double factorSpeedup = sequentialSoe.getMedianFactorizationTime() / parallelSoe.getMedianFactorizationTime();

        System.out.println("Prime count: " + sequentialSoe.getPrimes().length);
        System.out.printf("Sequential sieve median: %.3f ms.%n", sequentialSoe.getMedianSieveTime());
        System.out.printf("Parallel sieve median: %.3f ms.%n", parallelSoe.getMedianSieveTime());
        System.out.printf("Sieve speedup: %.3f%n", sieveSpeedup);
        System.out.println();
        System.out.printf("Sequential factorization median: %.3f ms.%n", sequentialSoe.getMedianFactorizationTime());
        System.out.printf("Parallel factorization median: %.3f ms.%n", parallelSoe.getMedianFactorizationTime());
        System.out.printf("Factorization speedup: %.3f%n", factorSpeedup);
    }
}