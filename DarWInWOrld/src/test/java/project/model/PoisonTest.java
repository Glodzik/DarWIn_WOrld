package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Vector2D;
import project.model.worldelements.edibleelements.Poison;
import project.model.worldelements.edibleelements.TypeOfPoison;

import static org.junit.jupiter.api.Assertions.*;

class PoisonTest {
    @Test
    void testConstructorWithType() {
        Vector2D position = new Vector2D(5, 5);
        TypeOfPoison type = TypeOfPoison.AZALEA;

        Poison poison = new Poison(position, type);

        assertEquals(position, poison.getPosition());
        assertEquals(type.getEnergy(), poison.getEnergy());
        assertEquals(type, poison.getType());
        assertTrue(poison.getEnergy() < 0);
    }

    @Test
    void testConstructorWithInteger() {
        Vector2D position = new Vector2D(2, 2);
        int poisonIndex = 2;
        TypeOfPoison[] values = TypeOfPoison.values();

        Poison poison = new Poison(position, poisonIndex);

        assertEquals(position, poison.getPosition());
        assertEquals(values[poisonIndex].getEnergy(), poison.getEnergy());
        assertEquals(values[poisonIndex], poison.getType());
    }

    @Test
    void testConstructorWithIntegerAndScaledEnergy() {
        Vector2D position = new Vector2D(4, 4);
        int poisonIndex = 1;
        int scaledEnergy = 25;
        TypeOfPoison[] values = TypeOfPoison.values();

        Poison poison = new Poison(position, poisonIndex, scaledEnergy);

        assertEquals(position, poison.getPosition());
        assertEquals(-scaledEnergy, poison.getEnergy());
        assertEquals(values[1], poison.getType());
        assertEquals(-25, poison.getEnergy());
    }

    @Test
    void testGetType() {
        Vector2D position = new Vector2D(5, 5);
        TypeOfPoison expectedType = TypeOfPoison.OLEANDER;
        Poison poison = new Poison(position, expectedType);

        TypeOfPoison type = poison.getType();

        assertEquals(expectedType, type);
    }

    @Test
    void testMultiplePoisonsDifferentTypes() {
        Vector2D position = new Vector2D(5, 5);

        TypeOfPoison[] values = TypeOfPoison.values();
        int length = values.length;
        Poison[] poisons = new Poison[length];

        for (int i = 0; i < length; i++) {
            poisons[i] = new Poison(position, i);
        }

        for(int i = 0; i < length; i++) {
            assertEquals(values[i], poisons[i].getType());
        }
    }
}