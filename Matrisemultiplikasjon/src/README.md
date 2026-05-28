# Matrix Multiplication - Sequential and Parallel Java implementation

Ryddig versjon av matrise multiplikasjon. 
Programmet multipliserer two NxN matriser på seks forskjellige måter. 


- Sequential, not transposed
- Sequential, A transposed
- Sequential, B transposed
- Parallel, not transposed
- Parallel, A transposed
- Parallel, B transposed

## What the program does

- Generates matrix A and B with the provided `Oblig2Precode` class.
- Runs all six matrix multiplication variants.
- Measures median runtime over 7 runs.
- Checks all variants against the sequential not-transposed baseline.
- Supports using all available cores by default.
- Supports setting the number of threads as an optional argument.
- Writes result files with the provided `Oblig2Precode` class when `n <= 100`.

## Run

Compile:

```bash
javac *.java