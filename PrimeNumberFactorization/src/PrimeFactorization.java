import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeFactorization {
    /**
     * This function calculates the prime factors of a given number sequentially.
     *
     * @param n the number to be factored.
     * @param primes array of primes.
     * @return A list with all the factored numbers.
     */
    public static List<Long> primeCalculation(long n, int[] primes) {
        List<Long> primeFactors = new ArrayList<>();
        long rest = n;

        for (int prime : primes) {
            if ((long) prime * prime > rest) {
                break;
            }

            while (rest % prime == 0) {
                primeFactors.add((long) prime);
                rest /= prime;
            }
        }

        if (rest > 1) {
            primeFactors.add(rest);
        }

        return primeFactors;
    }

    /**
     * This method factorizes a given number over multiple threads.
     * Each thread gets an interval with prime numbers which it uses to factor a part of the number.
     *
     * @param number to be factorized.
     * @param threads amount of threads.
     * @param primes array of primes.
     * @return list of factors for a given number.
     */
    public static List<Long> paraCalculation(long number, int threads, int[] primes) {
        ParallelFactorizer factorizer = new ParallelFactorizer(threads, primes);
        List<Long> factors = factorizer.factorize(number);
        factorizer.shutdown();
        return factors;
    }

    /**
     *
     * Reusable parallel factorizer. This avoids creating new threads for every single number,
     * while each number is still split across multiple worker tasks.
     */
    public static class ParallelFactorizer {
        private final int threads;
        private final int[] primes;
        private final ExecutorService executorService;

        public ParallelFactorizer(int threads, int[] primes) {
            this.threads = Math.max(1, threads);
            this.primes = primes;
            this.executorService = Executors.newFixedThreadPool(this.threads);
        }

        public List<Long> factorize(long number) {
            List<Callable<List<Long>>> tasks = new ArrayList<>();
            int interval = Math.max(1, primes.length / threads);

            for (int i = 0; i < threads; i++) {
                int start = i * interval;
                int end = (i == threads - 1) ? primes.length : Math.min(primes.length, (i + 1) * interval);

                if (start < primes.length) {
                    tasks.add(new Worker(start, end, primes, number));
                }
            }

            List<Long> primeFactors = new ArrayList<>();
            long dividedNumber = number;

            try {
                List<Future<List<Long>>> results = executorService.invokeAll(tasks);

                for (Future<List<Long>> result : results) {
                    primeFactors.addAll(result.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Collections.sort(primeFactors);

            for (long factor : primeFactors) {
                dividedNumber /= factor;
            }


            if (dividedNumber > 1) {
                primeFactors.add(dividedNumber);
            }

            Collections.sort(primeFactors);
            return primeFactors;
        }

        public void shutdown() {
            executorService.shutdown();
        }
    }

    static class Worker implements Callable<List<Long>> {
        private final int start;
        private final int end;
        private final int[] primes;
        private final long n;

        public Worker(int start, int end, int[] primes, long n) {
            this.start = start;
            this.end = end;
            this.primes = primes;
            this.n = n;
        }

        @Override
        public List<Long> call() {
            List<Long> factors = new ArrayList<>();

            for (int i = start; i < end; i++) {
                long prime = primes[i];

                if (prime * prime > n) {
                    break;
                }

                long rest = n;

                while (rest % prime == 0) {
                    factors.add(prime);
                    rest /= prime;
                }
            }

            return factors;
        }
    }
}