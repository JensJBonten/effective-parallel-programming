import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.PrintWriter;

public class Main {
    // FORBEDRET: samlet antall kjøringer i én konstant.
    private static final int NUMBER_OF_RUNS = 7;

    public static void main(String[] args) {
        int n;
        int seed;
        boolean drawGraph;

        try {
            n = getN(args);
            seed = getSeed(args);
            drawGraph = shouldDrawGraph(args);
        } catch (InputMismatchException e) {
            System.out.println("Wrong input used, n and seed must be integers.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        NPunkter17 nPunkter17 = new NPunkter17(n, seed);

        int[] x = new int[n];
        int[] y = new int[n];

        nPunkter17.fyllArrayer(x, y);
        IntList points = nPunkter17.lagIntList();

        List<Double> medianListSeq = new ArrayList<>();
        List<Double> medianListPara = new ArrayList<>();
        IntList sequentialHull = null;
        IntList parallelHull = null;
        ConvexHull convexHullSeq = null;
        ConvexHull convexHullPara = null;

        // Kjører både sekvensiell og parallell løsning 7 ganger og bruker median.
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            convexHullSeq = new ConvexHull(points, x, y);
            convexHullPara = new ConvexHull(points, x, y);

            long startTimeSequential = System.nanoTime();
            convexHullSeq.findConvexHullSeq();
            long endTimeSequential = System.nanoTime();

            long startTimePara = System.nanoTime();
            convexHullPara.findConvexHullParallel();
            long endTimePara = System.nanoTime();

            medianListSeq.add(toMilliseconds(startTimeSequential, endTimeSequential));
            medianListPara.add(toMilliseconds(startTimePara, endTimePara));

            sequentialHull = convexHullSeq.getHullIndices();
            parallelHull = convexHullPara.getHullIndices();
        }

        // Sjekker at sekvensiell og parallell løsning faktisk gir samme hull.
        if (!sameHull(sequentialHull, parallelHull)) {
            System.out.println("The sequential and parallel hull did not match.");
            return;
        }

        Collections.sort(medianListSeq);
        Collections.sort(medianListPara);

        double medianTimeUsageSeq = medianListSeq.get(medianListSeq.size() / 2);
        double medianTimeUsagePara = medianListPara.get(medianListPara.size() / 2);

        System.out.println("Number of points: " + n);
        System.out.println("Seed: " + seed);
        System.out.println("Convex hull points: " + sequentialHull.size());
        System.out.printf("Median time usage for the sequential computation: %.3f ms.%n", medianTimeUsageSeq);
        System.out.printf("Median time usage for the parallel computation: %.3f ms.%n", medianTimeUsagePara);
        System.out.printf("Speedup gained: %.3f%n", medianTimeUsageSeq / medianTimeUsagePara);

        if (n < 10000) {
            // Skriver hullpunkter uten å måtte åpne Swing-vindu.
            writeHullPoints(convexHullSeq);

            // Tegner valgfritt med --draw.
            if (drawGraph) {
                Oblig4Precode preCodeSeq = new Oblig4Precode(convexHullSeq, convexHullSeq.getHullIndices());
                preCodeSeq.drawGraph();
            }
        }
    }

    private static int getN(String[] args) {
        if (args.length >= 1) {
            return Integer.parseInt(args[0]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter n: ");
        return scanner.nextInt();
    }

    private static int getSeed(String[] args) {
        if (args.length >= 2) {
            return Integer.parseInt(args[1]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the seed number of the program: ");
        return scanner.nextInt();
    }

    private static boolean shouldDrawGraph(String[] args) {
        for (String arg : args) {
            if (arg.equals("--draw")) {
                return true;
            }
        }

        return false;
    }

    private static double toMilliseconds(long startTime, long endTime) {
        return (endTime - startTime) / 1_000_000.0;
    }

    private static void writeHullPoints(ConvexHull convexHull) {
        String filename = "CONVEX-HULL-POINTS_" + convexHull.n + ".txt";
        IntList convexHullPoints = convexHull.getHullIndices();

        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.printf("Found %d number of convex hull points in a graph with n = %d:%n", convexHullPoints.size(), convexHull.n);
            writer.println("______________________________________________________");
            writer.println();

            for (int i = 0; i < convexHullPoints.size(); i++) {
                int point = convexHullPoints.get(i);
                writer.print("(" + convexHull.x[point] + "," + convexHull.y[point] + ")");
            }

            // FORBEDRET: tydelig beskjed om at filen faktisk ble laget.
            System.out.println("Hull points written to: " + filename);

        } catch (Exception e) {
            System.out.printf("Got exception when trying to write file %s : %s%n", filename, e.getMessage());
        }
    }

    private static boolean sameHull(IntList hull1, IntList hull2) {
        if (hull1.size() != hull2.size()) {
            return false;
        }

        for (int i = 0; i < hull1.size(); i++) {
            if (hull1.get(i) != hull2.get(i)) {
                return false;
            }
        }

        return true;
    }
}