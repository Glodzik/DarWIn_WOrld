package project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RectangularMap {
    private final Map<Vector2D, Animal> animals = new HashMap<>();
    private final static Vector2D LEFT_END = new Vector2D(0, 0);
    private final Vector2D rightEnd;

    public RectangularMap(int width, int height) {
        rightEnd = new Vector2D(width, height);
    }

    public Vector2D getRightEnd() {
        return rightEnd;
    }

    public void place(Animal animal) {
        animals.put(animal.getPosition(), animal);
    }

    public void move(Animal animal) {
        if (animals.get(animal.getPosition()) == animal) {
            Vector2D oldPosition = animal.getPosition();
            animals.remove(animal.getPosition(), animal);
            animal.move();
            Vector2D newPosition = animal.getPosition();

            if(!inBorder(newPosition)) {
                if (newPosition.aboveYLine(rightEnd.getY()) || newPosition.belowYLine(LEFT_END.getY())) {
                    // odbicie od biegunów
                    animal.turnBack();
                    animal.setPosition(oldPosition);
                    animals.put(oldPosition, animal);
                } else if (newPosition.onLeftXLine(LEFT_END.getX())) {
                    // przejście z lewej strony na prawą
                    Vector2D correctedPosition = new Vector2D(rightEnd.getX(), newPosition.getY());
                    animal.setPosition(correctedPosition);
                    animals.put(correctedPosition, animal);
                } else if (newPosition.onRightXLine(rightEnd.getX())) {
                    // przejście z prawej strony na lewą
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
