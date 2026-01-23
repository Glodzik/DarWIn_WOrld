package project.model.simulation.statistics;

import project.model.simulation.Simulation;
import project.model.worldelements.animals.Animal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SimulationStatisticsTracker {
    private final Simulation simulation;
    private final SimulationStatistics statistics;
    private final StatisticsToFile statsWriter;

    public SimulationStatisticsTracker(Simulation simulation) {
        this.simulation = simulation;
        this.statistics = new SimulationStatistics(0, 0, 0, 0, new int[0], 0.0, 0.0, 0.0);
        this.statsWriter = new StatisticsToFile();

        try {
            statsWriter.initializeStatsFile();
        } catch (Exception e) {
            System.err.println("Nie udało się zainicjalizować pliku CSV: " + e.getMessage());
        }
    }

    public void updateStats() {
        statistics.setNumberOfNotOccupiedFields(countNumberOfNotOccupiedFields());
        statistics.setNumberOfAnimals(countNumberOfAnimals());
        statistics.setDeadAnimals(countDeadAnimals());
        statistics.setNumberOfPlants(countNumberOfPlants());
        statistics.setAverageEnergyLevel(countAverageEnergyLevel());
        statistics.setAverageAmountOfChildren(countAverageAmountOfChildren());
        statistics.setMostPopularGenes(countMostPopularGenes());
        statistics.setAverageLifespan(countAverageLifespan());

        statsWriter.saveDailyStats(simulation.getDay(), statistics);
    }

    public SimulationStatistics getStatistics() {
        return statistics;
    }

    public void closeFile() {
        statsWriter.closeStatsFile();
    }

    // counting stats methods

    private int countNumberOfNotOccupiedFields() {
        return simulation.getWorldMap().countFreeFields();
    }

    private int countNumberOfAnimals() {
        return simulation.getAnimals().size();
    }

    private int countNumberOfPlants() {
        return simulation.getPlants().size();
    }

    private int countDeadAnimals() {
        return simulation.getDeadAnimals().size();
    }

    private int[] countMostPopularGenes() {
        List<Animal> animals = simulation.getAnimals();
        if (animals == null || animals.isEmpty()) {
            return new int[0];
        }

        Map<String, Integer> counts = new HashMap<>();
        int[] mostPopular = null;
        int maxCount = 0;

        for (Animal animal : animals) {
            int[] genome = animal.getGenom().getGenomeSequence();
            String key = Arrays.toString(genome);
            int newCount = counts.merge(key, 1, Integer::sum);

            if (newCount > maxCount) {
                maxCount = newCount;
                mostPopular = genome;
            }
        }

        return mostPopular != null ? mostPopular : new int[0];
    }

    private double countAverageEnergyLevel() {
        List<Animal> animals = simulation.getAnimals();
        if (animals == null || animals.isEmpty()) {
            return 0.0;
        }
        return animals.stream()
                .filter(animal -> !animal.isDead())
                .mapToInt(Animal::getEnergy)
                .average()
                .orElse(0.0);
    }

    private double countAverageAmountOfChildren() {
        List<Animal> animals = simulation.getAnimals();
        if (animals == null) {
            return 0.0;
        }

        return animals.stream()
                .filter(animal -> !animal.isDead())
                .mapToInt(Animal::getChildrenCount)
                .average()
                .orElse(0.0);
    }

    private double countAverageLifespan() {
        List<Animal> dead = simulation.getDeadAnimals();
        if (dead.isEmpty()) return 0.0;

        return dead.stream()
                .mapToInt(Animal::getDaysAlive)
                .average()
                .orElse(0.0);
    }
}
