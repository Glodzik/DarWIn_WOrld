package project.model.random;

import project.model.coordinates.Boundary;
import project.model.coordinates.Vector2D;
import project.model.map.MapDirection;
import project.model.worldelements.edibleelements.*;

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

    public static Plant randomCustomPlants(int poisonPlantProbability, int basePlantEnergy, int basePoisonEnergy) {
        int probability = random.nextInt(101);
        double[] multipliers = {1, 1.5, 2.0, 2.5, 3.0};

        if(probability <= poisonPlantProbability) {
            int poisonIndex = random.nextInt(TypeOfPoison.values().length);
            int scaledEnergy = (int) (basePoisonEnergy * multipliers[poisonIndex]);
            return new Poison(null, poisonIndex, scaledEnergy);
        } else {
            int antidoteIndex = random.nextInt(TypeOfAntidote.values().length);
            int scaledEnergy = (int) (basePlantEnergy * multipliers[antidoteIndex]);
            return new Antidote(null, antidoteIndex, scaledEnergy);
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

    public static int randomMovingPointer(int genomLength) {
        return random.nextInt(genomLength);
    }
}
