package project.model.WorldElements.EdibleElements;

import project.model.Coordinates.Vector2D;

public final class Poison extends Plant {
    private final TypeOfPoison typeOfPoison;

    public Poison(Vector2D position, TypeOfPoison typeOfPoison) {
        super(position, typeOfPoison.getEnergy());
        this.typeOfPoison = typeOfPoison;
    }

    public Poison(Vector2D position, int poison) {
        TypeOfPoison[] values = TypeOfPoison.values();
        this(position, values[poison]);
    }

    public TypeOfPoison getType() {
        return typeOfPoison;
    }
}
