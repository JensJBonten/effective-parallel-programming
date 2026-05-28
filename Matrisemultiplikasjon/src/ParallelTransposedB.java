import java.util.concurrent.CyclicBarrier;

public class ParallelTransposedB {

    /**
     * Multiplies two matrices in parallel when matrix B has already been transposed.
     *
     * @param matrixA normal matrix A.
     * @param matrixBTransposed matrix B transposed.
     * @param matrixC where the answer is stored.
     * @param n size of the matrices.
     * @param antallTraader amount of threads used.
     * @return matrixC which is the product of matrixA and matrixB.
     */
    public static double[][] multiply(double[][] matrixA, double[][] matrixBTransposed, double[][] matrixC, int n,
                                      int antallTraader) {
        antallTraader = Math.max(1, Math.min(antallTraader, n));

        CyclicBarrier barriere = new CyclicBarrier(antallTraader + 1);
        int antallElementerPerTraad = Math.max(1, n / antallTraader);

        for (int i = 0; i < antallTraader; i++) {
            int venstre = i * antallElementerPerTraad;
            int hoyere = (i == antallTraader - 1) ? n : (i + 1) * antallElementerPerTraad;

            new Thread(new Arbeid(matrixA, matrixBTransposed, matrixC, venstre, hoyere, n, barriere)).start();
        }

        try {
            barriere.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matrixC;
    }

    public static void matrixMultiplixationParallel(double[][] matrixA, double[][] matrixBTransposed,
                                                    double[][] matrixC, int start, int end, int n) {
        for (int i = start; i < end; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;

                for (int k = 0; k < n; k++) {
                    sum += matrixA[i][k] * matrixBTransposed[j][k];
                }

                matrixC[i][j] = sum;
            }
        }
    }


    private static class Arbeid implements Runnable {
        private final double[][] matrixA;
        private final double[][] matrixB;
        private final double[][] matrixC;
        private final int start;
        private final int end;
        private final int sizeOfMatrix;
        private final CyclicBarrier barrier;

        public Arbeid(double[][] matrixA, double[][] matrixB, double[][] matrixC, int start, int end,
                      int sizeOfMatrix, CyclicBarrier barrier) {
            this.matrixA = matrixA;
            this.matrixB = matrixB;
            this.matrixC = matrixC;
            this.start = start;
            this.end = end;
            this.sizeOfMatrix = sizeOfMatrix;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                matrixMultiplixationParallel(matrixA, matrixB, matrixC, start, end, sizeOfMatrix);
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}