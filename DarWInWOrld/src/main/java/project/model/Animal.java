package project.model;

import static project.model.GenomeGenerator.randomGenome;

public class Animal implements WorldElement {
    private MapDirection currDirection;
    private Vector2D position;

    // to do: losowy genom
    private final String genom = randomGenome(8);
    private int daysAlive = 0;

    // to do: zarzadzanie energia
    private int energy = 100;

    private final static Vector2D START_POSITION = new Vector2D(2, 2);

    public Animal(Vector2D position) {
        // to do: losowa pozycja startowa
        this.position = position;

        // to do: ma byc losowy kierunek
        this.currDirection = MapDirection.NORTH;
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


    public void move() {
        int rotation = Character.getNumericValue(genom.charAt(daysAlive % genom.length()));
        currDirection = currDirection.rotate(rotation);

        // to do: odbijanie od biegonow i przechodzenie z prawej na lewa/lewej na prawa strone mapy
        Vector2D newPosition = position.add(currDirection.toUnitVector());

        position = newPosition;
        daysAlive += 1;
    }

}
