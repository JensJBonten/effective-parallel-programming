# Prime Numbers - Sequential and Parallel Java implementation

Effektiv parallell programmerings oppgave fra universitet. Har ryddet i filene og gjort det mer
leselig for å vise frem som et repository.

Programmet generer primtall med Sieve of Erathosthenes og faktoriserer de 100 største nummerene under `n*n`
Både sekvensiell og parallel løsning for sammenligning av tidsbruk.

## What the program does

- Generates prime numbers up to `n` sequentially.
- Generates prime numbers up to `n` in parallel.
- Tests that the sequential and parallel sieve return the same primes.
- Factorizes the 100 largest numbers below `n*n` sequentially.
- Factorizes the same numbers in parallel, using multiple threads for each factorization.
- Tests that the sequential and parallel factorization return the same factors.
- Measures median runtime over 7 runs.
- Writes factorization results with the provided `Oblig3Precode` class.

## Run

Compile:

```bash
javac *.java 