package project.model.WorldElements.EdibleElements;

import project.model.Vector2D;
import project.model.WorldElements.Animal;

public final class Antidote extends Plant {
    private final TypeOfAntidote typeOfAntidote;

    protected Antidote(Vector2D position, TypeOfAntidote typeOfAntidote) {
        super(position, typeOfAntidote.getEnergy());
        this.typeOfAntidote = typeOfAntidote;
    }

    @Override
    public void changeAnimalEnergy(Animal animal) {
        animal.energyIncrease(getEnergy());
    }
}

