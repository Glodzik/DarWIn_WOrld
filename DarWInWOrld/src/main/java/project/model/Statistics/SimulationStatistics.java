package project.model.Statistics;

public class SimulationStatistics {
    private int numberOfAnimals;
    private int numberOfPlants;
    private int numberOfNotOccupiedFields;
    private int[] mostPopularGenes;
    private double averageEnergyLevel;
    private double averageAmountOfChildren;
    private double averageLifespan;

    public SimulationStatistics(int numberOfAnimals,
                                int numberOfPlants,
                                int numberOfNotOccupiedFields,
                                int[] mostPopularGenes,
                                double averageEnergyLevel,
                                double averageAmountOfChildren,
                                double averageLifespan) {
        this.numberOfAnimals = numberOfAnimals;
        this.numberOfPlants = numberOfPlants;
        this.numberOfNotOccupiedFields = numberOfNotOccupiedFields;
        this.mostPopularGenes = mostPopularGenes != null ? mostPopularGenes.clone() : new int[0];
        this.averageEnergyLevel = averageEnergyLevel;
        this.averageAmountOfChildren = averageAmountOfChildren;
        this.averageLifespan = averageLifespan;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public void setNumberOfAnimals(int numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public void setNumberOfPlants(int numberOfPlants) {
        this.numberOfPlants = numberOfPlants;
    }

    public int getNumberOfNotOccupiedFields() {
        return numberOfNotOccupiedFields;
    }

    public void setNumberOfNotOccupiedFields(int numberOfNotOccupiedFields) {
        this.numberOfNotOccupiedFields = numberOfNotOccupiedFields;
    }

    public int[] getMostPopularGenes() {
        return mostPopularGenes != null ? mostPopularGenes.clone() : new int[0];
    }

    public void setMostPopularGenes(int[] mostPopularGenes) {
        this.mostPopularGenes = mostPopularGenes != null ? mostPopularGenes.clone() : new int[0];
    }

    public double getAverageEnergyLevel() {
        return averageEnergyLevel;
    }

    public void setAverageEnergyLevel(double averageEnergyLevel) {
        this.averageEnergyLevel = averageEnergyLevel;
    }

    public double getAverageAmountOfChildren() {
        return averageAmountOfChildren;
    }

    public void setAverageAmountOfChildren(double averageAmountOfChildren) {
        this.averageAmountOfChildren = averageAmountOfChildren;
    }

    public double getAverageLifespan() {
        return averageLifespan;
    }

    public void setAverageLifespan(double averageLifespan) {
        this.averageLifespan = averageLifespan;
    }
}