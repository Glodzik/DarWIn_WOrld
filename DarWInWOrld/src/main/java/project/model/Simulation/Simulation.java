package project.model.Simulation;

import project.model.RandomGenerator;
import project.model.RectangularMap;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalComparator;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.Animals.Genome;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.PlantParameters;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Animal> animals = new ArrayList<Animal>();
    private List<Plant> plants = new ArrayList<Plant>();
    private RectangularMap worldMap;
    private final Genome protectionGenome;

    public Simulation(SimulationParameters parameters) {
        this.worldMap = new RectangularMap(parameters.mapWidth(), parameters.mapHeight());
        this.protectionGenome = new Genome(parameters.protectionGenomLength());

        addAnimals(parameters.startAnimals(), parameters.animalParameters());
        addPlants(parameters.startPlants(), parameters.plantParameters(), parameters.customPlants());

        animals = worldMap.getAnimals();
        plants = worldMap.getPlants();

        dayAction();
        /*
            Symulacja każdego dnia składa się z poniższej sekwencji kroków:

            Usunięcie martwych zwierzaków z mapy.
            Skręt i przemieszczenie każdego zwierzaka.
            Konsumpcja roślin, na których pola weszły zwierzaki.
            Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            Wzrastanie nowych roślin na wybranych polach mapy.
        */
    }

    public void addAnimals(int animalsCount, AnimalParameters parameters) {
        for(int i = 0; i < animalsCount; i++) {
            Animal animal = new Animal(parameters, protectionGenome);
            worldMap.place(animal);
        }
    }

    public void addPlants(int plantsCount, PlantParameters parameters, boolean customPlants) {
        if(customPlants) {
            for (int i = 0; i < plantsCount; i++) {
                Plant plant = RandomGenerator.randomCustomPlants(parameters.poisonProbability());
                worldMap.placePlant(plant);
            }
        } else {
            for (int i = 0; i < plantsCount; i++) {
                Plant plant = RandomGenerator.randomPlant(parameters.energy(), parameters.poisonProbability(), parameters.poisonEnergyLoss());
                worldMap.placePlant(plant);
            }
        }
    }

    public void dayAction() {
        //removeAllDead();

        //animalsMoving();
        // miedzy kazdym ruchem ma byc pauza na moment
        // moze animalsMoving(time)?

        //animalsEating();
        //animalsBreeding();
        //addNewPlants();
    }
}
