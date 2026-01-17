package project.model;

import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalComparator;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.WorldElement;

import java.util.*;


public final class RectangularMap {
    private final Map<Vector2D, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2D, Plant> plants = new HashMap<>();
    private final Boundary mapBounds;
    private final Boundary jungleBoundary;
    private final int jungleHeight;
    private final List<MapChangeListener> observers = new ArrayList<>();

    private final static Vector2D LOWER_LEFT = new Vector2D(0, 0);

    public RectangularMap(int width, int height) {
        this.mapBounds = new Boundary(LOWER_LEFT, new Vector2D(width - 1, height - 1));
        this.jungleHeight = Math.max((int) Math.round(height * 0.2), 1);
        int jungleStartY = (int) Math.round(((height - 1) / 2.0) - (jungleHeight - 1) / 2.0);
        int jungleEndY = jungleStartY + jungleHeight - 1;
        Vector2D jungleLowerLeft = new Vector2D(0, jungleStartY);
        Vector2D jungleUpperRight = new Vector2D(width - 1, jungleEndY);
        this.jungleBoundary = new Boundary(jungleLowerLeft, jungleUpperRight);
    }

    public void place(Animal animal) {
        Vector2D position = RandomGenerator.randomPositionWithinBounds(mapBounds);
        animal.setPosition(position);
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
        mapChanged("Animal placed at random position: %s".formatted(animal.getPosition()));
    }

    public void place(Animal animal, Vector2D position) {
        animals.computeIfAbsent(position, k -> new ArrayList<>()).add(animal);
        mapChanged("Animal placed at specified position: %s".formatted(animal.getPosition()));
    }

    public void placePlant(Plant plant) {
        Vector2D vectorToPlacePlant;
        if (RandomGenerator.probability() < 0.8) {
            // plant in the jungle
            vectorToPlacePlant = RandomGenerator.randomPositionWithinBounds(jungleBoundary);
        } else {
            // plant out of jungle
            if(RandomGenerator.probability() < 0.5) {
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
            mapChanged("Plant placed at position: %s".formatted(vectorToPlacePlant));
        }
    }

    public void move(Animal animal) {
        Vector2D oldPosition = animal.getPosition();
        List<Animal> animalsAtPosition = animals.get(oldPosition);

        animalsAtPosition.remove(animal);
        animal.move(this.getMapBounds());

        if (animalsAtPosition.isEmpty()) {
            animals.remove(oldPosition);
        }

        Vector2D newPosition = animal.getPosition();
        animals.computeIfAbsent(newPosition, k -> new ArrayList<>()).add(animal);
        mapChanged("Animal moved from %s to %s".formatted(oldPosition, newPosition));
    }

    public Boundary getMapBounds() {
        return mapBounds;
    }

    public List<Animal> getAnimalsAt(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return animalsAtPosition != null ? new ArrayList<>(animalsAtPosition) : new ArrayList<>();
    }

    public WorldElement getPlantAt(Vector2D position) {
        return plants.get(position);
    }

    public void eatIfPossible(Animal animal) {
        Vector2D position = animal.getPosition();
        if(isOccupiedByPlant(position)) {
            animal.eat(plants.get(position));
            plants.remove(position);
        }
    }

    public boolean isOccupiedByPlant(Vector2D position) {
        return getPlantAt(position) != null;
    }

    public boolean isOccupiedByAnimal(Vector2D position) {
        List<Animal> animalsAtPosition = animals.get(position);
        return animalsAtPosition != null && !animalsAtPosition.isEmpty();
    }

    public void removeDeadAnimals() {
        Iterator<Map.Entry<Vector2D, List<Animal>>> iterator = animals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2D, List<Animal>> entry = iterator.next();
            List<Animal> animalsAtPosition = entry.getValue();

            animalsAtPosition.removeIf(Animal::isDead);

            if (animalsAtPosition.isEmpty()) {
                iterator.remove();
            }
        }
    }

    // unmodifiable lists
    public List<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }

    public List<Animal> getAnimals() {
        List<Animal> allAnimals = new ArrayList<>();
        for (List<Animal> animalList : animals.values()) {
            allAnimals.addAll(animalList);
        }
        return allAnimals;
    }

    public List<Vector2D> getAllPositions() {
        return new ArrayList<>(animals.keySet());
    }

    public int countFreeFields() {
        int totalFields = (mapBounds.upperRight().getX() + 1) * (mapBounds.upperRight().getY() + 1);

        Set<Vector2D> occupiedFields = new HashSet<>();
        occupiedFields.addAll(animals.keySet());
        occupiedFields.addAll(plants.keySet());

        return totalFields - occupiedFields.size();
    }

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public WorldElement objectAt(Vector2D position) {
        List<Animal> animalsAtPosition = getAnimalsAt(position);
        WorldElement worldElement = null;
        if(!animalsAtPosition.isEmpty()) {
            animalsAtPosition.sort(AnimalComparator.createComparator());
            worldElement = animalsAtPosition.getFirst();
        }

        if(worldElement == null) {
            worldElement = getPlantAt(position);
        }

        return worldElement;
    }
}
