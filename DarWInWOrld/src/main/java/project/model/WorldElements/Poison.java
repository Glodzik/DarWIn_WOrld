package project.model.WorldElements;

import project.model.Vector2D;

public final class Poison extends Plant {
    private String protection;
    private final TypeOfPoison typeOfPoison;

    protected Poison(Vector2D position, TypeOfPoison typeOfPoison) {
        super(position, typeOfPoison.getEnergy());
        this.typeOfPoison = typeOfPoison;
        //this.protection - losowanie części genomu który umożliwia zwierzakom odporność
    }

    @Override
    public void changeAnimalEnergy(Animal animal) {
        animal.energyLoss(getEnergy());
    }

}
