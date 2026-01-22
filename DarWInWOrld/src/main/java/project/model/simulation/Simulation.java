package project.model.simulation;

import project.model.coordinates.Vector2D;
import project.model.map.RectangularMap;
import project.model.random.RandomGenerator;
import project.model.simulation.statistics.SimulationStatisticsTracker;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.animals.AnimalComparator;
import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.animals.Genome;
import project.model.worldelements.edibleelements.Plant;
import project.model.worldelements.edibleelements.PlantParameters;

import java.util.ArrayList;
import java.util.List;

public final class Simulation implements Runnable {
    private List<Animal> animals = new ArrayList<Animal>();
    private List<Animal> deadAnimals = new ArrayList<>();
    private List<Plant> plants = new ArrayList<Plant>();
    private final RectangularMap worldMap;
    private final Genome protectionGenome;
    private int day = 1;
    private final SimulationParameters simulationParameters;
    private boolean isRunning = false;
    private final SimulationStatisticsTracker statisticsTracker;

    public Simulation(SimulationParameters parameters) {
        this.worldMap = new RectangularMap(parameters.mapWidth(), parameters.mapHeight());
        this.protectionGenome = new Genome(parameters.protectionGenomLength());
        this.simulationParameters = parameters;
        this.statisticsTracker = new SimulationStatisticsTracker(this);

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
                Plant plant = RandomGenerator.randomCustomPlants(
                        parameters.poisonProbability(),
                        parameters.energy(),
                        parameters.poisonEnergyLoss()
                );
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        animalsMoving();
        animalsEating();
        animalsBreeding();
        addPlants(simulationParameters.newPlantsEveryday(), simulationParameters.plantParameters(), simulationParameters.customPlants());
        animalsEnergyLoss();
    }

    private void removeAllDead() {
        updateAnimalsAndPlants();
        List<Animal> justDied = animals.stream()
                .filter(Animal::isDead)
                .toList();
        deadAnimals.addAll(justDied);
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
        List<Vector2D> positions = worldMap.getAllPositions();
        for(Vector2D position : positions) {
            List<Animal> animalsAtPosition = worldMap.getAnimalsAt(position);
            if (animalsAtPosition.size() >= 2) {
                animalsAtPosition.sort(AnimalComparator.getComparator());
            }

            Animal animal = animalsAtPosition.getFirst();
            worldMap.eatIfPossible(animal);
        }
    }

    private void animalsBreeding() {
        updateAnimalsAndPlants();
        List<Vector2D> positions = worldMap.getAllPositions();
        for(Vector2D position : positions) {
            List<Animal> animalsAtPosition = worldMap.getAnimalsAt(position);
            if (animalsAtPosition.size() >= 2) {
                animalsAtPosition.sort(AnimalComparator.getComparator());

                Animal parent1 = animalsAtPosition.get(0);
                Animal parent2 = animalsAtPosition.get(1);

                AnimalParameters params = simulationParameters.animalParameters();
                if (parent1.getEnergy() >= params.energyLevelToBreed() && parent2.getEnergy() >= params.energyLevelToBreed()) {
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

    public void start() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        while (!animals.isEmpty() && isRunning) {
            statisticsTracker.updateStats();
            dayAction();
        }
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public List<Animal> getDeadAnimals() {
        return deadAnimals;
    }

    public List<Plant> getPlants() {
        return plants;
    }

    public RectangularMap getWorldMap() {
        return worldMap;
    }

    public int getDay() {
        return day;
    }

    public SimulationStatisticsTracker getStatisticsTracker() {
        return statisticsTracker;
    }

    public SimulationParameters getParameters() {
        return simulationParameters;
    }
}
