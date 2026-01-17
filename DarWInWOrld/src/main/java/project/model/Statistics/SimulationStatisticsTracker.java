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

    public SimulationStatisticsTracker(Simulation simulation) {
        this.numberOfNotOccupiedFields = simulation.getWorldMap().countFreeFields();
    }

    //tutaj liczymy parametry a poźniej przenosimy to do rekordu
/*
    int numberOfAnimals, int numberOfPlants,
    int numberOfNotOccupiedFields, ArrayList<Integer> mostPopularGenes,
    double averageEnergyLevel, int averageAmountOfChildren

 */
}
