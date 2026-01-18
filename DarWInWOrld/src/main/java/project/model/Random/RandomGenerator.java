package project.model.Random;

import project.model.Coordinates.Boundary;
import project.model.Coordinates.Vector2D;
import project.model.Map.MapDirection;
import project.model.WorldElements.EdibleElements.*;

import java.util.Random;


public final class RandomGenerator {
    private final static Random random = new Random();

    public static Vector2D randomPositionWithinBounds(Boundary bounds) {
        int x1 = bounds.lowerLeft().getX(), x2 = bounds.upperRight().getX();
        int y1 = bounds.lowerLeft().getY(), y2 = bounds.upperRight().getY();
        int randomX = random.nextInt(x2 - x1 + 1) + x1;
        int randomY = random.nextInt(y2 - y1 + 1) + y1;
        return new Vector2D(randomX, randomY);
    }

    public static MapDirection randomDirection() {
        MapDirection[] directions = MapDirection.values();
        int direction = random.nextInt(directions.length);
        return directions[direction];
    }

    public static double probability() {
        return random.nextDouble();
    }

    public static boolean TrueOrFalse() {
        return random.nextBoolean();
    }

    public static Plant randomCustomPlants(int poisonPlantProbability) {
        int probability = random.nextInt(101);
        if(probability <= poisonPlantProbability) {
            int poison = random.nextInt(TypeOfPoison.values().length);
            return new Poison(null, poison);
        } else {
            int antidote = random.nextInt(TypeOfAntidote.values().length);
            return new Antidote(null, antidote);
        }
    }

    public static Plant randomPlant(int plantEnergy, int poisonPlantProbability, int poisonEnergy) {
        int probability = random.nextInt(101);
        if(poisonPlantProbability != 0 && probability < poisonPlantProbability) {
            return new Plant(null, -poisonEnergy);
        } else {
            return new Plant(null, plantEnergy);
        }
    }
}
