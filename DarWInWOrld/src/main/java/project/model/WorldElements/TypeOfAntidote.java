package project.model.WorldElements;

public enum TypeOfAntidote {
    GRASS(5),
    BAMBOO(10),
    CARROT(15),
    PARSLEY(20),
    SUNFLOWER(30);

    private final int energy;

    TypeOfAntidote(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return switch (this) {
            case GRASS -> "GR";
            case BAMBOO -> "BA";
            case CARROT -> "CA";
            case PARSLEY -> "PA";
            case SUNFLOWER -> "SU";
        };
    }

    public int getEnergy() {
        return energy;
    }
}
