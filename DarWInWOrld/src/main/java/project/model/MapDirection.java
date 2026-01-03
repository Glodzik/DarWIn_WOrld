package project.model;

public enum MapDirection {
    NORTH(0),
    NORTH_EAST(1),
    EAST(2),
    SOUTH_EAST(3),
    SOUTH(4),
    SOUTH_WEST(5),
    WEST(6),
    NORTH_WEST(7);

    private final int order;

    MapDirection(int order) {
        this.order = order;
    }

    private static final MapDirection[] DIRECTIONS = values();

    @Override
    public String toString(){
        return switch(this) {
            case NORTH -> "North";
            case NORTH_EAST -> "NorthEast";
            case EAST -> "East";
            case SOUTH_EAST -> "SouthEast";
            case SOUTH -> "South";
            case SOUTH_WEST -> "SouthWest";
            case WEST -> "West";
            case NORTH_WEST -> "NorthWest";
        };
    }

    public Vector2D toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2D(0, 1);
            case NORTH_EAST -> new Vector2D(1, 1);
            case EAST -> new Vector2D(0, 1);
            case SOUTH_EAST -> new Vector2D(1, -1);
            case SOUTH -> new Vector2D(0, -1);
            case SOUTH_WEST -> new Vector2D(-1, -1);
            case WEST -> new Vector2D(-1, 0);
            case NORTH_WEST -> new Vector2D(-1, 1);
        };
    }

    // step - values from 0 to 7 (0 - 45 degrees, 7 - 315 degrees)
    public MapDirection rotate(int step) {
        if(step >= 0 && step <= 7) return DIRECTIONS[(this.order +  step) % DIRECTIONS.length];
        return this;
    }

    public MapDirection opposite() {
        return rotate(4);
    }

}
