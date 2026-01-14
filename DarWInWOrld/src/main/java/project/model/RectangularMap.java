package project.model;

import project.model.WorldElements.Animal;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.WorldElement;

import java.util.*;


public final class RectangularMap {
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private Map<Vector2D, Plant> plants = new HashMap<>();

    private final static Vector2D LEFT_END = new Vector2D(0, 0);
    private final Vector2D rightEnd;
    private final Vector2D jungleLeftEnd;
    private final Vector2D jungleRightEnd;
    private final int jungleHeight;

    public RectangularMap(int width, int height) {
        this.rightEnd = new Vector2D(width, height);

        this.jungleHeight = Math.max((height + 1) / 5, 1);
        int equatorHeight = ((height + 1) / 2);
        this.jungleLeftEnd = new Vector2D(0, equatorHeight);
        this.jungleRightEnd = new Vector2D(rightEnd.getX(), equatorHeight + jungleHeight - 1);
    }

    public Vector2D getRightEnd() {
        return rightEnd;
    }

    public void place(Animal animal) {
        Vector2D position = RandomGenerator.randomPositionWithinBounds(LEFT_END, rightEnd);
        animal.setPosition(position);
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
    }

    public void placePlant(Plant plant) {
        Random random = new Random();
        Vector2D vectorToPlacePlant;
        if (random.nextDouble() < 0.8) {
            // plant in the jungle
            vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(jungleLeftEnd, jungleRightEnd);
        } else {
            // plant out of jungle
            if(random.nextDouble() < 0.5) {
                // plant to the north of the jungle
                Vector2D northLeftEnd = jungleLeftEnd.add(new Vector2D(0, jungleHeight));
                vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(northLeftEnd, rightEnd);
            } else {
                // plant to the south of the jungle
                Vector2D southRightEnd = jungleRightEnd.subtract(new Vector2D(0, jungleHeight));
                vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(LEFT_END, southRightEnd);
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
        animal.move();
        Vector2D newPosition = animal.getPosition();

        if (animalsAtPosition.isEmpty()) {
            animals.remove(oldPosition);
        }

        if(!inBorder(newPosition)) {
            if (newPosition.aboveYLine(rightEnd.getY()) || newPosition.belowYLine(LEFT_END.getY())) {
                // turning back from the poles
                animal.turnBack();
                animal.setPosition(oldPosition);
                animals.computeIfAbsent(oldPosition, k -> new ArrayList<>()).add(animal);
            } else if (newPosition.onLeftXLine(LEFT_END.getX())) {
                // transition from left to right
                Vector2D correctedPosition = new Vector2D(rightEnd.getX(), newPosition.getY());
                animal.setPosition(correctedPosition);
                animals.computeIfAbsent(correctedPosition, k -> new ArrayList<>()).add(animal);
            } else if (newPosition.onRightXLine(rightEnd.getX())) {
                // transition from right to left
                Vector2D correctedPosition = new Vector2D(LEFT_END.getX(), newPosition.getY());
                animal.setPosition(correctedPosition);
                animals.computeIfAbsent(correctedPosition, k -> new ArrayList<>()).add(animal);
            }
        } else {
            animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
        }
    }

    public List<Animal> getAnimalsAt(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return animalsAtPosition != null ? new ArrayList<>(animalsAtPosition) : new ArrayList<>();
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

    public WorldElement animalAt(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return (animalsAtPosition != null && !animalsAtPosition.isEmpty())
                ? animalsAtPosition.get(0)
                : null;

        // to do: sortowanie zwierzaków według kryteriów
    }

    public List<WorldElement> getAnimals() {
        List<WorldElement> allAnimals = new ArrayList<>();
        for (List<Animal> animalList : animals.values()) {
            allAnimals.addAll(animalList);
        }
        return allAnimals;
    }

    public List<WorldElement> getPlants() {
        return new ArrayList<>(plants.values());
    }

    private boolean inBorder(Vector2D position) {
        return (position.precedes(rightEnd) && position.follows(LEFT_END));
    }
}
