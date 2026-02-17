package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Vector2D;
import project.model.worldelements.edibleelements.Antidote;
import project.model.worldelements.edibleelements.TypeOfAntidote;

import static org.junit.jupiter.api.Assertions.*;

class AntidoteTest {
    @Test
    void testConstructorWithType() {
        Vector2D position = new Vector2D(5, 5);
        TypeOfAntidote type = TypeOfAntidote.CARROT;

        Antidote antidote = new Antidote(position, type);

        assertEquals(position, antidote.getPosition());
        assertEquals(type.getEnergy(), antidote.getEnergy());
        assertEquals(type, antidote.getType());
    }

    @Test
    void testConstructorWithInteger() {
        Vector2D position = new Vector2D(2, 2);
        int antidoteIndex = 2;
        TypeOfAntidote[] values = TypeOfAntidote.values();

        Antidote antidote = new Antidote(position, antidoteIndex);

        assertEquals(position, antidote.getPosition());
        assertEquals(values[2].getEnergy(), antidote.getEnergy());
        assertEquals(values[2], antidote.getType());
    }

    @Test
    void testConstructorWithIntegerAndScaledEnergy() {
        Vector2D position = new Vector2D(4, 4);
        int antidoteIndex = 1;
        int scaledEnergy = 25;
        TypeOfAntidote[] values = TypeOfAntidote.values();

        Antidote antidote = new Antidote(position, antidoteIndex, scaledEnergy);

        assertEquals(position, antidote.getPosition());
        assertEquals(scaledEnergy, antidote.getEnergy());
        assertEquals(values[1], antidote.getType());
    }

    @Test
    void testGetType() {
        Vector2D position = new Vector2D(5, 5);
        TypeOfAntidote expectedType = TypeOfAntidote.PARSLEY;
        Antidote antidote = new Antidote(position, expectedType);

        TypeOfAntidote type = antidote.getType();

        assertEquals(expectedType, type);
    }

    @Test
    void testMultipleAntidotesDifferentTypes() {
        Vector2D position = new Vector2D(5, 5);

        TypeOfAntidote[] values = TypeOfAntidote.values();
        int length = values.length;
        Antidote[] antidotes = new Antidote[length];

        for (int i = 0; i < length; i++) {
            antidotes[i] = new Antidote(position, values[i]);
        }

        for(int i = 0; i < length; i++) {
            assertEquals(values[i], antidotes[i].getType());
        }
    }
}