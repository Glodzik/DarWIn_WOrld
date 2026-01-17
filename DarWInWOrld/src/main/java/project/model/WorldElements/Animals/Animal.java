package project.model.WorldElements.Animals;

import project.model.Boundary;
import project.model.MapDirection;
import project.model.RandomGenerator;
import project.model.Vector2D;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.WorldElement;

import java.util.Random;

public final class Animal implements WorldElement {
    private MapDirection currDirection;
    private Vector2D position;
    private final Genome genom;
    private int energy;
    private int daysAlive = 0;
    private int protection = 0;
    private int childrenCount = 0;
    private int movingPointer;

    private static final Vector2D START_POSITION = new Vector2D(0, 0);
    private static final int START_ENERGY = 100;

    public Animal(Animal animal1, Animal animal2, AnimalParameters parameters, Genome protectionGenome) {
        animal1.energyLoss(parameters.energyLevelToBreed());
        animal2.energyLoss(parameters.energyLevelToBreed());
        animal1.childrenCount += 1;
        animal2.childrenCount += 1;

        this.energy = parameters.energyLossAfterBreed() * 2;
        this.position = animal1.getPosition();
        this.currDirection = RandomGenerator.randomDirection();

        Animal strongerParent = animal1.getEnergy() >= animal2.getEnergy() ? animal1 : animal2;
        Animal weakerParent = animal1.getEnergy() >= animal2.getEnergy() ? animal2 : animal1;
        this.genom = new Genome(
                strongerParent.getGenom(), weakerParent.getGenom(),
                strongerParent.getEnergy(), weakerParent.getEnergy(),
                parameters.minMutation(), parameters.maxMutation()
        );
        this.protection = Genome.protectionLevel(this.genom, protectionGenome);
        Random random = new Random();
        this.movingPointer = random.nextInt(parameters.genomLength());
    }

    public Animal(AnimalParameters parameters, Genome protectionGenome) {
        this.position = START_POSITION;
        this.currDirection = RandomGenerator.randomDirection();
        this.energy = parameters.startEnergy();
        this.genom = new Genome(parameters.genomLength());
        this.protection = Genome.protectionLevel(this.genom, protectionGenome);
        Random random = new Random();
        this.movingPointer = random.nextInt(parameters.genomLength());
    }

    public Animal(Vector2D position) {
        this.position = position;
        this.genom = new Genome(8);
        this.currDirection = RandomGenerator.randomDirection();
        this.energy = START_ENERGY;
        Random random = new Random();
        this.movingPointer = random.nextInt(genom.getGenomeSize());
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

    public void move(Boundary bounds) {
        if (energy > 0) {
            int rotation = genom.getGenomeSequence()[movingPointer % this.genom.getGenomeSize()];
            currDirection = currDirection.rotate(rotation);
            Vector2D oldPosition = position;
            Vector2D newPosition = position.add(currDirection.toUnitVector());
            daysAlive += 1;
            movingPointer += 1;

            Vector2D lowerLeft = bounds.lowerLeft();
            Vector2D upperRight = bounds.upperRight();

            if(!(newPosition.precedes(upperRight) && newPosition.follows(lowerLeft))) {
                if (newPosition.aboveYLine(upperRight.getY()) || newPosition.belowYLine(lowerLeft.getY())) {
                    // turning back from the poles
                    this.turnBack();
                    this.position = oldPosition;
                } else if (newPosition.onLeftXLine(lowerLeft.getX())) {
                    // transition from left to right
                    this.position = new Vector2D(upperRight.getX(), newPosition.getY());
                } else if (newPosition.onRightXLine(upperRight.getX())) {
                    // transition from right to left
                    this.position = new Vector2D(lowerLeft.getX(), newPosition.getY());
                }
            } else {
                this.position = newPosition;
            }
        }
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void energyLoss(int energyAmount) {
        this.energy -= energyAmount;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenom() {
        return this.genom;
    }

    public int getDaysAlive() {
        return this.daysAlive;
    }

    public int getChildrenCount() {
        return this.childrenCount;
    }

    public void eat(Plant plant) {
        int energyFromPlant = plant.getEnergy();
        if(energyFromPlant < 0) {
            this.energy += (int)(energyFromPlant * (100.0 - protection) / 100.0);
        } else {
            this.energy += energyFromPlant;
        }
    }

    public boolean isDead() {
        return this.energy <= 0;
    }
}