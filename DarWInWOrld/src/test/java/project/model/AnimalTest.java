package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Boundary;
import project.model.coordinates.Vector2D;
import project.model.map.MapDirection;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.animals.Genome;
import project.model.worldelements.edibleelements.Antidote;
import project.model.worldelements.edibleelements.Poison;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void getPosition() {
        //given
        Animal animal1 = new Animal(new Vector2D(2, 1));
        Animal animal2 = new Animal();

        //when
        Vector2D result1 = animal1.getPosition();
        Vector2D result2 = animal2.getPosition();

        //then
        assertEquals(new Vector2D(2, 1), result1);
        assertEquals(new Vector2D(0, 0), result2); //basic position
    }

    @Test
    void testToString() {
        //given
        Animal animal = new Animal();

        String expectedString = switch (animal.getCurrDirection()) {
            case NORTH -> "↑";
            case NORTH_EAST -> "↗";
            case EAST -> "→";
            case SOUTH_EAST -> "↘";
            case SOUTH -> "↓";
            case SOUTH_WEST -> "↙";
            case WEST -> "←";
            case NORTH_WEST -> "↖";
        };

        assertEquals(expectedString, animal.toString());
    }

    @Test
    void isAt() {
        //given
        Animal animal = new Animal(new Vector2D(6, 7));

        //when & then
        assertTrue(animal.isAt(new Vector2D(6, 7)));
        assertFalse(animal.isAt(new Vector2D(5, 5)));
    }

    @Test
    void testEatAntidotes() {
        Animal[] animals = new Animal[5];
        Antidote[] antidotes = new Antidote[5];
        int[] expectedAfterEating = new int[5];

        for(int i = 0; i < 5; i++) {
            animals[i] = new Animal();
            antidotes[i] = new Antidote(null, i);
            expectedAfterEating[i] = animals[i].getEnergy() + antidotes[i].getEnergy();
            animals[i].eat(antidotes[i]);
        }

        for(int i = 0; i < 5; i++) {
            assertEquals(expectedAfterEating[i], animals[i].getEnergy());
        }
    }

    @Test
    void testEatPoison() {
        Animal[] animals = new Animal[5];
        Poison[] poison = new Poison[5];
        int[] expectedAfterEating = new int[5];

        for(int i = 0; i < 5; i++) {
            animals[i] = new Animal();
            poison[i] = new Poison(null, i);
            expectedAfterEating[i] = animals[i].getEnergy() + poison[i].getEnergy();
            animals[i].eat(poison[i]);
        }

        for(int i = 0; i < 5; i++) {
            assertEquals(expectedAfterEating[i], animals[i].getEnergy());
        }
    }

    @Test
    void testConstructorWithParameters() {
        AnimalParameters parameters = new AnimalParameters(99, 10, 80, 40, 1, 5, 8);
        Animal animal = new Animal(parameters, new Genome(8));

        assertNotNull(animal);
        assertEquals(new Vector2D(0, 0), animal.getPosition());
        assertNotNull(animal.getCurrDirection());
        assertNotNull(animal.getGenom());
        assertEquals(99, animal.getEnergy());
        assertEquals(0, animal.getDaysAlive());
        assertEquals(0, animal.getChildrenCount());
    }

    @Test
    void testConstructorWithPosition() {
        Animal animal = new Animal(new Vector2D(2, 2));

        assertNotNull(animal);
        assertEquals(new Vector2D(2, 2), animal.getPosition());
        assertNotNull(animal.getCurrDirection());
        assertNotNull(animal.getGenom());
        assertEquals(100, animal.getEnergy());
        assertEquals(0, animal.getDaysAlive());
        assertEquals(0, animal.getChildrenCount());
    }

    @Test
    void testDefaultConstructor() {
        Animal animal = new Animal();

        assertEquals(new Vector2D(0, 0), animal.getPosition());
        assertNotNull(animal.getCurrDirection());
        assertNotNull(animal.getGenom());
        assertEquals(100, animal.getEnergy());
    }

    @Test
    void testBreedingConstructor() {
        AnimalParameters parameters = new AnimalParameters(200, 10, 80, 40, 1, 5, 8);
        Genome protectionGenome = new Genome(8);
        Animal parent1 = new Animal(parameters, protectionGenome);
        Animal parent2 = new Animal(parameters, protectionGenome);

        Animal child = new Animal(parent1, parent2, parameters, protectionGenome);

        assertNotNull(child);
        assertEquals(parent1.getPosition(), child.getPosition());
        assertEquals(80, child.getEnergy());
        assertEquals(0, child.getDaysAlive());
        assertEquals(0, child.getChildrenCount());
        assertEquals(160, parent1.getEnergy());
        assertEquals(160, parent2.getEnergy());
        assertEquals(1, parent1.getChildrenCount());
        assertEquals(1, parent2.getChildrenCount());
    }

    @Test
    void testSetPosition() {
        Animal animal = new Animal();
        Vector2D newPosition = new Vector2D(7, 3);
        animal.setPosition(newPosition);

        assertEquals(newPosition, animal.getPosition());
    }

    @Test
    void testEnergyLoss() {
        Animal animal = new Animal();
        int initialEnergy = animal.getEnergy();
        int lossAmount = 30;

        animal.energyLoss(lossAmount);

        assertEquals(initialEnergy - lossAmount, animal.getEnergy());
    }

    @Test
    void testIsDead() {
        Animal animal = new Animal();
        int energy = animal.getEnergy();
        animal.energyLoss(energy);

        assertTrue(animal.isDead());
        assertEquals(0, animal.getEnergy());
    }

    @Test
    void testTurnBack() {
        Animal animal = new Animal();
        MapDirection oppositeDirection = animal.getCurrDirection().opposite();

        animal.turnBack();
        assertEquals(oppositeDirection, animal.getCurrDirection());
    }

    @Test
    void testMoveWithinBounds() {
        Animal animal = new Animal();
        Vector2D initialPosition = new Vector2D(0, 0);
        animal.setPosition(initialPosition);


        Vector2D lowerLeft = new Vector2D(0, 0);
        Vector2D upperRight = new Vector2D(2, 2);
        Boundary bounds = new Boundary(lowerLeft, upperRight);

        int initialDaysAlive = animal.getDaysAlive();

        animal.move(bounds);

        assertEquals(initialDaysAlive + 1, animal.getDaysAlive());
        assertTrue(animal.getPosition().precedes(upperRight) && animal.getPosition().follows(lowerLeft));
    }

    @Test
    void testMoveWithNoEnergy() {
        Animal animal = new Animal(new Vector2D(1, 1));
        animal.energyLoss(animal.getEnergy());

        Vector2D initialPosition = animal.getPosition();

        Vector2D lowerLeft = new Vector2D(0, 0);
        Vector2D upperRight = new Vector2D(2, 2);
        Boundary bounds = new Boundary(lowerLeft, upperRight);
        animal.move(bounds);

        // Position and days alive should not change when energy is 0 or less
        assertEquals(initialPosition, animal.getPosition());
        assertEquals(0, animal.getDaysAlive());
    }
}