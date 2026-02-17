package project.model;

import org.junit.jupiter.api.Test;
import project.model.worldelements.animals.Genome;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {
    @Test
    void testConstructor() {
        int size = 10;
        Genome genome = new Genome(size);

        assertNotNull(genome);
        assertEquals(size, genome.getGenomeSize());
        assertEquals(size, genome.getGenomeSequence().length);

        int[] sequence = genome.getGenomeSequence();
        for (int gene : sequence) {
            assertTrue(gene >= 0 && gene < 8);
        }
    }

    @Test
    void testConstructorWithParents() {
        Genome parent1 = new Genome(8);
        Genome parent2 = new Genome(8);
        int parent1Energy = 70;
        int parent2Energy = 30;

        Genome childGenome = new Genome(parent1, parent2, parent1Energy, parent2Energy, 1, 3);

        assertEquals(8, childGenome.getGenomeSize());

        int[] childSequence = childGenome.getGenomeSequence();
        assertEquals(8, childSequence.length);

        for (int gene : childSequence) {
            assertTrue(gene >= 0 && gene < 8);
        }
    }

    @Test
    void testToString() {
        Genome genome = new Genome(5);

        String genomeString = genome.toString();

        assertEquals(5, genomeString.length());

        for (char c : genomeString.toCharArray()) {
            assertTrue(c >= '0' && c <= '7');
        }
    }

    @Test
    void testFormatGenome() {
        int[] genes = {0, 1, 2, 3, 4, 5, 6, 7};

        String formatted = Genome.formatGenome(genes);

        assertEquals("01234567", formatted);
    }
}