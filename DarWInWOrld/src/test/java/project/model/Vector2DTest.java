package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Vector2D;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

    @Test
    void getters() {
        //given
        Vector2D v1 = new Vector2D(2,-1);
        Vector2D v2 = new Vector2D(0,0);
        Vector2D v3 = new Vector2D(-2,1);

        //when & then
        assertEquals(2, v1.getX());
        assertEquals(-1, v1.getY());
        assertEquals(0, v2.getX());
        assertEquals(0, v2.getY());
        assertEquals(-2, v3.getX());
        assertEquals(1, v3.getY());
    }

    @Test
    void precedes() {
        //given
        Vector2D v1 = new Vector2D(1,1);
        Vector2D v2 = new Vector2D(2,2);
        Vector2D v3 = new Vector2D(3,3);
        Vector2D v4 = new Vector2D(1,3);
        Vector2D v5 = new Vector2D(3,1);

        //when & then
        assertTrue(v1.precedes(v2));
        assertTrue(v2.precedes(v2)); //te same wektory
        assertFalse(v3.precedes(v2));
        assertFalse(v4.precedes(v2));
        assertFalse(v5.precedes(v2));

    }

    @Test
    void follows() {
        //given
        Vector2D v1 = new Vector2D(1,1);
        Vector2D v2 = new Vector2D(2,2);
        Vector2D v3 = new Vector2D(3,3);
        Vector2D v4 = new Vector2D(1,3);
        Vector2D v5 = new Vector2D(3,1);

        //when & then
        assertTrue(v2.follows(v2)); //te same wektory
        assertTrue(v3.follows(v2)); //
        assertFalse(v1.follows(v2));
        assertFalse(v4.follows(v2));
        assertFalse(v5.follows(v2));
    }

    @Test
    void addTwoPositiveVectors() {
        //given
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(2,3);

        //when
        Vector2D result = v1.add(v2);

        //then
        assertEquals(new Vector2D(3,5), result);
    }

    @Test
    void addTwoNegativeVectors() {
        //given
        Vector2D v1 = new Vector2D(-1,-2);
        Vector2D v2 = new Vector2D(-2,-3);

        //when
        Vector2D result = v1.add(v2);

        //then
        assertEquals(new Vector2D(-3,-5), result);
    }

    @Test
    void addOnePositiveOneNegative() {
        //given
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(-2,-3);

        //when
        Vector2D result = v1.add(v2);

        //then
        assertEquals(new Vector2D(-1,-1), result);
    }

    @Test
    void addVectorToZeroVector() {
        //given
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(0,0);
        Vector2D v3 = new Vector2D(2,3);

        //when
        Vector2D result1 = v1.add(v2);
        Vector2D result2 = v2.add(v3);

        //then
        assertEquals(new Vector2D(1,2), result1);
        assertEquals(new Vector2D(2,3), result2);
    }

    @Test
    void subtractPositiveVectors() {
        //given
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(2,3);

        //when
        Vector2D result = v1.subtract(v2);

        //then
        assertEquals(new Vector2D(-1,-1), result);
    }

    @Test
    void subtractNegativeVectors() {
        //given
        Vector2D v1 = new Vector2D(-1,-2);
        Vector2D v2 = new Vector2D(-2,-3);

        //when
        Vector2D result = v1.subtract(v2);

        //then
        assertEquals(new Vector2D(1,1), result);
    }

    @Test
    void upperRightPositive() {
        //given
        Vector2D v1 = new Vector2D(3,4);
        Vector2D v2 = new Vector2D(2,6);

        //when
        Vector2D result = v1.upperRight(v2);

        //then
        assertEquals(new Vector2D(3,6), result);
    }

    @Test
    void upperRightNegative() {
        //given
        Vector2D v1 = new Vector2D(-3,-4);
        Vector2D v2 = new Vector2D(-2,-6);

        //when
        Vector2D result = v1.upperRight(v2);

        //then
        assertEquals(new Vector2D(-2,-4), result);
    }

    @Test
    void lowerLeftPositive() {
        //given
        Vector2D v1 = new Vector2D(3,4);
        Vector2D v2 = new Vector2D(2,6);

        //when
        Vector2D result = v1.lowerLeft(v2);

        //then
        assertEquals(new Vector2D(2,4), result);
    }

    @Test
    void lowerLeftNegative() {
        //given
        Vector2D v1 = new Vector2D(-3,-4);
        Vector2D v2 = new Vector2D(-2,-6);

        //when
        Vector2D result = v1.lowerLeft(v2);

        //then
        assertEquals(new Vector2D(-3,-6), result);
    }

    @Test
    void aboveYLine() {
        // given
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(-3, -5);
        Vector2D v3 = new Vector2D(6, 8);

        // when
        boolean result1 = v1.aboveYLine(-1);
        boolean result2 = v1.aboveYLine(0);

        boolean result3 = v2.aboveYLine(-6);
        boolean result4 = v2.aboveYLine(0);

        boolean result5 = v3.aboveYLine(1);
        boolean result6 = v3.aboveYLine(10);

        // then
        assertTrue(result1);
        assertFalse(result2);

        assertTrue(result3);
        assertFalse(result4);

        assertTrue(result5);
        assertFalse(result6);
    }

    @Test
    void belowYLine() {
        // given
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(-3, -5);
        Vector2D v3 = new Vector2D(6, 8);

        // when
        boolean result1 = v1.belowYLine(1);
        boolean result2 = v1.belowYLine(0);

        boolean result3 = v2.belowYLine(-4);
        boolean result4 = v2.belowYLine(-10);

        boolean result5 = v3.belowYLine(10);
        boolean result6 = v3.belowYLine(6);

        // then
        assertTrue(result1);
        assertFalse(result2);

        assertTrue(result3);
        assertFalse(result4);

        assertTrue(result5);
        assertFalse(result6);
    }

    @Test
    void onLeftXLine() {
        // given
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(-3, -5);
        Vector2D v3 = new Vector2D(6, 8);

        // when
        boolean result1 = v1.onLeftXLine(1);
        boolean result2 = v1.onLeftXLine(0);

        boolean result3 = v2.onLeftXLine(-2);
        boolean result4 = v2.onLeftXLine(-5);

        boolean result5 = v3.onLeftXLine(10);
        boolean result6 = v3.onLeftXLine(3);

        // then
        assertTrue(result1);
        assertFalse(result2);

        assertTrue(result3);
        assertFalse(result4);

        assertTrue(result5);
        assertFalse(result6);
    }

    @Test
    void onRightXLine() {
        // given
        Vector2D v1 = new Vector2D(0, 0);
        Vector2D v2 = new Vector2D(-3, -5);
        Vector2D v3 = new Vector2D(6, 8);

        // when
        boolean result1 = v1.onRightXLine(-1);
        boolean result2 = v1.onRightXLine(0);

        boolean result3 = v2.onRightXLine(-6);
        boolean result4 = v2.onRightXLine(0);

        boolean result5 = v3.onRightXLine(1);
        boolean result6 = v3.onRightXLine(12);

        // then
        assertTrue(result1);
        assertFalse(result2);

        assertTrue(result3);
        assertFalse(result4);

        assertTrue(result5);
        assertFalse(result6);
    }


    @Test
    void testToString() {
        //given
        Vector2D v = new Vector2D(2,1);

        //when & then
        assertEquals("(2, 1)", v.toString());

    }

    @Test
    void testEquals() {
        //given
        Vector2D v1 = new Vector2D(2,3);
        Vector2D v2 = new Vector2D(2,3);
        Vector2D v3 = new Vector2D(2,4);
        Vector2D v4 = new Vector2D(1,3);
        Vector2D v5 = new Vector2D(4,7);

        //when & then
        assertTrue(v1.equals(v2)); // te same wartości
        assertFalse(v1.equals(v3)); // inny y
        assertFalse(v1.equals(v4)); // inny x
        assertFalse(v1.equals(v5)); // rozne wartosci
        assertTrue(v1.equals(v1)); // ten sam wektor
        assertFalse(v1.equals(null)); // null
    }

    @Test
    void testHashCode() {
        //given
        Vector2D v1 = new Vector2D(1,2);
        Vector2D v2 = new Vector2D(2,3);
        Vector2D v3 = new Vector2D(2,3);

        //when & then
        assertEquals(v1.hashCode(), v1.hashCode());

        assertTrue(v2.equals(v3));
        assertEquals(v2.hashCode(), v3.hashCode());
    }
}