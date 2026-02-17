package project.model.worldelements.edibleelements;

import project.model.coordinates.Vector2D;
import project.model.worldelements.WorldElement;

public class Plant implements WorldElement {
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
