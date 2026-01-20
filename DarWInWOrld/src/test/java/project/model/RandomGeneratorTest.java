package project.model;

import org.junit.jupiter.api.Test;
import project.model.Coordinates.Boundary;
import project.model.Coordinates.Vector2D;
import project.model.Map.MapDirection;
import project.model.Random.RandomGenerator;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.Poison;

import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @Test
    void randomPositionWithinBounds() {
        Boundary bounds = new Boundary(new Vector2D(0, 0), new Vector2D(4, 4));
        Vector2D position = RandomGenerator.randomPositionWithinBounds(bounds);

        assertNotNull(position);
        assertTrue(position.getX() >= 0 && position.getX() <= 4);
        assertTrue(position.getY() >= 0 && position.getY() <= 4);
        assertTrue(position.precedes(bounds.upperRight()));
        assertTrue(position.follows(bounds.lowerLeft()));
    }

    @Test
    void randomDirection() {
        MapDirection direction = RandomGenerator.randomDirection();

        assertNotNull(direction);
    }

    @Test
    void probability() {
        double prob = RandomGenerator.probability();

        assertTrue(prob >= 0.0 && prob < 1.0);
    }

    @Test
    void randomCustomPlantsWithZeroProbability() {
        int poisonPlantProbability = 0;
        int basePlantEnergy = 10;
        int basePoisonEnergy = 5;

        Plant plant = RandomGenerator.randomCustomPlants(poisonPlantProbability, basePlantEnergy, basePoisonEnergy);

        assertNotNull(plant);
        assertInstanceOf(Antidote.class, plant);
        assertTrue(plant.getEnergy() > 0);
    }

    @Test
    void randomCustomPlantsWithFullProbability() {
        int poisonPlantProbability = 100;
        int basePlantEnergy = 10;
        int basePoisonEnergy = 5;

        Plant plant = RandomGenerator.randomCustomPlants(poisonPlantProbability, basePlantEnergy, basePoisonEnergy);

        assertNotNull(plant);
        assertInstanceOf(Poison.class, plant);
        assertTrue(plant.getEnergy() < 0); // Zależy od implementacji
    }

    @Test
    void randomPlantNormalPlant() {
        int plantEnergy = 20;
        int poisonPlantProbability = 0;
        int poisonEnergy = 10;

        Plant plant = RandomGenerator.randomPlant(plantEnergy, poisonPlantProbability, poisonEnergy);

        assertNotNull(plant);
        assertEquals(plantEnergy, plant.getEnergy());
    }

    @Test
    void randomPlantPoisonPlant() {
        int plantEnergy = 20;
        int poisonPlantProbability = 100;
        int poisonEnergy = 10;

        Plant plant = RandomGenerator.randomPlant(plantEnergy, poisonPlantProbability, poisonEnergy);

        assertNotNull(plant);
        assertEquals(-poisonEnergy, plant.getEnergy());
    }

    @Test
    void randomMovingPointer() {
        int genomLength = 8;

        int pointer = RandomGenerator.randomMovingPointer(genomLength);

        assertTrue(pointer >= 0 && pointer < genomLength);
    }
}