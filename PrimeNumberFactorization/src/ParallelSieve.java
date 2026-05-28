import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ParallelSieve {
    /**
     * Declaring all the global variables
     */
    private final int n;
    private final int root;
    private final int numWorkers;
    private final int[] basePrimes;
    private final List<Integer>[] workerPrimeLists;

    /**
     * Constructor that initializes the global variables.
     *
     * @param n Prime numbers up until and including n is found.
     * @param numWorkers workers that run an interval of the program.
     */
    public ParallelSieve(int n, int numWorkers) {
        this.n = n;
        this.root = (int) Math.sqrt(n);
        this.numWorkers = Math.max(1, numWorkers);

        this.basePrimes = new SieveOfEratosthenes(root).getPrimes();
        this.workerPrimeLists = new List[this.numWorkers];
    }

    /**
     * Performs the parallel sieve and collects the primes produced by the sieve.
     *
     * @return An array containing all the primes up to and including n.
     */
    public int[] getPrimes() {
        if (n <= 1) {
            return new int[0];
        }

        if (n == 2) {
            return new int[]{2};
        }

        return collectPrimes();
    }

    /**
     * Collects all the primes between 2 and n.
     * Each worker gets one interval and performs a segmented sieve on that interval.
     *
     * @return an array containing all the primes up to and including n.
     */
    private int[] collectPrimes() {
        CyclicBarrier barrier = new CyclicBarrier(numWorkers + 1);
        Thread[] threads = new Thread[numWorkers];

        int firstOdd = 3;
        int oddCount = ((n - firstOdd) / 2) + 1;
        int interval = Math.max(1, oddCount / numWorkers);

        for (int i = 0; i < numWorkers; i++) {
            int startOddIndex = i * interval;
            int endOddIndex = (i == numWorkers - 1) ? oddCount - 1 : ((i + 1) * interval) - 1;

            int start = firstOdd + (startOddIndex * 2);
            int end = firstOdd + (endOddIndex * 2);

            Worker worker = new Worker(i, start, end, barrier);
            threads[i] = new Thread(worker);
            threads[i].start();
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        return mergePrimes();
    }

    /**
     * FORBEDRET: slår sammen resultatene fra workerne i riktig rekkefølge.
     */
    private int[] mergePrimes() {
        int amountOfPrimes = 1;

        for (List<Integer> workerPrimeList : workerPrimeLists) {
            amountOfPrimes += workerPrimeList.size();
        }

        int[] primes = new int[amountOfPrimes];
        primes[0] = 2;
        int index = 1;

        for (List<Integer> workerPrimeList : workerPrimeLists) {
            for (int prime : workerPrimeList) {
                primes[index++] = prime;
            }
        }

        return primes;
    }

    /**
     * The worker class is set to find primes in a given interval.
     * The run method performs a segmented sieve between start and end.
     */
    private class Worker implements Runnable {
        private final int id;
        private final int start;
        private final int end;
        private final CyclicBarrier barrier;
        private final List<Integer> primes;

        public Worker(int id, int start, int end, CyclicBarrier barrier) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.barrier = barrier;
            primes = new ArrayList<>();
        }

        @Override
        public void run() {
            if (start <= end) {
                segmentedSieve();
            }

            workerPrimeLists[id] = primes;

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void segmentedSieve() {
            boolean[] compositeNumbers = new boolean[((end - start) / 2) + 1];

            for (int prime : basePrimes) {
                if (prime == 2) {
                    continue;
                }

                long primeSquared = (long) prime * prime;

                if (primeSquared > end) {
                    break;
                }

                long firstMultiple = Math.max(primeSquared, ((start + prime - 1L) / prime) * prime);

                if (firstMultiple % 2 == 0) {
                    firstMultiple += prime;
                }

                for (long j = firstMultiple; j <= end; j += prime * 2L) {
                    int index = (int) ((j - start) / 2);
                    compositeNumbers[index] = true;
                }
            }

            for (int i = 0; i < compositeNumbers.length; i++) {
                if (!compositeNumbers[i]) {
                    primes.add(start + (i * 2));
                }
            }
        }
    }

    public static void main(String[] args) {
        ParallelSieve soe = new ParallelSieve(20000, 8);
        System.out.println(Arrays.toString(soe.getPrimes()));
    }
}