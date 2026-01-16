package project.model;

import project.model.WorldElements.Animal;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.WorldElement;

import java.util.*;


public final class RectangularMap {
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final int jungleHeight;
    private final Boundary mapBounds;
    private final Boundary jungleBoundary;

    private final static Vector2D LOWER_LEFT = new Vector2D(0, 0);

    public RectangularMap(int width, int height) {
        this.mapBounds = new Boundary(LOWER_LEFT, new Vector2D(width, height));
        this.jungleHeight = Math.max((height + 1) / 5, 1);
        int equatorHeight = ((height + 1) / 2);
        Vector2D jungleLowerLeft = new Vector2D(0, equatorHeight);
        Vector2D jungleUpperRight = new Vector2D(mapBounds.upperRight().getX(), equatorHeight + jungleHeight - 1);
        this.jungleBoundary = new Boundary(jungleLowerLeft, jungleUpperRight);
    }

    public void place(Animal animal) {
        Vector2D position = RandomGenerator.randomPositionWithinBounds(mapBounds);
        animal.setPosition(position);
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
    }

    public void placePlant(Plant plant) {
        Random random = new Random();
        Vector2D vectorToPlacePlant;
        if (random.nextDouble() < 0.8) {
            // plant in the jungle
            vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(jungleBoundary);
        } else {
            // plant out of jungle
            if(random.nextDouble() < 0.5) {
                // plant to the north of the jungle
                Vector2D northLeftEnd = jungleBoundary.lowerLeft().add(new Vector2D(0, jungleHeight));
                vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds
                        (new Boundary(northLeftEnd, mapBounds.upperRight()));
            } else {
                // plant to the south of the jungle
                Vector2D southRightEnd = jungleBoundary.upperRight().subtract(new Vector2D(0, jungleHeight));
                vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(new Boundary(mapBounds.lowerLeft(), southRightEnd));
            }

        }

        if(!isOccupiedByPlant(vectorToPlacePlant)) {
            plants.put(vectorToPlacePlant, plant);
            plant.setPosition(vectorToPlacePlant);
        }
    }

    public void move(Animal animal) {
        Vector2D oldPosition = animal.getPosition();
        List<Animal> animalsAtPosition = animals.get(oldPosition);

        animalsAtPosition.remove(animal);
        animal.move(this);

        if (animalsAtPosition.isEmpty()) {
            animals.remove(oldPosition);
        }

        Vector2D newPosition = animal.getPosition();
        animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
    }

    public List<Animal> getAnimalsAt(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return animalsAtPosition != null ? new ArrayList<>(animalsAtPosition) : new ArrayList<>();
    }

    public void eatIfPossible(Animal animal) {
        Vector2D position = animal.getPosition();
        if(isOccupiedByPlant(position)) {
            animal.eat(plants.get(position));
            plants.remove(position);
        }
    }

    public boolean isOccupiedByPlant(Vector2D position) {
        return plantAt(position) != null;
    }

    public boolean isOccupiedByAnimal(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return animalsAtPosition != null && !animalsAtPosition.isEmpty();
    }

    public WorldElement plantAt(Vector2D position) {
        return plants.get(position);
    }

    public List<WorldElement> getAnimals() {
        List<WorldElement> allAnimals = new ArrayList<>();
        for (List<Animal> animalList : animals.values()) {
            allAnimals.addAll(animalList);
        }
        return allAnimals;
    }

    public Boundary getMapBounds() {
        return mapBounds;
    }

    public List<WorldElement> getPlants() {
        return new ArrayList<>(plants.values());
    }

    public boolean inBorder(Vector2D position) {
        return (position.precedes(mapBounds.upperRight()) && position.follows(LOWER_LEFT));
    }
}
