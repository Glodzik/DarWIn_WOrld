package project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.model.coordinates.Vector2D;
import project.model.simulation.Simulation;
import project.model.simulation.SimulationParameters;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.edibleelements.PlantParameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    private Simulation simulation;
    private SimulationParameters parameters;

    @BeforeEach
    void setUp() {
        AnimalParameters animalParams = new AnimalParameters(
                100,
                10,
                80,
                40,
                2,
                5,
                8
        );

        PlantParameters plantParams = new PlantParameters(
                20,
                30,
                10
        );

        parameters = new SimulationParameters(
                10,
                10,
                5,
                10,
                5,
                animalParams,
                plantParams,
                8,
                false,
                true
        );

        simulation = new Simulation(parameters);
    }

    @Test
    void testConstructor() {
        assertNotNull(simulation);
        assertNotNull(simulation.getWorldMap());
        assertNotNull(simulation.getAnimals());
        assertNotNull(simulation.getPlants());
        assertEquals(1, simulation.getDay());
        assertEquals(5, simulation.getAnimals().size());
    }

    @Test
    void testAddAnimals() {
        int initialAnimalCount = simulation.getAnimals().size();
        AnimalParameters animalParams = simulation.getParameters().animalParameters();

        simulation.addAnimals(3, animalParams);
        simulation.updateAnimalsAndPlants();

        assertEquals(initialAnimalCount + 3, simulation.getAnimals().size());
    }

    @Test
    void testUpdateAnimalsAndPlants() {
        int initialAnimalCount = simulation.getAnimals().size();
        int initialPlantCount = simulation.getPlants().size();

        simulation.updateAnimalsAndPlants();

        assertEquals(initialAnimalCount, simulation.getAnimals().size());
        assertEquals(initialPlantCount, simulation.getPlants().size());
    }

    @Test
    void testDayActionIncrementsDay() {
        int initialDay = simulation.getDay();

        simulation.dayAction();

        assertEquals(initialDay + 1, simulation.getDay());
    }

    @Test
    void testRemoveAllDeadAnimals() {
        List<Animal> animals = simulation.getAnimals();
        Animal animal = animals.getFirst();
        
        animal.energyLoss(animal.getEnergy());

        int initialAnimalCount = animals.size();
        int initialDeadCount = simulation.getDeadAnimals().size();

        Method privateMethod = null;
        try {
            privateMethod = Simulation.class.getDeclaredMethod("removeAllDead");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        privateMethod.setAccessible(true);

        try {
            privateMethod.invoke(simulation);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        simulation.updateAnimalsAndPlants();

        assertEquals(initialAnimalCount - 1, simulation.getAnimals().size());
        assertEquals(initialDeadCount + 1, simulation.getDeadAnimals().size());
        assertTrue(simulation.getDeadAnimals().contains(animal));
        assertFalse(simulation.getAnimals().contains(animal));
    }

    @Test
    void testAnimalsBreedingCreatesNewAnimal() {
        AnimalParameters animalParams = new AnimalParameters(100, 10, 80, 40, 0, 1, 8);
        PlantParameters plantParams = new PlantParameters(20, 30, 10);

        SimulationParameters params = new SimulationParameters(
                10, 10, 0, 10, 0, animalParams, plantParams, 8, false, false
        );

        simulation = new Simulation(params);

        Vector2D position = new Vector2D(2,2);
        Animal parent1 = new Animal(position);
        Animal parent2 = new Animal(position);
        simulation.getWorldMap().place(parent1, position);
        simulation.getWorldMap().place(parent2, position);

        simulation.updateAnimalsAndPlants();
        int initialAnimalCount = simulation.getAnimals().size();

        Method privateMethod = null;
        try {
            privateMethod = Simulation.class.getDeclaredMethod("animalsBreeding");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        privateMethod.setAccessible(true);

        try {
            privateMethod.invoke(simulation);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        simulation.updateAnimalsAndPlants();

        assertEquals(initialAnimalCount + 1, simulation.getAnimals().size());
    }

    @Test
    void testAnimalsEnergyLossDecreasesEnergy() {
        List<Animal> animals = simulation.getAnimals();
        int initialEnergy = parameters.animalParameters().startEnergy();

        simulation.animalsEnergyLoss();

        for(Animal animal : animals) {
            assertEquals(initialEnergy - parameters.animalParameters().energyLossEveryDay(), animal.getEnergy());
        }
    }

    @Test
    void testStartAndStop() {
        simulation.start();
        assertTrue(simulation.isRunning());

        simulation.stop();
        assertFalse(simulation.isRunning());
    }

    @Test
    void testRunWithNoAnimals() {
        AnimalParameters animalParams = new AnimalParameters(100, 10, 80, 40, 0, 1, 8);
        PlantParameters plantParams = new PlantParameters(20, 30, 10);

        SimulationParameters params = new SimulationParameters(
                10, 10, 0, 10, 0, animalParams, plantParams, 8, false, false
        );

        Simulation simulation = new Simulation(params);
        simulation.start();

        simulation.run();
        simulation.updateAnimalsAndPlants();

        assertTrue(simulation.getAnimals().isEmpty());
    }
}