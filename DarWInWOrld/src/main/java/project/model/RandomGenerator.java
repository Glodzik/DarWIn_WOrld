package project.model;

import java.util.Random;


public final class RandomGenerator {
    public static String randomGenome(int n) {
        StringBuilder genome = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            genome.append(random.nextInt(8));
        }

        return genome.toString();
    }

    public static Vector2D randomPositionWithinBounds(Vector2D leftEnd, Vector2D rightEnd) {
        Random random = new Random();
        int x1 = leftEnd.getX(), x2 = rightEnd.getX();
        int y1 = leftEnd.getY(), y2 = rightEnd.getY();
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
