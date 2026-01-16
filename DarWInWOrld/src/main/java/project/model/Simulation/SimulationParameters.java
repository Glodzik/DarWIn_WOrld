package project.model.Simulation;

import project.model.WorldElements.Animals.AnimalParameters;

public record SimulationParameters(int mapHeight, int mapWidth,
                                   int startPlants, int eatingEnergy,
                                   int newPlantsEveryday, int startAnimals,
                                   AnimalParameters animalParameters,
                                   int poisonPlantProbability, int poisonPlantEnergyLoss, int protectionGenomLength) {
}
