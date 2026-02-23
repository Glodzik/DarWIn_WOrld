# DarWIn WOrld

**Wariant:** Trucizny i Odpornosc  
**Autorzy:** Michal Grabos, Jakub Glod  
**Technologie:** Java 21, JavaFX 21, Gradle, JUnit 5

---

## Opis

Symulator ewolucyjny, w ktorym zwierzeta poruszaja sie po prostokatnej mapie, jedza rosliny, rozmnazaja sie i ewoluuja dzieki genomom. Mapa podzielona jest na **dzungle** (centralny pas, 80% roslin) i **step** (reszta, 20% roslin). Swiat jest kulisty -- krawedzie lewo/prawo zawijaja sie, a na biegunach zwierze zawraca.

W wariancie **Trucizny i Odpornosc** rosliny moga byc trujace, a zwierzeta zyskuja odpornosc proporcjonalna do zgodnosci ich genomu z genomem ochronnym.

---

## Uruchomienie

**Wymagania:** Zainstalowany [JDK 21](https://www.oracle.com/pl/java/technologies/downloads/#java21)
```bash
cd DarWInWOrld
./gradlew build
./gradlew run
```


## Konfiguracja symulacji

![Konfiguracja symulacji](konfiguracja_symulacji.png)

## Symulacja

![Symulacja](symulacja.png)

## Symulacja z customowymi roślinami

![Symulacja z customowymi roślinami](symulacja_custom_rosliny.png)

---

## Architektura

Wzorzec **MVC** z wzorcem **Observer** do aktualizacji GUI.

```
project/
  model/
    coordinates/        Vector2D, Boundary
    map/                WorldMap (interfejs), RectangularMap, MapDirection
    random/             RandomGenerator
    simulation/         Simulation, SimulationApp, SimulationParameters
      statistics/       SimulationStatistics, SimulationStatisticsTracker, StatisticsToFile
    worldelements/      WorldElement (interfejs)
      animals/          Animal, Genome, AnimalComparator, AnimalParameters
      edibleelements/   Plant, Antidote, Poison, TypeOfAntidote, TypeOfPoison, PlantParameters
  presenter/            ConfigurationPresenter, MapPresenter, MapDrafter, PresetService
```

---

## Zrealizowane rozszerzenia

1. **Wiele symulacji jednoczesnie** -- osobne okna, osobne watki
2. **Wizualizacja energii** -- paski energii nad zwierzetami (zielony -> czerwony)
3. **Presety i zapis/odczyt konfiguracji** -- 5 wbudowanych presetow + serializacja do `.properties`
4. **Eksport statystyk do CSV** -- dzienne statystyki zapisywane do `simulationstats/`
5. **Customowe rosliny** -- 10 gatunkow z wlasnymi teksturami i skalowana energia (rozszerzenie wlasne)

---

## Parametry konfiguracji

| Grupa | Parametry |
|-------|-----------|
| Mapa | szerokosc, wysokosc, poczatkowe rosliny, nowe rosliny/dzien, poczatkowe zwierzeta |
| Zwierzeta | energia startowa, dzienna strata, prog rozmnazania, koszt rozmnazania, min/max mutacji, dlugosc genomu |
| Rosliny | energia bazowa, % trucizn, strata od trucizn |
| Warianty | trucizny wl/wyl, customowe rosliny wl/wyl, dlugosc genomu ochronnego |

---

## Testy

9 klas testowych: `AnimalTest`, `GenomeTest`, `RectangularMapTest`, `SimulationTest`, `Vector2DTest`, `MapDirectionTest`, `RandomGeneratorTest`, `AntidoteTest`, `PoisonTest`

```bash
cd DarWInWOrld
./gradlew test
```
