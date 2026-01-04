package project.model;

import java.util.Objects;

public class Vector2D {
    private final int x;
    private final int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean precedes(Vector2D other) {
        return x <= other.getX() && y <= other.getY();
    }

    public boolean follows(Vector2D other) {
        return x >= other.getX() && y >= other.getY();
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.getX(), y + other.getY());
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.getX(), y - other.getY());
    }

    public Vector2D upperRight(Vector2D other) {
        return new Vector2D( Math.max(x, other.getX()), Math.max(y, other.getY()) );
    }

    public Vector2D lowerLeft(Vector2D other) {
        return new Vector2D( Math.min(x, other.getX()), Math.min(y, other.getY()) );
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2D that))
            return false;

        return x == that.getX() && y == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}