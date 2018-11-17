package cs.jirkamayer.gatefields.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    void itStoresLocalPosition() {
        Transform t = new Transform();
        t.setLocalPosition(new Vector2D(1, 1));
        assertEquals(new Vector2D(1, 1), t.getLocalPosition());
    }

    @Test
    void itCalculatesGlobalPosition() {
        Transform p = new Transform();
        p.setLocalPosition(new Vector2D(1, 1));

        Transform t = new Transform();
        t.setParent(p);
        t.setLocalPosition(new Vector2D(1, 1));
        assertEquals(new Vector2D(2, 2), t.getPosition()); // with parent

        // when parent updates
        p.setLocalPosition(new Vector2D(5, 5));
        assertEquals(new Vector2D(6, 6), t.getPosition());

        t.setParent(null);
        assertEquals(new Vector2D(1, 1), t.getPosition()); // without parent
    }

    @Test
    void itCountsWithRotation() {
        Transform p = new Transform();
        p.setLocalPosition(new Vector2D(1, 0));
        p.setLocalRotation(Math.PI / 2);

        Transform t = new Transform();
        t.setParent(p);
        t.setLocalPosition(new Vector2D(1, 0));
        t.setLocalRotation(-Math.PI / 2);

        assertEquals(new Vector2D(1, 1), t.getPosition().round());
        assertEquals(0, t.getRotation());
    }

    @Test
    void itStacksThreeTransformsInASpiral() {
        Transform t = new Transform(
            new Vector2D(1, 0), (float)(Math.PI / 2),
            new Transform(
                new Vector2D(1, 0), (float)(Math.PI / 2),
                new Transform(
                    new Vector2D(1, 0), (float)(Math.PI / 2),
                    null
                )
            )
        );

        assertEquals(new Vector2D(0, 1), t.getPosition().round());
        assertEquals((float)(3.0 * Math.PI / 2.0), t.getRotation());
    }
}