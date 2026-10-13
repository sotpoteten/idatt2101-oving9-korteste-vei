# Korteste vei i Norden: Dijkstra og ALT

Øving 9 i IDATT2101 Algoritmer og datastrukturer ved NTNU, høsten 2023.

Programmet leser et veikart over Norden (ca. 8 millioner noder og 18 millioner kanter) og finner raskeste kjørerute mellom to steder, først med Dijkstras algoritme og deretter med ALT (A\*, landemerker og trekantulikhet). Det kan også finne de nærmeste interessepunktene av en gitt type, for eksempel de fem nærmeste ladestasjonene.

Koden ligger her slik den ble levert. Se [Kjente svakheter](#kjente-svakheter).

## Slik fungerer det

**Preprosessering** (`preprosessering()` i [Graf.java](Graf.java#L203)). Fem landemerker velges automatisk:

1. Det første er noden med lengst kjøretid fra node 0.
2. Hvert av de neste er noden med størst samlet luftlinjeavstand til landemerkene som allerede er valgt.
3. Et landemerke som ikke har vei til node 0, byttes ut med en nabonode.

Deretter kjøres Dijkstra fra hvert landemerke, både på grafen og på den omvendte grafen. Det gir kjøretiden fra og til hvert landemerke for alle noder.

**ALT** (`ALT()` i [Graf.java](Graf.java#L442)). For en node *n*, et mål *m* og et landemerke *L* gir trekantulikheten to nedre grenser for kjøretiden fra *n* til *m*:

- avstand(*L*, *m*) − avstand(*L*, *n*)
- avstand(*n*, *L*) − avstand(*m*, *L*)

Estimatet til en node er den største grensen over alle landemerkene, og prioritetskøen sorteres på kjøretid så langt pluss estimat.

**Interessepunkter** (`dijkstraForInteressepunkt()` i [Graf.java](Graf.java#L379)). Et vanlig Dijkstra-søk som stopper når det har funnet ønsket antall noder med riktig type. Typen er en bitmaske:

| Kode | Type |
|---|---|
| 1 | Stedsnavn |
| 2 | Bensinstasjon |
| 4 | Ladestasjon |
| 8 | Spisested |
| 16 | Drikkested |
| 32 | Overnattingssted |

## Resultater

Målt på Norden-kartet med en Intel Core i3-7100U.

| Rute | Kjøretid | Dijkstra: noder undersøkt | Dijkstra: tid | ALT: noder undersøkt | ALT: tid |
|---|---|---|---|---|---|
| Selbustrand – GreenStar Hotel Lahti | 15 t 24 min 42 s | 7 085 471 | 9094 ms | 39 482 | 93 ms |
| Orkanger – Trondheim | 32 min 38 s | 33 464 | 19 ms | 7 528 | 5 ms |

Begge algoritmene finner samme kjøretid. På den lange ruten undersøker ALT under 1 % av nodene Dijkstra må innom.

Rutene og interessepunktene fra kjøringene ligger som koordinatlister i [Output/](Output/).

## Kjøring

Krever Java. Koden har `ø` og `å` i identifikatorer, så tegnsettet må oppgis ved kompilering:

```
javac -encoding UTF-8 *.java
java -Xmx12G Graf
```

Programmet leser `noder.txt`, `kanter.txt` og `interessepkt.txt` fra mappen det kjøres i, og skriver resultater til `Output/`. Norden-kartet trenger flere gigabyte med heap. `-Xmx12G` er verdien som ble brukt under innleveringen.

### Data

Norden-filene er for store for GitHub (`kanter.txt` alene er 460 MB) og følger ikke med. De ble delt ut i faget og må legges i rotmappen.

Et mindre kart over Island ligger i [Island/](Island/). `main` er hardkodet for Norden, så for å kjøre på Island må du:

1. kopiere de tre filene fra `Island/` til rotmappen,
2. fjerne kommentartegnene rundt Island-ruten øverst i `main` ([Graf.java](Graf.java#L35-L42)),
3. kommentere ut resten av `main`, siden nodenumrene der bare finnes i Norden-kartet.

## Kjente svakheter

- `dijkstraForInteressepunkt` har en invertert `iKø`-sjekk ([Graf.java](Graf.java#L397)). En node kan bli liggende flere ganger i køen, og samme interessepunkt kan dermed bli talt to ganger.
- Reservevalget av landemerke bruker `>` der det skulle stått `>=` ([Graf.java](Graf.java#L240)).
- `PriorityQueue.remove()` er lineær i køens størrelse, noe som gjør preprosesseringen tregere enn nødvendig.
- «Dijkstra» er feilstavet som «Djikstra» i metodenavn, utskrift og filnavn.

## Lisens og kreditering

Koden er lisensiert under [MIT](LICENSE).

Kartdataene er © [OpenStreetMap](https://www.openstreetmap.org/copyright)-bidragsytere og lisensiert under Open Database License (ODbL). MIT-lisensen gjelder ikke dem.
