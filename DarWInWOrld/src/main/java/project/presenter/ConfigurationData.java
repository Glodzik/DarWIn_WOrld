package project.presenter;

public record ConfigurationData(int mapHeight, int mapWidth,
                                int startPlants, int newPlantsEveryday, int startAnimals,
                                int startEnergy, int energyLossEveryDay,
                                int energyLevelToBreed, int energyLossAfterBreed,
                                int minMutation, int maxMutation,
                                int genomeLength, int eatingEnergy, int poisonProbability, int poisonEnergyLoss,
                                int protectionLength, boolean poisonPlantVariant, boolean customPlants) {
}
