package project.model;

import java.util.Arrays;

public final class Genome {

    private final int size;
    private int[] genomeSequence;

    public Genome(int size) {
        this.size = size;
        this.genomeSequence = new int[size];
    }

    @Override
    public String toString() {
        return Arrays.toString(genomeSequence);
    }

    public int[] getGenomeSequence() {
        return genomeSequence;
    }
}
