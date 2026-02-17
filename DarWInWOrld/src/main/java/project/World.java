package project;

import project.model.coordinates.Vector2D;
import project.model.map.RectangularMap;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationParameters;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.animals.Genome;
import project.model.worldelements.edibleelements.Antidote;
import project.model.worldelements.edibleelements.Plant;
import project.model.worldelements.edibleelements.PlantParameters;

import java.util.Random;

public final class World {
    public static void main(String[] args) {
        RectangularMap map = new RectangularMap(2, 2);

        Animal[] animals = new Animal[5];
        Plant[] plantArray = new Plant[10];

        AnimalParameters animalParameters = new AnimalParameters(100, 10, 80, 20, 1, 3, 8);
        Genome protectionGenome = new Genome(8);

        for (int i = 0; i < animals.length; i++) {
            animals[i] = new Animal(animalParameters, protectionGenome);
            map.place(animals[i]);
        }

        Random random = new Random();
        for (int i = 0; i < plantArray.length; i++) {
            int antidoteType = random.nextInt(5);
            plantArray[i] = new Antidote(new Vector2D(0, 0), antidoteType);
            map.placePlant(plantArray[i]);
        }

        for(int i = 0; i < 100; i++) {
            map.move(animals[i % animals.length]);
            map.eatIfPossible(animals[i % animals.length]);
        }

        SimulationParameters parameters = new SimulationParameters(5,5, 5,2,5,
                animalParameters,
                new PlantParameters(20, 40, -20), 8, false, false);
        Simulation simulation = new Simulation(parameters);
        simulation.run();
    }
}
