package project.model.WorldElements;

public record AnimalParameters(int startEnergy, int energyLossEveryDay,
                                int energyLevelToBreed, int energyLossAfterBreed,
                                int minMutation, int maxMutation,
                                int genomLength) {
}
