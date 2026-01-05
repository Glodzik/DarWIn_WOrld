package project.model;

import java.util.Random;


public final class GenomeGenerator {
    public static String randomGenome(int n) {
        StringBuilder genome = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            genome.append(random.nextInt(8));
        }

        return genome.toString();
    }
}
