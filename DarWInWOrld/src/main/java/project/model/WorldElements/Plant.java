package project.model.WorldElements;

import project.model.Vector2D;

public abstract class Plant implements WorldElement {
    private final Vector2D position;
    private final int energy;

    protected Plant(Vector2D position, int energy) {
        this.position = position;
        this.energy = energy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public abstract void changeAnimalEnergy(Animal animal);
}
