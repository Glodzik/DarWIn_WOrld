package project.model.Simulation;

import project.model.RandomGenerator;
import project.model.RectangularMap;
import project.model.Vector2D;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalComparator;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.Animals.Genome;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.PlantParameters;

import java.util.ArrayList;
import java.util.List;

public final class Simulation {
    private List<Animal> animals = new ArrayList<Animal>();
    private List<Plant> plants = new ArrayList<Plant>();
    private RectangularMap worldMap;
    private final Genome protectionGenome;
    private int day = 0;
    private SimulationParameters simulationParameters;

    public Simulation(SimulationParameters parameters) {
        this.worldMap = new RectangularMap(parameters.mapWidth(), parameters.mapHeight());
        this.protectionGenome = new Genome(parameters.protectionGenomLength());
        this.simulationParameters = parameters;

        addAnimals(parameters.startAnimals(), parameters.animalParameters());
        addPlants(parameters.startPlants(), parameters.plantParameters(), parameters.customPlants());

        updateAnimalsAndPlants();
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

    public void updateAnimalsAndPlants() {
        animals = worldMap.getAnimals();
        plants = worldMap.getPlants();
    }

    public void dayAction() {
        day++;

        removeAllDead();
        animalsMoving();
        // miedzy kazdym ruchem ma byc pauza na moment
        // moze animalsMoving(time)?

        animalsEating();
        animalsBreeding();
        addPlants(simulationParameters.newPlantsEveryday(), simulationParameters.plantParameters(), simulationParameters.customPlants());
        animalsEnergyLoss();
    }

    private void removeAllDead() {
        updateAnimalsAndPlants();
        worldMap.removeDeadAnimals();
    }

    private void animalsMoving() {
        updateAnimalsAndPlants();
        for(Animal animal : animals) {
            worldMap.move(animal);
        }
    }

    private void animalsEating() {
        updateAnimalsAndPlants();
        for(Animal animal : animals) {
            worldMap.eatIfPossible(animal);
        }
    }

    private void animalsBreeding() {
        updateAnimalsAndPlants();
        List<Vector2D> positions = worldMap.getAllPositions();
        for(Vector2D position : positions) {
            List<Animal> aliveAnimals = worldMap.getAnimalsAt(position);
            if (aliveAnimals.size() >= 2) {
                aliveAnimals.sort(AnimalComparator.createComparator());

                Animal parent1 = aliveAnimals.get(0);
                Animal parent2 = aliveAnimals.get(1);

                AnimalParameters params = simulationParameters.animalParameters();
                if (parent1.getEnergy() >= params.energyLevelToBreed() &&
                        parent2.getEnergy() >= params.energyLevelToBreed()) {
                    Animal child = new Animal(parent1, parent2, params, protectionGenome);
                    worldMap.place(child, child.getPosition());
                }
            }
        }
    }

    public void animalsEnergyLoss() {
        updateAnimalsAndPlants();
        for(Animal animal : animals) {
            animal.energyLoss(simulationParameters.animalParameters().energyLossEveryDay());
        }
    }

    public void run() {
        while (!animals.isEmpty()) {
            dayAction();
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public RectangularMap getWorldMap() {
        return worldMap;
    }
}
