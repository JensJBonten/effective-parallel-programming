# Kodeopprinnelse

Dette dokumentet klassifiserer filene i de tre dokumenterte Java-prosjektene etter synlig opprinnelse i kildekoden. Klassifiseringen bygger på filnavn, kommentarer, `@author`-felter og hvordan filene brukes av de aktive programmene. Der opprinnelse ikke kan fastslås direkte fra filen, er det markert som uavklart.

## Prinsipper

| Kategori | Betydning |
| --- | --- |
| Studentimplementasjon | Filen ser ut til å være skrevet eller videreutviklet som del av prosjektløsningen. |
| Utdelt kode / prekode | Filen er eksplisitt merket som prekode, eksempel, `DO NOT MODIFY`, eller har tydelig opphav fra IN3030/UiO-kommentarer. |
| Uavklart opprinnelse | Filen har ikke nok metadata til å avgjøre sikkert om den er studentkode eller utdelt støtteklasse. |

## ConvexHull

| Fil | Klassifisering | Begrunnelse |
| --- | --- | --- |
| `ConvexHull/src/Main.java` | Studentimplementasjon | Aktivt program med forbedringskommentarer, argumenthåndtering, medianmåling og korrekthetssjekk. |
| `ConvexHull/src/ConvexHull.java` | Studentimplementasjon | Inneholder aktiv sekvensiell og parallell convex hull-logikk, inkludert `ForkJoinPool` og intern `HullTask`. |
| `ConvexHull/src/Oblig4Precode.java` | Utdelt kode / prekode | Kommentarene beskriver tegneklasse for oblig og oppgir Kim Sverre Hilton som forfatter. |
| `ConvexHull/src/NPunkter17.java` | Uavklart / trolig utdelt støtteklasse | Filen beskriver `NPunkter17` med versjonshistorikk fra 2015/2020, men uten tydelig eier i filen. |
| `ConvexHull/src/IntList.java` | Uavklart | Generisk hjelpeklasse uten forfatter- eller prekodekommentar. |
| `ConvexHull/src/README.md` | Student-/repo-dokumentasjon | Prosjektlokal dokumentasjon for portefølje/repo. |

## Matrisemultiplikasjon

| Fil | Klassifisering | Begrunnelse |
| --- | --- | --- |
| `Matrisemultiplikasjon/src/Main.java` | Studentimplementasjon | Aktiv samlet benchmark- og korrekthetsdriver for seks varianter. |
| `Matrisemultiplikasjon/src/MatrixMultiAndTranspose.java` | Studentimplementasjon | Implementerer multiplikasjon og transponering brukt av aktiv `Main`. |
| `Matrisemultiplikasjon/src/ParallelNotTransposed.java` | Studentimplementasjon | Aktiv parallell variant med `Thread` og `CyclicBarrier`. |
| `Matrisemultiplikasjon/src/ParallelTransposedA.java` | Studentimplementasjon | Aktiv parallell variant for transponert A. |
| `Matrisemultiplikasjon/src/ParallelTransposedB.java` | Studentimplementasjon | Aktiv parallell variant for transponert B. |
| `Matrisemultiplikasjon/src/CorrectnesCheck.java` | Studentimplementasjon | Prosjektspesifikk toleransebasert sammenligning. |
| `Matrisemultiplikasjon/src/SequentialNotTrasposed.java` | Studentimplementasjon, eldre inngangspunkt | Har egen `main`, men brukes ikke av aktiv samlet `Main`. |
| `Matrisemultiplikasjon/src/TransposeA.java` | Studentimplementasjon, eldre inngangspunkt | Har egen `main`, men brukes ikke av aktiv samlet `Main`. |
| `Matrisemultiplikasjon/src/TransposeB.java` | Studentimplementasjon, eldre inngangspunkt | Har egen `main`, men brukes ikke av aktiv samlet `Main`. |
| `Matrisemultiplikasjon/src/Oblig2Precode.java` | Utdelt kode / prekode | Eksplisitt merket som IN3030 pre-code for Oblig 2, med Magnus Espeland som forfatter. |
| `Matrisemultiplikasjon/src/Example.java` | Utdelt kode / eksempel | Eksplisitt merket som IN3030 pre-code usage example, med Magnus Espeland som forfatter. |
| `Matrisemultiplikasjon/src/README.md` | Student-/repo-dokumentasjon | Prosjektlokal dokumentasjon for portefølje/repo. |
| `Matrisemultiplikasjon/files/*.txt` | Genererte resultatfiler | Skrevet av `Oblig2Precode.saveResult`; dette er outputfiler, ikke kildekode. |

## PrimeNumberFactorization

| Fil | Klassifisering | Begrunnelse |
| --- | --- | --- |
| `PrimeNumberFactorization/src/DriveProgram.java` | Studentimplementasjon | Aktiv driver for sekvensiell/parallell kjøring, korrekthetskontroll, timing og filskriving. |
| `PrimeNumberFactorization/src/SequentialSoe.java` | Studentimplementasjon | Wrapper rundt sekvensiell sil og faktorisering med medianmåling. |
| `PrimeNumberFactorization/src/ParallelSoe.java` | Studentimplementasjon | Wrapper rundt parallell sil og faktorisering med medianmåling. |
| `PrimeNumberFactorization/src/ParallelSieve.java` | Studentimplementasjon | Parallell segmentert sil med arbeidere og `CyclicBarrier`. |
| `PrimeNumberFactorization/src/PrimeFactorization.java` | Studentimplementasjon | Sekvensiell og parallell faktorisering, inkludert gjenbrukbar `ExecutorService`. |
| `PrimeNumberFactorization/src/SieveOfEratosthenes.java` | Utdelt kode / basert på kurskode | Kommentarene beskriver mulig sekvensiell algoritme og oppgir Eric Jul, Shiela Kristoffersen, Magnus Espeland og Kim Hilton som opphav/referanser. |
| `PrimeNumberFactorization/src/Oblig3Precode.java` | Utdelt kode / prekode | Eksplisitt merket `DO NOT MODIFY THIS FILE!`, IN3030 Spring 2019, Magnus Espeland og Kari Stamnes. |
| `PrimeNumberFactorization/src/README.md` | Student-/repo-dokumentasjon | Prosjektlokal dokumentasjon for portefølje/repo. |

## Prosjektmetadata

| Filtype | Klassifisering | Kommentar |
| --- | --- | --- |
| `*.iml` | Prosjektmetadata | IntelliJ IDEA-modulfiler, ikke algoritmisk kildekode. |
| `Matrisemultiplikasjon/.idea/*` | Prosjektmetadata | IDE-konfigurasjon. Ikke vurdert som implementasjonskode. |
| `docs/data/*` | Benchmarkdata | Eksisterende rådata og aggregerte målinger. Ikke kildekode. |
