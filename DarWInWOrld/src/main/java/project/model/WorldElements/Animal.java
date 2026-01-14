package project.model.WorldElements;

import project.model.MapDirection;
import project.model.RandomGenerator;
import project.model.Vector2D;
import project.model.WorldElements.EdibleElements.Plant;

public class Animal implements WorldElement {
    private MapDirection currDirection;
    private Vector2D position;
    private final Genome genom;
    private int energy;
    private int daysAlive = 0;
    private int protection;

    private static final Vector2D START_POSITION = new Vector2D(2, 2);
    private static final int START_ENERGY = 100;
    private static final int GENOM_LENGTH = 8;

    public Animal(int startEnergy, int genomLength, Genome protectionGenome) {
        this.position = START_POSITION;
        this.energy = startEnergy;
        this.genom = new Genome(genomLength);
        this.protection = Genome.protectionLevel(this.genom, protectionGenome);
        this.currDirection = RandomGenerator.randomDirection();
    }

    public Animal(Vector2D position, int startEnergy, int genomLength) {
        this.position = position;
        // to do: zarządzanie energia, parametr - wartość energii startowej
        this.energy = startEnergy;
        this.currDirection = RandomGenerator.randomDirection();
        this.genom = new Genome(genomLength);

        // to do: dodanie parametru - genom ochrony przed trucizną i obliczenie ochrony zwierzaka
        int protection = 0;
    }

    public Animal(Vector2D position) {
        this(position, START_ENERGY, GENOM_LENGTH);
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
        if(energy > 0) {
            int rotation = genom.getGenomeSequence()[daysAlive % this.genom.getGenomeSize()];
            currDirection = currDirection.rotate(rotation);
            position = position.add(currDirection.toUnitVector());
            daysAlive += 1;
        }
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void energyLoss(int energyAmount) {
        this.energy -= energyAmount;
    }

    public void energyIncrease(int energyAmount) {
        this.energy += energyAmount;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenom() {
        return this.genom;
    }

    public void eat(Plant plant) {
        this.energy += plant.getEnergy();
    }
}
