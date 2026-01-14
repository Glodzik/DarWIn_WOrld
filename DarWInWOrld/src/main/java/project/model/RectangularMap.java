package project.model;

import project.model.WorldElements.Animal;
import project.model.WorldElements.Plant;
import project.model.WorldElements.WorldElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RectangularMap {
    private final Map<Vector2D, Animal> animals = new HashMap<>();
    private Map<Vector2D, Plant> plants = new HashMap<>();
    private final static Vector2D LEFT_END = new Vector2D(0, 0);
    private final Vector2D rightEnd;
    private final int equatorHeight;
    private ArrayList<Vector2D> jungle;
    private final int jungleHeigth;

    public RectangularMap(int width, int height) {
        rightEnd = new Vector2D(width, height);
        jungleHeigth = height / 5;
        if (height % 2 != 0) {
            equatorHeight = height / 2 + 1;
        } else {
            equatorHeight = height / 2;
        }
        for (int i = 0; i < width; i++) {
            this.jungle.add(new Vector2D(i, equatorHeight));
        }
    }

    public Vector2D getRightEnd() {
        return rightEnd;
    }

    public void place(Animal animal) {
        Vector2D position = RandomGenerator.randomPositionWithinBounds(LEFT_END, rightEnd);
        animal.setPosition(position);
        animals.put(position, animal);
    }

    public void move(Animal animal) {
        if (animals.get(animal.getPosition()) == animal) {
            Vector2D oldPosition = animal.getPosition();
            animals.remove(animal.getPosition(), animal);
            animal.move();
            Vector2D newPosition = animal.getPosition();

            if(!inBorder(newPosition)) {
                if (newPosition.aboveYLine(rightEnd.getY()) || newPosition.belowYLine(LEFT_END.getY())) {
                    // turning back from the poles
                    animal.turnBack();
                    animal.setPosition(oldPosition);
                    animals.put(oldPosition, animal);
                } else if (newPosition.onLeftXLine(LEFT_END.getX())) {
                    // transition from left to right
                    Vector2D correctedPosition = new Vector2D(rightEnd.getX(), newPosition.getY());
                    animal.setPosition(correctedPosition);
                    animals.put(correctedPosition, animal);
                } else if (newPosition.onRightXLine(rightEnd.getX())) {
                    // transition from right to left
                    Vector2D correctedPosition = new Vector2D(LEFT_END.getX(), newPosition.getY());
                    animal.setPosition(correctedPosition);
                    animals.put(correctedPosition, animal);
                }
            } else {
                animals.put(newPosition, animal);
            }
        }
    }

    public boolean isOccupied(Vector2D position) {
        return objectAt(position) != null;
    }

    public WorldElement objectAt(Vector2D position) {
        // to do: do zmiany po dodaniu grass
        return animals.get(position);
    }

    public List<WorldElement> getElements() {
        // to do: do zmiany po dodaniu grass
        return new ArrayList<>(animals.values());
    }

    private boolean inBorder(Vector2D position) {
        return (position.precedes(rightEnd) && position.follows(LEFT_END));
    }

    public boolean canMoveTo(Vector2D position) {
        return inBorder(position) && !isOccupied(position);
    }
}
