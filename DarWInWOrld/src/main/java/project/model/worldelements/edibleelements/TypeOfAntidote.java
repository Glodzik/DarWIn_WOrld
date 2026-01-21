package project.model.worldelements.edibleelements;

public enum TypeOfAntidote {
    GRASS(5, "/textures/plants/antidotes/grass.png"),
    BAMBOO(10, "/textures/plants/antidotes/bamboo.png"),
    CARROT(15, "/textures/plants/antidotes/carrot.png"),
    PARSLEY(20, "/textures/plants/antidotes/parsley.png"),
    SUNFLOWER(30, "/textures/plants/antidotes/sunflower_front.png");

    private final int energy;
    private final String texturePath;

    TypeOfAntidote(int energy, String texturePath) {
        this.energy = energy;
        this.texturePath = texturePath;
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

    public String getTexturePath() {
        return texturePath;
    }
}
