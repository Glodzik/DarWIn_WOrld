package project.model.worldelements.animals;

import java.util.Random;

public final class Genome {

    private final int size;
    private int[] genomeSequence;

    public Genome(int size) {
        this.size = size;
        this.genomeSequence = new int[size];
        generateRandomGenome(size);
    }

    public Genome(Genome strongerParentGenome, Genome weakerParentGenome,
                  int strongerParentEnergy, int weakerParentEnergy, int minMutations, int maxMutations) {
        this.size = strongerParentGenome.getGenomeSize();
        this.genomeSequence = new int[size];

        int sumOfEnergy = strongerParentEnergy + weakerParentEnergy;
        int sizeOfStrongerPart = (int) (((float) strongerParentEnergy / sumOfEnergy) * size);
        int sizeOfWeakerPart = size - sizeOfStrongerPart;
        Random random = new Random();
        int[] strongerGenome = strongerParentGenome.getGenomeSequence();
        int[] weakerGenome = weakerParentGenome.getGenomeSequence();
        if (random.nextDouble() < 0.5) {
            // lewa strona od silniejszego, prawa od słabszego
            System.arraycopy(strongerGenome, 0, this.genomeSequence, 0, sizeOfStrongerPart);
            System.arraycopy(weakerGenome, sizeOfStrongerPart, this.genomeSequence, sizeOfStrongerPart, sizeOfWeakerPart);
        } else {
            // lewa strona od słabszego, prawa od silniejszego
            System.arraycopy(weakerGenome, 0, this.genomeSequence, 0, sizeOfWeakerPart);
            System.arraycopy(strongerGenome, size - sizeOfStrongerPart, this.genomeSequence, sizeOfWeakerPart, sizeOfStrongerPart);
        }

        int mutationCount = random.nextInt(maxMutations - minMutations + 1) + minMutations;
        for (int i = 0; i < mutationCount; i++) {
            int mutationIndex = random.nextInt(size);
            this.genomeSequence[mutationIndex] = random.nextInt(8);
        }
    }

    @Override
    public String toString() {
        StringBuilder sequenceToString = new StringBuilder();
        for (int i : this.genomeSequence) {
            sequenceToString.append(i);
        }
        return sequenceToString.toString();
    }

    public int[] getGenomeSequence() {
        return genomeSequence.clone();
    }

    public int getGenomeSize() {
        return size;
    }

    public void generateRandomGenome(int size) {
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            this.genomeSequence[i] = random.nextInt(8);
        }
    }

    public static int protectionLevel(Genome animalGenome, Genome protectionGenome) {
        int[] protectionGenomeArray = protectionGenome.getGenomeSequence();
        int[] animalGenomeArray = animalGenome.getGenomeSequence();
        int matches = 0;
        int minLength = Math.min(protectionGenomeArray.length, animalGenomeArray.length);

        for (int i = 0; i < minLength; i++) {
            if (animalGenomeArray[i] == protectionGenomeArray[i]) {
                matches++;
            }
        }

        double percentage = (double) matches / protectionGenomeArray.length * 100;
        return (int) Math.round(percentage);
    }
}
