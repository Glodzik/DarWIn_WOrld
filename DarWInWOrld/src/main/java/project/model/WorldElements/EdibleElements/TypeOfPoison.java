package project.model.WorldElements.EdibleElements;

public enum TypeOfPoison {
    LILY(-5),
    ALOE(-10),
    AZALEA(-15),
    OLEANDER(-20),
    COMMON_YEW(-30);

    private final int energy;

    TypeOfPoison(int energy) {
        this.energy = energy;
    }

    @Override
    public String toString() {
        return switch (this) {
            case LILY -> "LI";
            case ALOE -> "AL";
            case AZALEA -> "AZ";
            case OLEANDER -> "OL";
            case COMMON_YEW -> "CY";
        };
    }

    public int getEnergy() {
        return energy;
    }
}
