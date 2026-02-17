package project.model.worldelements.edibleelements;

import project.model.coordinates.Vector2D;

public final class Poison extends Plant {
    private final TypeOfPoison typeOfPoison;

    public Poison(Vector2D position, TypeOfPoison typeOfPoison) {
        super(position, typeOfPoison.getEnergy());
        this.typeOfPoison = typeOfPoison;
    }

    public Poison(Vector2D position, int poison) {
        this(position, TypeOfPoison.values()[poison]);
    }

    public Poison(Vector2D position, int poisonIndex, int scaledEnergy) {
        super(position, -scaledEnergy);
        this.typeOfPoison = TypeOfPoison.values()[poisonIndex];
    }

    public TypeOfPoison getType() {
        return typeOfPoison;
    }
}
