# Effective Parallel Programming

This repository contains three Java projects focused on algorithmic performance and parallel programming. The projects originate from university coursework and have been cleaned up for portfolio-style documentation.

Only the following projects are documented here:

- `ConvexHull`
- `Matrisemultiplikasjon`
- `PrimeNumberFactorization`

`Oblig1` is intentionally not part of this repository documentation.

## Repository Structure

| Path | Purpose |
| --- | --- |
| `ConvexHull/src` | Sequential and parallel convex hull implementation, point generation, drawing/output support, and project-local README. |
| `Matrisemultiplikasjon/src` | Sequential and parallel matrix multiplication variants, correctness checking, course precode, and legacy standalone entry points. |
| `Matrisemultiplikasjon/files` | Existing matrix result files written by `Oblig2Precode` for small matrices. These are correctness/output artifacts, not benchmark timings. |
| `PrimeNumberFactorization/src` | Sequential and parallel prime generation and factorization code, course precode, and project-local README. |
| `docs/data` | Existing benchmark CSV files and raw run logs used by the benchmark report. |
| `docs/BENCHMARK_REPORT.md` | Norwegian benchmark report with method, limitations, tables, speedup, efficiency, and discussion. |
| `docs/CODE_ORIGIN.md` | Classification of student code, provided course code, and files with unclear origin. |

## Technologies

- Java
- Threads, `CyclicBarrier`, `ExecutorService`, `ForkJoinPool`, and `RecursiveTask`
- Sequential and parallel algorithm implementations
- Median-based runtime measurement using `System.nanoTime()` in the active main programs
- Correctness checks comparing parallel output against sequential baselines

## Projects

### ConvexHull

`ConvexHull` generates a deterministic set of points from `NPunkter17`, computes the convex hull sequentially and in parallel, compares the two hulls, and reports median runtime over seven internal runs.

Relevant files:

| File | Role |
| --- | --- |
| `ConvexHull/src/Main.java` | Active entry point. Parses `n`, `seed`, and optional `--draw`; runs both implementations seven times; compares hulls; reports median times and speedup. |
| `ConvexHull/src/ConvexHull.java` | Active sequential and parallel convex hull implementation. The parallel version uses `ForkJoinPool` and recursive `HullTask` tasks. |
| `ConvexHull/src/IntList.java` | Integer list helper used for point and hull indices. |
| `ConvexHull/src/NPunkter17.java` | Point generator used by the project. |
| `ConvexHull/src/Oblig4Precode.java` | Provided drawing/output support. |

### Matrisemultiplikasjon

`Matrisemultiplikasjon` multiplies two `n x n` matrices using six variants: three sequential and three parallel. The active `Main` generates matrices once, creates transposed versions once, runs each variant seven times, checks correctness against the sequential non-transposed baseline, and prints median times.

Relevant active files:

| File | Role |
| --- | --- |
| `Matrisemultiplikasjon/src/Main.java` | Active entry point for all six benchmarked variants. |
| `Matrisemultiplikasjon/src/MatrixMultiAndTranspose.java` | Shared sequential multiplication and transpose methods. |
| `Matrisemultiplikasjon/src/ParallelNotTransposed.java` | Parallel row-partitioned multiplication without transposition. |
| `Matrisemultiplikasjon/src/ParallelTransposedA.java` | Parallel multiplication where A is pre-transposed. |
| `Matrisemultiplikasjon/src/ParallelTransposedB.java` | Parallel multiplication where B is pre-transposed. |
| `Matrisemultiplikasjon/src/CorrectnesCheck.java` | Floating-point tolerance check against the baseline result. |
| `Matrisemultiplikasjon/src/Oblig2Precode.java` | Provided matrix generation and result-file writer. |

Legacy or alternative standalone entry points are still present but are not used by the active `Main`: `Example.java`, `SequentialNotTrasposed.java`, `TransposeA.java`, and `TransposeB.java`.

### PrimeNumberFactorization

`PrimeNumberFactorization` generates primes up to `n`, factorizes the 100 largest numbers below `n*n`, and compares sequential and parallel results.

Relevant files:

| File | Role |
| --- | --- |
| `PrimeNumberFactorization/src/DriveProgram.java` | Active entry point. Runs sequential and parallel sieve/factorization seven times, checks equality, prints timings and speedups, and writes factor files. |
| `PrimeNumberFactorization/src/SequentialSoe.java` | Sequential wrapper for sieve and factorization timing. |
| `PrimeNumberFactorization/src/ParallelSoe.java` | Parallel wrapper for sieve and factorization timing. |
| `PrimeNumberFactorization/src/SieveOfEratosthenes.java` | Sequential sieve implementation based on provided course code. |
| `PrimeNumberFactorization/src/ParallelSieve.java` | Parallel segmented sieve using worker threads and `CyclicBarrier`. |
| `PrimeNumberFactorization/src/PrimeFactorization.java` | Sequential factorization and reusable parallel factorizer using `ExecutorService`. |
| `PrimeNumberFactorization/src/Oblig3Precode.java` | Provided factor-output writer. |

## Documentation

- [Benchmark report](docs/BENCHMARK_REPORT.md)
- [Code origin](docs/CODE_ORIGIN.md)
- [Aggregated benchmark results](docs/data/benchmark_results.csv)
- [Raw benchmark runs](docs/data/benchmark_raw_runs.csv)
- [Benchmark run logs](docs/data/benchmark_run_logs.txt)
- [Matrix result artifacts](Matrisemultiplikasjon/files)

## Benchmark Data Note

The benchmark files in `docs/data` contain historical run data whose logs reference an `IN3030` working directory and temporary build directories. The benchmark report documents this limitation explicitly and does not present those measurements as newly executed results from this checkout.
