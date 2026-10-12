# Benchmarkrapport

Denne rapporten dokumenterer benchmarkdataene som ligger i `docs/data` for de tre prosjektene som finnes i dette repositoriet: `ConvexHull`, `Matrisemultiplikasjon` og `PrimeNumberFactorization`. `Oblig1` er bevisst utelatt.

Det er ikke kjørt nye benchmarker for denne rapporten. Tallene under er hentet fra eksisterende rådata og kontrollert mot Java-filene i repositoriet så langt det lar seg gjøre uten å kjøre programmene på nytt.

## Datagrunnlag og begrensninger

| Fil | Innhold | Bruk i rapporten |
| --- | --- | --- |
| `docs/data/benchmark_results.csv` | Aggregerte resultater med median, gjennomsnitt, standardavvik, speedup, effektivitet og korrekthetsstatus. | Hovedkilde for tabellene. |
| `docs/data/benchmark_raw_runs.csv` | Individuelle eksterne kjøringer. Hver verdi er programmets interne median over 7 repetisjoner. | Brukt til å verifisere at aggregerte tall kommer fra flere kjøringer. |
| `docs/data/benchmark_run_logs.txt` | Kompilerings- og kjørelogger. | Brukt til å kontrollere kommandoer, programoutput og avgrensninger. |
| `Matrisemultiplikasjon/files` | Resultatmatriser skrevet av `Oblig2Precode`. | Brukt som korrekthetsartefakter, ikke som benchmarktider. |

Viktig begrensning: loggene viser kjøringer mot `C:\Users\JensB\IdeaProjects\IN3030\Oblig2`, `Oblig3` og `Oblig4`, med bygging til `C:\Users\JensB\AppData\Local\Temp\in3030-bench2\build\...`. De er derfor historiske IN3030-målinger, ikke nye målinger fra denne checkouten. Klassene, argumentmønstrene og programoutputen samsvarer med de aktive implementasjonene i dette repositoriet for de tre dokumenterte prosjektene, men rapporten markerer likevel resultatene som historiske og ikke fullstendig reprodusert i nåværende repository.

`benchmark_run_logs.txt` inneholder også kompilering av `Oblig1`. Disse dataene er ikke brukt her.

## Metode

| Punkt | Beskrivelse |
| --- | --- |
| Antall eksterne kjøringer | 10 per konfigurasjon i rådataene. |
| Interne repetisjoner | De aktive programmene bruker 7 interne repetisjoner og rapporterer median. |
| Tidstaking i aktiv kode | `System.nanoTime()` i de aktive hovedprogrammene for de tre prosjektene. Eldre, alternative matriseklasser bruker `System.currentTimeMillis()`, men de er ikke aktive i samlet `Main`. |
| Speedup | `T_sekvensiell / T_testet`. For Matrisemultiplikasjon er baseline `SEQ_NOT_TRANSPOSED`, slik programmet selv skriver ut. |
| Parallell effektivitet | `speedup / antall tråder`. For sekvensielle algoritmevarianter er trådtallet 1. |
| Korrekthet | Alle 220 rader i `benchmark_results.csv` har `correctness=passed`. I tillegg har fem ikke-tomme n=100-matrisefiler identisk numerisk innhold etter headerlinjene. |
| Testmiljø dokumentert i rådata | Windows-stier og Java-kommandolinjer er dokumentert. CPU-modell, JDK-versjon og minne er ikke dokumentert i rådataene. |

## Kodegjennomgang

| Prosjekt | Aktiv sekvensiell implementasjon | Aktiv parallell implementasjon | Korrekthetskontroll | Tidsmåling |
| --- | --- | --- | --- | --- |
| ConvexHull | `ConvexHull.findConvexHullSeq()` | `ConvexHull.findConvexHullParallel()` med `ForkJoinPool` og `RecursiveTask`. | `Main.sameHull()` sammenligner hullindekser fra sekvensiell og parallell kjøring. | `Main` kjører begge 7 ganger og bruker median. |
| Matrisemultiplikasjon | `MatrixMultiAndTranspose.matrixMultiplication`, `matrixMultiplicationTransposedA`, `matrixMultiplicationTransposedB`. | `ParallelNotTransposed`, `ParallelTransposedA`, `ParallelTransposedB`, alle med radintervaller, `Thread` og `CyclicBarrier`. | `CorrectnesCheck.checkWithLoop()` sammenligner mot sekvensiell ikke-transponert baseline med toleranse `1e-6`. | Aktiv `Main` kjører hver variant 7 ganger og bruker median. |
| PrimeNumberFactorization | `SieveOfEratosthenes` og `PrimeFactorization.primeCalculation`. | `ParallelSieve` med segmentert sil og `CyclicBarrier`; `PrimeFactorization.ParallelFactorizer` med `ExecutorService`. | `DriveProgram` sammenligner primtallsarray med `Arrays.equals` og faktoriseringskart med `equals`. | `SequentialSoe` og `ParallelSoe` måler sil og faktorisering separat over 7 repetisjoner og bruker median. |

Utkommenterte eller alternative inngangspunkter er ikke behandlet som aktive benchmarkprogrammer. For Matrisemultiplikasjon gjelder dette særlig `Example.java`, `SequentialNotTrasposed.java`, `TransposeA.java` og `TransposeB.java`.

## Matrisemultiplikasjon

Aktiv `Main` kjører seks varianter. Den parallelle strategien er statisk radpartisjonering: hver tråd får et sammenhengende intervall av rader i resultatmatrisen og hovedtråden venter på `CyclicBarrier`.

Følgende tabell viser de beste parallelle resultatene per problemstørrelse fra `benchmark_results.csv`. Dette er historiske målinger, men algoritmenavn og outputformat samsvarer med aktiv `Main` i dette repositoriet.

| n | Algoritme | Tråder | Median ms | Speedup | Effektivitet | Korrekthet |
| ---: | --- | ---: | ---: | ---: | ---: | --- |
| 100 | `PARA_NOT_TRANSPOSED` | 8 | 1.604 | 0.6746 | 0.0843 | passed |
| 200 | `PARA_B_TRANSPOSED` | 8 | 1.238 | 3.1716 | 0.3965 | passed |
| 300 | `PARA_B_TRANSPOSED` | 12 | 2.386 | 6.6296 | 0.5525 | passed |

For `n=300` viser et utsnitt av trådskalaeringen at B-transponert variant er klart raskest blant de parallelle variantene:

| Algoritme | Tråder | Median ms | Speedup | Effektivitet |
| --- | ---: | ---: | ---: | ---: |
| `SEQ_NOT_TRANSPOSED` | 1 | 14.893 | 1.0000 | 1.0000 |
| `SEQ_B_TRANSPOSED` | 1 | 12.137 | 1.2365 | 1.2365 |
| `PARA_NOT_TRANSPOSED` | 4 | 4.919 | 3.2227 | 0.8057 |
| `PARA_B_TRANSPOSED` | 4 | 3.571 | 4.3178 | 1.0795 |
| `PARA_NOT_TRANSPOSED` | 8 | 4.823 | 3.1356 | 0.3920 |
| `PARA_B_TRANSPOSED` | 8 | 2.721 | 5.7367 | 0.7171 |
| `PARA_NOT_TRANSPOSED` | 12 | 4.682 | 3.2804 | 0.2734 |
| `PARA_B_TRANSPOSED` | 12 | 2.386 | 6.6296 | 0.5525 |
| `PARA_NOT_TRANSPOSED` | 16 | 5.204 | 2.8892 | 0.1806 |
| `PARA_B_TRANSPOSED` | 16 | 2.458 | 5.9458 | 0.3716 |

Effektivitet over 1.0 for enkelte transponerte varianter betyr ikke at trådene er mer enn perfekt utnyttet. Baseline er ikke-transponert sekvensiell kode, mens `B_TRANSPOSED` endrer minneaksessmønsteret og gir bedre cache-lokalitet. Speedupen blander derfor algoritmisk forbedring og parallellisering.

For små matriser, særlig `n=100`, dominerer trådoppretting og synkronisering. Parallell kode er da tregere enn baseline i de historiske målingene. For `n=200` og `n=300` blir arbeidsmengden stor nok til at `PARA_B_TRANSPOSED` gir tydelig gevinst.

Korrekthetsartefakter: fem ikke-tomme n=100-resultatfiler under `Matrisemultiplikasjon/files` har identisk numerisk innhold etter headeren, med SHA-256 `27b1d2792dbbfe12de812c06d151dd884923ae4bb885da1cdcd216ed2557d1a5`. `O2Result_42_PARA_B_TRANSPOSED_100.txt` er tom, så disse filene alene verifiserer ikke alle seks varianter for n=100.

## PrimeNumberFactorization

Aktiv `DriveProgram` måler sil og faktorisering separat. Den sekvensielle silen bruker byte-representasjon for oddetall. Den parallelle silen deler oddetallsintervallet i segmenter og bruker baseprimtall opp til `sqrt(n)`. Parallell faktorisering bruker en fast trådpool og deler primtallsarrayet mellom worker-oppgaver for hvert tall.

Beste parallelle resultat per deloppgave og n i `benchmark_results.csv`:

| Deloppgave | n | Tråder | Median ms | Speedup | Effektivitet | Korrekthet |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| `PAR_SIEVE` | 20000 | 1 | 0.647 | 1.1874 | 1.1874 | passed |
| `PAR_FACTOR` | 20000 | 1 | 2.661 | 0.0767 | 0.0767 | passed |
| `PAR_SIEVE` | 100000 | 1 | 1.089 | 0.7738 | 0.7738 | passed |
| `PAR_FACTOR` | 100000 | 2 | 2.869 | 0.1746 | 0.0873 | passed |
| `PAR_SIEVE` | 300000 | 2 | 0.805 | 1.2355 | 0.6178 | passed |
| `PAR_FACTOR` | 300000 | 2 | 4.196 | 0.3490 | 0.1745 | passed |

For `n=300000` viser rådataene at parallell sil med 2 tråder er raskere enn sekvensiell sil, men flere tråder gir fallende effektivitet. Parallell faktorisering er tregere enn sekvensiell faktorisering for alle målte n. Det er plausibelt fordi hver faktorisering av ett tall har begrenset arbeid, mens parallellvarianten må opprette og koordinere flere `Callable`-oppgaver og samle resultater.

| Algoritme | Tråder | Median ms | Speedup | Effektivitet |
| --- | ---: | ---: | ---: | ---: |
| `SEQ_SIEVE` | 1 | 1.022 | 1.0000 | 1.0000 |
| `PAR_SIEVE` | 2 | 0.805 | 1.2355 | 0.6178 |
| `PAR_SIEVE` | 8 | 1.020 | 0.9089 | 0.1136 |
| `PAR_SIEVE` | 16 | 2.571 | 0.4249 | 0.0266 |
| `SEQ_FACTOR` | 1 | 1.476 | 1.0000 | 1.0000 |
| `PAR_FACTOR` | 2 | 4.196 | 0.3490 | 0.1745 |
| `PAR_FACTOR` | 8 | 4.502 | 0.3289 | 0.0411 |
| `PAR_FACTOR` | 16 | 5.267 | 0.2812 | 0.0176 |

## ConvexHull

Aktiv `Main` kjører sekvensiell og parallell konveks innhylling på samme punktsett. Parallellversjonen bruker `ForkJoinPool` med `Runtime.getRuntime().availableProcessors()` og begrenser rekursiv task-splitting med et beregnet maksnivå.

Rådataene bruker `threads=16` og `configured_threads=auto` for parallellvarianten:

| n | Algoritme | Tråder | Median ms | Speedup | Effektivitet | Korrekthet |
| ---: | --- | ---: | ---: | ---: | ---: | --- |
| 1000 | `SEQ_CONVEX_HULL` | 1 | 0.422 | 1.0000 | 1.0000 | passed |
| 1000 | `PAR_CONVEX_HULL` | 16 | 1.526 | 0.2889 | 0.0181 | passed |
| 5000 | `SEQ_CONVEX_HULL` | 1 | 1.704 | 1.0000 | 1.0000 | passed |
| 5000 | `PAR_CONVEX_HULL` | 16 | 4.496 | 0.3217 | 0.0201 | passed |
| 10000 | `SEQ_CONVEX_HULL` | 1 | 1.875 | 1.0000 | 1.0000 | passed |
| 10000 | `PAR_CONVEX_HULL` | 16 | 4.068 | 0.7589 | 0.0474 | passed |
| 50000 | `SEQ_CONVEX_HULL` | 1 | 5.082 | 1.0000 | 1.0000 | passed |
| 50000 | `PAR_CONVEX_HULL` | 16 | 1.607 | 3.1782 | 0.1986 | passed |
| 100000 | `SEQ_CONVEX_HULL` | 1 | 8.422 | 1.0000 | 1.0000 | passed |
| 100000 | `PAR_CONVEX_HULL` | 16 | 1.986 | 4.2671 | 0.2667 | passed |

Resultatene viser typisk parallell overhead: ved små n er parallellversjonen tregere, mens større punktmengder gir speedup. Effektiviteten er likevel moderat, noe som er forventet for en rekursiv geometrisk algoritme der arbeid og task-størrelser kan bli skjevt fordelt etter punktsettets form.

## Samlet vurdering

| Prosjekt | Hovedobservasjon | Begrensning |
| --- | --- | --- |
| Matrisemultiplikasjon | Parallell `B_TRANSPOSED` skalerer best i de historiske dataene, særlig fra n=200 og oppover. | Speedup må tolkes som kombinasjon av bedre minnelokalitet og parallellisering, ikke ren trådeffekt. |
| PrimeNumberFactorization | Parallell sil kan gi gevinst ved enkelte n og trådtall, mens parallell faktorisering taper mot sekvensiell kode i målingene. | Målingene er små i millisekunder og følsomme for overhead og miljøstøy. |
| ConvexHull | Parallellversjonen lønner seg først for større punktmengder. | Trådtall er automatisk og logget som 16; det finnes ikke separate målinger for andre trådtall i rådataene. |

Konklusjonen er at eksisterende benchmarkdata er nyttige som historisk ytelsesdokumentasjon for implementasjoner med samme struktur og output som dagens kode, men de er ikke fullstendig verifiserte som nye resultater fra dette repositoriet. Rapporten bruker derfor tallene med tydelig kildebegrensning og presenterer ikke eldre eller alternative inngangspunkter som aktive implementasjoner.
