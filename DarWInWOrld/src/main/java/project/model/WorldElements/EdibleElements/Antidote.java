package project.model.WorldElements.EdibleElements;

import project.model.Vector2D;
import project.model.WorldElements.Animal;

public final class Antidote extends Plant {
    private final TypeOfAntidote typeOfAntidote;

    public Antidote(Vector2D position, TypeOfAntidote type) {
        super(position, type.getEnergy());
        this.typeOfAntidote = type;
    }

    public Antidote(Vector2D position, int antidote) {
        TypeOfAntidote[] values = TypeOfAntidote.values();
        this(position, values[antidote]);
    }

    @Override
    public void changeAnimalEnergy(Animal animal) {
        animal.energyIncrease(getEnergy());
    }
}

