package project.model.simulation;

import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.edibleelements.PlantParameters;

public record SimulationParameters(int mapHeight, int mapWidth,
                                   int startPlants, int newPlantsEveryday, int startAnimals,
                                   AnimalParameters animalParameters,
                                   PlantParameters plantParameters,
                                   int protectionGenomLength, boolean poisonPlants, boolean customPlants) {
}
