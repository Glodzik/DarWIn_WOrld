package project.model;

import org.junit.jupiter.api.Test;
import project.model.Coordinates.Vector2D;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.Animals.Genome;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Poison;

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

        //when & then
        //assertEquals("↑", animal.toString());
        //do dokonczenia - badanie wszystkich kierunków
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
        AnimalParameters parameters = new AnimalParameters(100, 10, 80, 40, 1, 5, 8);
        Animal animal = new Animal(parameters, new Genome(8));

        assertNotNull(animal);
        assertEquals(new Vector2D(0, 0), animal.getPosition());
        assertNotNull(animal.getCurrDirection());
        assertNotNull(animal.getGenom());
        assertEquals(100, animal.getEnergy());
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
}