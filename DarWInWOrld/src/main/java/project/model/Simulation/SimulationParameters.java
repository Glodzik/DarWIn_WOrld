package project.model.Simulation;

public record SimulationParameters(int mapHeight, int mapWidth,
                                   int startPlants, int eatingEnergy,
                                   int newPlantsEveryday, int startAnimals,
                                   int startEnergy, int energyLossEveryDay,
                                   int energyLevelToBreed, int energyLossAfterBreed,
                                   int minMutation, int maxMutation,
                                   int genomLength,
                                   int poisonPlantProbability, int poisonPlantEnergyLoss, int protectionGenomLength) {
}
