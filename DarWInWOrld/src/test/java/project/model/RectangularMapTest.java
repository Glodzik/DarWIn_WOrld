package project.model;

import org.junit.jupiter.api.Test;
import project.model.worldElements.Animal;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {

    @Test
    void placeAnimalToMap() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();

        //when
        map.place(animal);

        //then
        assertEquals(animal, map.animalAt(animal.getPosition()));
        assertTrue(map.isOccupiedByAnimal(animal.getPosition()));
        assertTrue(map.getAnimals().contains(animal));
    }

    @Test
    void move() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();
        map.place(animal);
        Vector2D oldPosition = animal.getPosition();

        //when
        map.move(animal);

        //then
        assertFalse(map.isOccupiedByAnimal(oldPosition));
        assertTrue(map.isOccupiedByAnimal(animal.getPosition()));
    }

    @Test
    void isOccupied() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();
        map.place(animal);

        //when & then
        assertTrue(map.isOccupiedByAnimal(animal.getPosition()));
    }

    @Test
    void objectAt() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();
        map.place(animal);

        //when & then
        assertEquals(animal, map.animalAt(animal.getPosition()));
    }

    @Test
    void getElements() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal();
        Animal animal2 = new Animal(new Vector2D(1, 1));
        map.place(animal1);
        map.place(animal2);

        //when & then
        assertTrue(map.getAnimals().contains(animal1));
        assertTrue(map.getAnimals().contains(animal2));
    }
}