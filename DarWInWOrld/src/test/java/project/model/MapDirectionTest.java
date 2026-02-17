package project.model;

import org.junit.jupiter.api.Test;
import project.model.coordinates.Vector2D;
import project.model.map.MapDirection;

import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    void testToString() {
        assertEquals("North", MapDirection.NORTH.toString());
        assertEquals("NorthEast", MapDirection.NORTH_EAST.toString());
        assertEquals("East", MapDirection.EAST.toString());
        assertEquals("SouthEast", MapDirection.SOUTH_EAST.toString());
        assertEquals("South", MapDirection.SOUTH.toString());
        assertEquals("SouthWest", MapDirection.SOUTH_WEST.toString());
        assertEquals("West", MapDirection.WEST.toString());
        assertEquals("NorthWest", MapDirection.NORTH_WEST.toString());
    }

    @Test
    void testToUnitVector() {
        MapDirection[] values = MapDirection.values();

        for(MapDirection direction : values) {
            Vector2D unitVector = direction.toUnitVector();
            switch (direction) {
                case NORTH -> {
                    assertEquals(0, unitVector.getX());
                    assertEquals(1, unitVector.getY());
                }
                case NORTH_EAST -> {
                    assertEquals(1, unitVector.getX());
                    assertEquals(1, unitVector.getY());
                }
                case EAST -> {
                    assertEquals(1, unitVector.getX());
                    assertEquals(0, unitVector.getY());
                }
                case SOUTH_EAST -> {
                    assertEquals(1, unitVector.getX());
                    assertEquals(-1, unitVector.getY());
                }
                case SOUTH -> {
                    assertEquals(0, unitVector.getX());
                    assertEquals(-1, unitVector.getY());
                }
                case SOUTH_WEST -> {
                    assertEquals(-1, unitVector.getX());
                    assertEquals(-1, unitVector.getY());
                }
                case WEST -> {
                    assertEquals(-1, unitVector.getX());
                    assertEquals(0, unitVector.getY());
                }
                case NORTH_WEST -> {
                    assertEquals(-1, unitVector.getX());
                    assertEquals(1, unitVector.getY());
                }
            }
        }
    }

    @Test
    void testRotateOneStep() {
        MapDirection[] values = MapDirection.values();

        for(MapDirection direction : values) {
            MapDirection rotated = direction.rotate(1);
            switch (direction) {
                case NORTH -> {
                    assertEquals(MapDirection.NORTH_EAST, rotated);
                }
                case NORTH_EAST -> {
                    assertEquals(MapDirection.EAST, rotated);
                }
                case EAST -> {
                    assertEquals(MapDirection.SOUTH_EAST, rotated);
                }
                case SOUTH_EAST -> {
                    assertEquals(MapDirection.SOUTH, rotated);
                }
                case SOUTH -> {
                    assertEquals(MapDirection.SOUTH_WEST, rotated);
                }
                case SOUTH_WEST -> {
                    assertEquals(MapDirection.WEST, rotated);
                }
                case WEST -> {
                    assertEquals(MapDirection.NORTH_WEST, rotated);
                }
                case NORTH_WEST -> {
                    assertEquals(MapDirection.NORTH, rotated);
                }
            }
        }
    }

    @Test
    void testRotateInvalidStep() {
        MapDirection initial = MapDirection.NORTH;
        MapDirection initial2 = MapDirection.NORTH;

        MapDirection rotated = initial.rotate(8);
        MapDirection rotated2 = initial2.rotate(-1);

        assertEquals(MapDirection.NORTH, rotated);
        assertEquals(MapDirection.NORTH, rotated2);
    }

    @Test
    void testOpposite() {
        MapDirection[] values = MapDirection.values();

        for(MapDirection direction : values) {
            MapDirection opposite = direction.opposite();
            switch (direction) {
                case NORTH -> {
                    assertEquals(MapDirection.SOUTH, opposite);
                }
                case NORTH_EAST -> {
                    assertEquals(MapDirection.SOUTH_WEST, opposite);
                }
                case EAST -> {
                    assertEquals(MapDirection.WEST, opposite);
                }
                case SOUTH_EAST -> {
                    assertEquals(MapDirection.NORTH_WEST, opposite);
                }
                case SOUTH -> {
                    assertEquals(MapDirection.NORTH, opposite);
                }
                case SOUTH_WEST -> {
                    assertEquals(MapDirection.NORTH_EAST, opposite);
                }
                case WEST -> {
                    assertEquals(MapDirection.EAST, opposite);
                }
                case NORTH_WEST -> {
                    assertEquals(MapDirection.SOUTH_EAST, opposite);
                }
            }
        }
    }

    @Test
    void testOppositeTwice() {
        MapDirection initial = MapDirection.EAST;

        MapDirection opposite = initial.opposite();
        MapDirection back = opposite.opposite();

        assertEquals(initial, back);
    }

    @Test
    void testRotateDegrees() {
        MapDirection[] values = MapDirection.values();

        for(MapDirection direction : values) {
            int degrees = direction.rotateDegrees();
            switch (direction) {
                case NORTH -> {
                    assertEquals(0, degrees);
                }
                case NORTH_EAST -> {
                    assertEquals(45, degrees);
                }
                case EAST -> {
                    assertEquals(90, degrees);
                }
                case SOUTH_EAST -> {
                    assertEquals(135, degrees);
                }
                case SOUTH -> {
                    assertEquals(180, degrees);
                }
                case SOUTH_WEST -> {
                    assertEquals(225, degrees);
                }
                case WEST -> {
                    assertEquals(270, degrees);
                }
                case NORTH_WEST -> {
                    assertEquals(315, degrees);
                }
            }
        }
    }
}