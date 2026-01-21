package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Boundary;
import project.model.coordinates.Vector2D;
import project.model.map.RectangularMap;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.edibleelements.Plant;

import java.util.List;

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
        assertTrue(map.isOccupiedByAnimal(animal.getPosition()));
        assertTrue(map.getAnimals().contains(animal));
    }

    @Test
    void move() {
        //given
        RectangularMap map = new RectangularMap(5, 5);
        Vector2D position = new Vector2D(2, 2);
        Animal animal = new Animal(position);
        map.place(animal, position);
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

    @Test
    void testConstructor() {
        RectangularMap map1 = new RectangularMap(10, 10);

        Boundary mapBounds = map1.getMapBounds();
        assertNotNull(mapBounds);
        assertEquals(0, mapBounds.lowerLeft().getX());
        assertEquals(0, mapBounds.lowerLeft().getY());
        assertEquals(9, mapBounds.upperRight().getX());
        assertEquals(9, mapBounds.upperRight().getY());

        Boundary jungleBounds = map1.getJungleBoundary();
        assertNotNull(jungleBounds);
        assertTrue(jungleBounds.upperRight().getY() > jungleBounds.lowerLeft().getY());
    }

    @Test
    void testPlaceAnimalRandomPosition() {
        Vector2D lowerLeft = new Vector2D(0,0);
        Vector2D upperRight = new Vector2D(4,4);
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal();

        map.place(animal);

        List<Animal> animals = map.getAnimals();
        assertTrue(animals.contains(animal));
        assertTrue(animal.getPosition().precedes(upperRight) && animal.getPosition().follows(lowerLeft));
    }

    @Test
    void testPlaceAnimalAtSpecifiedPosition() {
        Vector2D lowerLeft = new Vector2D(0,0);
        Vector2D upperRight = new Vector2D(4,4);
        RectangularMap map = new RectangularMap(5, 5);
        Vector2D position = new Vector2D(2, 2);
        Animal animal = new Animal(position);

        map.place(animal, position);

        List<Animal> animals = map.getAnimals();
        assertTrue(animals.contains(animal));
        assertTrue(animal.getPosition().precedes(upperRight) && animal.getPosition().follows(lowerLeft));
        assertEquals(position, animal.getPosition());
    }

    @Test
    void testPlaceMultipleAnimalsAtSamePosition() {
        Vector2D position = new Vector2D(3, 3);
        Animal animal1 = new Animal(position);
        Animal animal2 = new Animal(position);
        Animal animal3 = new Animal(position);
        RectangularMap map = new RectangularMap(5, 5);

        map.place(animal1, position);
        map.place(animal2, position);
        map.place(animal3, position);

        List<Animal> animalsAtPosition = map.getAnimalsAt(position);
        assertEquals(3, animalsAtPosition.size());
        assertTrue(animalsAtPosition.contains(animal1));
        assertTrue(animalsAtPosition.contains(animal2));
        assertTrue(animalsAtPosition.contains(animal3));
    }

    @Test
    void testPlacePlant() {
        Plant plant = new Plant(null, 10); // Zakładam, że Plant ma konstruktor z energią
        RectangularMap map = new RectangularMap(2, 2);

        map.placePlant(plant);
        assertNotNull(plant.getPosition());

        Vector2D plantPosition = plant.getPosition();
        assertTrue(map.isOccupiedByPlant(plantPosition));
        assertEquals(plant, map.getPlantAt(plantPosition));

        List<Plant> plants = map.getPlants();
        assertTrue(plants.contains(plant));
    }

    @Test
    void testCountFreeFields() {
        RectangularMap map = new RectangularMap(5, 5);
        int fieldsWithNothingOnMap = map.countFreeFields();

        map.place(new Animal());
        int fieldsWithAnimalOnMap = map.countFreeFields();

        assertEquals(25, fieldsWithNothingOnMap);
        assertEquals(24, fieldsWithAnimalOnMap);
    }
}