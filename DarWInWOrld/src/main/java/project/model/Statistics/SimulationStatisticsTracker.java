package project.model.Statistics;

import project.model.Simulation.Simulation;
import project.model.WorldElements.Animals.Animal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationStatisticsTracker {
    private int numberOfNotOccupiedFields;
    private Map<String, Integer> mostPopularGenes = new HashMap<>();
    private double averageEnergyLevel;
    private int averageLifespan;
    private int averageCountOfChildren;
    private final Simulation simulation;
    private final SimulationStatistics statistics;

    public SimulationStatisticsTracker(Simulation simulation) {
        this.simulation = simulation;
        this.statistics = new SimulationStatistics(0, 0, 0, 0, new int[0], 0.0, 0.0, 0.0);
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
    }

    public SimulationStatistics getStatistics() {
        return statistics;
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
            if (!animal.isDead()) {
                int[] genome = animal.getGenom().getGenomeSequence();
                String key = Arrays.toString(genome);
                int newCount = counts.merge(key, 1, Integer::sum);

                if (newCount > maxCount) {
                    maxCount = newCount;
                    mostPopular = genome;
                }
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
                .filter(animal -> !animal.isDead())  // tylko żywe zwierzęta
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
