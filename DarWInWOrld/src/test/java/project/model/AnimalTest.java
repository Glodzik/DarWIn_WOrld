package project.model;

import org.junit.jupiter.api.Test;
import project.model.WorldElements.Animal;

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
        assertEquals(result1, new Vector2D(2, 1));
        assertEquals(result2, new Vector2D(2, 2)); //basic position
    }

    @Test
    void testToString() {
        //given
        Animal animal = new Animal();

        //when & then
        assertEquals("↑", animal.toString());
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

}