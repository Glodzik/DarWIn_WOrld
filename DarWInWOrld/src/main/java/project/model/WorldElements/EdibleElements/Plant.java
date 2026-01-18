package project.model.WorldElements.EdibleElements;

import project.model.Coordinates.Vector2D;
import project.model.WorldElements.WorldElement;

public class Plant implements WorldElement, Edible {
    private Vector2D position;
    private final int energy;

    public Plant(Vector2D position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }
}
