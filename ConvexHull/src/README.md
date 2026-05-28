# Convex Hull - Sequential and Parallel Java implementation
Effektiv parallell programmerings oppgave fra universitet. Har ryddet i filene og gjort det mer 
leselig for å vise frem som et repository.

Programmet finner convexe hullet av generete punkter ved hjelp av en rekursjon  og en geometrisk algoritme.
Inneholder både en sekvensiel og en parallell løsning `ForkJoinPool` , for å sammenligne hastighetsforskjell.

## What the program does

- Generates `n` unique points using the provided `NPunkter17` class.
- Finds the convex hull sequentially.
- Finds the convex hull in parallel.
- Compares that both versions return the same hull.
- Measures median runtime over 7 runs.
- Writes hull points to `CONVEX-HULL-POINTS_<n>.txt` when `n < 10000`.
- Can draw the point set and hull with the provided precode when `--draw` is used.

## Run

Compile:

```bash
javac *.java