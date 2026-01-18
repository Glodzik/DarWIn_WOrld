package project.model.WorldElements.Animals;

import project.model.RandomGenerator;

import java.util.Comparator;

public final class AnimalComparator {
    private static final Comparator<Animal> comparator = Comparator
            .comparingInt(Animal::getEnergy).reversed()
            .thenComparing(Comparator.comparingInt(Animal::getDaysAlive).reversed())
            .thenComparing(Comparator.comparingInt(Animal::getChildrenCount).reversed())
            .thenComparing((a1, a2) -> RandomGenerator.TrueOrFalse() ? -1 : 1);

    public static Comparator<Animal> getComparator() {
        return comparator;
    }
}
