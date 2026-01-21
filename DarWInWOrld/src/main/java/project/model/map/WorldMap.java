package project.model.map;

import project.model.coordinates.Boundary;
import project.model.coordinates.Vector2D;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.edibleelements.Plant;
import project.presenter.MapChangeListener;

import java.util.List;

public interface WorldMap {

    // Operacje na zwierzętach
    void place(Animal animal);                      // Umieszcza zwierzę w losowej pozycji

    void place(Animal animal, Vector2D position);   // Umieszcza zwierzę w określonej pozycji

    void move(Animal animal);                       // Przesuwa zwierzę zgodnie z genomem

    void removeDeadAnimals();                       // Usuwa martwe zwierzęta

    // Operacje na roślinach
    void placePlant(Plant plant);                   // Umieszcza roślinę

    void eatIfPossible(Animal animal);              // Zjada roślinę jeśli jest na pozycji

    // Zapytania o stan mapy
    List<Animal> getAnimalsAt(Vector2D position);   // Zwierzęta na pozycji

    Plant getPlantAt(Vector2D position);            // Roślina na pozycji

    boolean isOccupiedByPlant(Vector2D position);   // Czy jest roślina?

    boolean isOccupiedByAnimal(Vector2D position);  // Czy jest zwierzę?

    // Granice mapy
    Boundary getMapBounds();                        // Granice całej mapy

    Boundary getJungleBoundary();                   // Granice dżungli

    // Kolekcje
    List<Animal> getAnimals();                      // Wszystkie żywe zwierzęta

    List<Plant> getPlants();                        // Wszystkie rośliny

    List<Vector2D> getAllPositions();               // Pozycje ze zwierzętami

    // Statystyki
    int countFreeFields();                          // Liczba wolnych pól

    // Observer pattern
    void addObserver(MapChangeListener observer);   // Dodaje obserwatora zmian
}