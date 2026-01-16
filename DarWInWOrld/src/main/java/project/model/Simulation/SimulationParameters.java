package project.model.Simulation;

import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.EdibleElements.PlantParameters;

public record SimulationParameters(int mapHeight, int mapWidth,
                                   int startPlants, int newPlantsEveryday, int startAnimals,
                                   AnimalParameters animalParameters,
                                   PlantParameters plantParameters,
                                   int protectionGenomLength, boolean customPlants) {
}
