package project.model.Statistics;

import project.model.Simulation.Simulation;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.Genome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimulationStatisticsTracker {
    private int numberOfNotOccupiedFields;
    private Map<String, Integer> mostPopularGenes = new HashMap<>();
    private double averageEnergyLevel;
    private int averageDaysAlive;
    private int averageCountOfChildren;
    private final Simulation simulation;
    private final SimulationStatistics statistics;

    public SimulationStatisticsTracker(Simulation simulation) {
        this.simulation = simulation;
        this.statistics = new SimulationStatistics(0, 0, 0, new int[0], 0.0, 0.0, 0.0);
    }

    public void updateStats() {
        statistics.setNumberOfNotOccupiedFields(countNumberOfNotOccupiedFields());
        statistics.setNumberOfAnimals(countNumberOfAnimals());
        statistics.setNumberOfPlants(countNumberOfPlants());
        statistics.setAverageEnergyLevel(countAverageEnergyLevel());
        statistics.setAverageAmountOfChildren(countAverageAmountOfChildren());
        //statistics.setMostPopularGenes();
        //statistics.setAverageLifespan();
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
}
