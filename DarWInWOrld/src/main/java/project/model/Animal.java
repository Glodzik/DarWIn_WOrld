package project.model;

import static project.model.GenomeGenerator.randomGenome;

public class Animal implements WorldElement {
    private MapDirection currDirection;
    private Vector2D position;
    private final String genom;
    private int daysAlive = 0;
    private int energy;

    private static final Vector2D START_POSITION = new Vector2D(2, 2);
    private static final int START_ENERGY = 100;

    public Animal(Vector2D position, int energy) {
        this.position = position;
        // to do: zarządzanie energia, parametr - wartość energii startowej
        this.energy = energy;
        // to do: parametr - długość genomu
        this.genom = randomGenome(8);
        // to do: losowy kierunek
        this.currDirection = MapDirection.NORTH;
    }

    public Animal(Vector2D position) {
        this(position, START_ENERGY);
    }

    public Animal() {
        this(START_POSITION);
    }

    public MapDirection getCurrDirection() {
        return currDirection;
    }

    public Vector2D getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return switch (this.currDirection) {
            case NORTH -> "↑";
            case NORTH_EAST -> "↗";
            case EAST -> "→";
            case SOUTH_EAST -> "↘";
            case SOUTH -> "↓";
            case SOUTH_WEST -> "↙";
            case WEST -> "←";
            case NORTH_WEST -> "↖";
        };
    }

    public boolean isAt(Vector2D position) {
        return this.position.equals(position);
    }

    public void turnBack() {
        currDirection = currDirection.opposite();
    }

    public void move() {
        int rotation = Character.getNumericValue(genom.charAt(daysAlive % genom.length()));
        currDirection = currDirection.rotate(rotation);
        position = position.add(currDirection.toUnitVector());
        daysAlive += 1;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
}
