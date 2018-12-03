package cs.jirkamayer.gatefields.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {
    @Test
    void itRotates() {
        Vector2D v = new Vector2D(2, 0);

        assertTrue(new Vector2D(0, 2).minus(v.rotate(Math.PI / 2)).length() < 0.001);
        assertTrue(new Vector2D(-2, 0).minus(v.rotate(Math.PI)).length() < 0.001);
        assertTrue(new Vector2D(0, -2).minus(v.rotate(3 * Math.PI / 2)).length() < 0.001);
    }

    @Test
    void itComputesAngle() {
        assertTrue(Math.abs(new Vector2D(1, 0).angle() - 0) < 0.001);
        assertTrue(Math.abs(new Vector2D(0, 1).angle() - Math.PI / 2) < 0.001);
        assertTrue(Math.abs(new Vector2D(1, 1).angle() - Math.PI / 4) < 0.001);
    }
}