package project.model.Statistics;

import project.model.Simulation.Simulation;

import java.util.HashMap;
import java.util.Map;

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
        statistics.setNumberOfNotOccupiedFields(simulation.getWorldMap().countFreeFields());
        statistics.setNumberOfAnimals(simulation.getAnimals().size());
        statistics.setNumberOfPlants(simulation.getPlants().size());
        //statistics.setMostPopularGenes();
        //statistics.setAverageEnergyLevel();
        //statistics.setAverageAmountOfChildren();
        //statistics.getAverageLifespan();
    }

    public SimulationStatistics getStatistics() {
        return statistics;
    }

}
