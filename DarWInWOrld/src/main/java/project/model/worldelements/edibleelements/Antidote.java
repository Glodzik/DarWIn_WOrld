package project.model.worldelements.edibleelements;

import project.model.coordinates.Vector2D;

public final class Antidote extends Plant {
    private final TypeOfAntidote typeOfAntidote;

    public Antidote(Vector2D position, TypeOfAntidote type) {
        super(position, type.getEnergy());
        this.typeOfAntidote = type;
    }

    public Antidote(Vector2D position, int antidote) {
        this(position, TypeOfAntidote.values()[antidote]);
    }

    public Antidote(Vector2D position, int antidoteIndex, int scaledEnergy) {
        super(position, scaledEnergy);
        this.typeOfAntidote = TypeOfAntidote.values()[antidoteIndex];
    }

    public TypeOfAntidote getType() {
        return typeOfAntidote;
    }

}

