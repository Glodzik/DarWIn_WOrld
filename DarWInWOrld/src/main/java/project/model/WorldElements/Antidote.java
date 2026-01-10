package project.model.WorldElements;

import project.model.Vector2D;

public class Antidote extends Plant {
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

