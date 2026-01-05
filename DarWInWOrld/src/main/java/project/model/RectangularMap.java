package project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RectangularMap implements MoveValidator {
    protected final Map<Vector2D, Animal> animals = new HashMap<>();
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
            animals.remove(animal.getPosition(), animal);
            animal.move();
            animals.put(animal.getPosition(), animal);
        }
    }

    public boolean isOccupied(Vector2D position) {
        return objectAt(position) != null;
    }

    public WorldElement objectAt(Vector2D position) {
        return animals.get(position);
    }

    public List<WorldElement> getElements() {
        return new ArrayList<>(animals.values());
    }

    private boolean inBorder(Vector2D position) {
        return (position.getX() >= LEFT_END.getX() && position.getX() < rightEnd.getX()
                && position.getY() >= LEFT_END.getY() && position.getY() < rightEnd.getY());
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        if (!inBorder(position) || isOccupied(position)) {
            return false;
        }
        return true;
    }
}
