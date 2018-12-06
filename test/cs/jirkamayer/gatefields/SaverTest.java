package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.Scheme;
import cs.jirkamayer.gatefields.scheme.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaverTest {
    private Scheme scheme;
    private Camera camera;
    private Saver saver;

    private ByteArrayOutputStream savedFile;

    @BeforeEach
    void setUp() {
        scheme = new Scheme();
        camera = new Camera();
        saver = new Saver(scheme, camera);
    }

    private void save() {
        savedFile = new ByteArrayOutputStream();
        try {
            saver.saveTo(savedFile);
        } catch (IOException e) {
            fail(e);
        }
    }

    private void load() {
        try {
            saver.loadFrom(
                new ByteArrayInputStream(savedFile.toByteArray())
            );
        } catch (IOException e) {
            fail(e);
        }
    }

    @Test
    void itStoresCameraProperties() {
        camera.position = new Vector2D(42, 43);
        camera.scale = 44.45f;

        save();

        camera.position = Vector2D.ZERO;
        camera.scale = 0f;

        load();

        assertEquals(42, camera.position.x);
        assertEquals(43, camera.position.y);
        assertEquals(44.45f, camera.scale);
    }

    @Test
    void itStoresFreeVertices() {
        scheme.add(new Vertex(new Vector2D(1, 2)));
        scheme.add(new Vertex(new Vector2D(3, 4)));
        scheme.getVertices().get(0).transform.setLocalRotation(42);

        save();

        scheme.clear();

        load();

        assertEquals(2, scheme.getVertices().size());
        assertEquals(2, scheme.getVertices().get(0).transform.getLocalPosition().y);
        assertEquals(42, scheme.getVertices().get(0).transform.getLocalRotation());
        assertEquals(3, scheme.getVertices().get(1).transform.getLocalPosition().x);
    }
}