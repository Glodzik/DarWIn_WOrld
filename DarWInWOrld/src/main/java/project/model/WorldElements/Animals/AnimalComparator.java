package project.model.WorldElements.Animals;

import project.model.RandomGenerator;

import java.util.Comparator;

public class AnimalComparator {
    public static Comparator<Animal> createComparator() {
        return Comparator
                .comparingInt(Animal::getEnergy).reversed()
                .thenComparing(Comparator.comparingInt(Animal::getDaysAlive).reversed())
                .thenComparing(Comparator.comparingInt(Animal::getChildrenCount).reversed())
                .thenComparing((a1, a2) -> RandomGenerator.TrueOrFalse() ? -1 : 1);
    }
}
