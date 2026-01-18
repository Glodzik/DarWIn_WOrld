package project;

import project.model.Coordinates.Vector2D;
import project.model.Map.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.Animals.Genome;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.PlantParameters;

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
                new PlantParameters(20, 40, -20), 8, false);
        Simulation simulation = new Simulation(parameters);
        simulation.run();
    }
}
