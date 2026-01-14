package project.model.worldElements.EdibleElements;

import project.model.Vector2D;
import project.model.worldElements.Animal;

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

    @Override
    public void changeAnimalEnergy(Animal animal) {
        animal.energyLoss(getEnergy());
    }

}
