package project.model.worldelements.edibleelements;

public enum TypeOfPoison {
    LILY(-5, "/textures/plants/poisons/lily.png"),
    ALOE(-10, "/textures/plants/poisons/aloe.png"),
    AZALEA(-15, "/textures/plants/poisons/azalea.png"),
    OLEANDER(-20, "/textures/plants/poisons/oleander.png"),
    COMMON_YEW(-30, "/textures/plants/poisons/commonYew.png"),
    ;
    private final int energy;
    private final String texturePath;


    TypeOfPoison(int energy, String texturePath) {
        this.energy = energy;
        this.texturePath = texturePath;
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

    public String getTexturePath() {
        return texturePath;
    }
}
