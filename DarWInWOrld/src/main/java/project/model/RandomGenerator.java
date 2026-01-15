package project.model;

import java.util.Random;


public final class RandomGenerator {
    public static Vector2D randomPositionWithinBounds(Boundary bounds) {
        Random random = new Random();
        int x1 = bounds.lowerLeft().getX(), x2 = bounds.upperRight().getY();
        int y1 = bounds.lowerLeft().getY(), y2 = bounds.lowerLeft().getY();
        int randomX = random.nextInt(x2 - x1 + 1) + x1;
        int randomY = random.nextInt(y2 - y1 + 1) + y1;
        return new Vector2D(randomX, randomY);
    }

    public static MapDirection randomDirection() {
        Random random = new Random();
        MapDirection[] directions = MapDirection.values();
        int direction = random.nextInt(directions.length);
        return directions[direction];
    }
}
