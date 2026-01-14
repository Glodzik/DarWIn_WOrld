package project.model.WorldElements;

import java.util.Random;

public final class Genome {

    private final int size;
    private int[] genomeSequence;

    public Genome(int size) {
        this.size = size;
        this.genomeSequence = new int[size];
        generateRandomGenome(size);
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
        return genomeSequence;
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
