package cs.jirkamayer.gatefields;

import cs.jirkamayer.gatefields.math.Vector2D;
import cs.jirkamayer.gatefields.scheme.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaverTest {
    private Scheme scheme;
    private Simulator sim;
    private Camera camera;
    private Saver saver;

    private ByteArrayOutputStream savedFile;

    @BeforeEach
    void setUp() {
        scheme = new Scheme();
        sim = scheme.getSimulator();
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

    @Test
    void itStoresElements() {
        scheme.add(new NotGate());

        save();
        scheme.clear();
        load();

        assertEquals(1, scheme.getElements().size());
        assertTrue(scheme.getElements().get(0) instanceof NotGate);
    }

    @Test
    void itStoresWires() {
        NotGate gate = new NotGate();
        Vertex a = new Vertex(Vector2D.ZERO);
        Vertex b = new Vertex(new Vector2D(42, 82));
        scheme.add(a);
        scheme.add(b);
        scheme.add(gate);
        scheme.add(new Wire(a, gate.inputVertex));
        scheme.add(new Wire(a, b));

        save();
        scheme.clear();
        load();

        assertEquals(2, scheme.getWires().size());
        assertTrue(scheme.getWires().get(0).end.isBound());
        assertTrue(scheme.getWires().get(0).end.getBoundElement() instanceof NotGate);
        assertFalse(scheme.getWires().get(0).start.isBound());
        assertEquals(42, scheme.getWires().get(1).end.transform.getPosition().x);
    }

    @Test
    void itStoresActiveElements() {
        NotGate gate = new NotGate();
        Vertex a = new Vertex(Vector2D.ZERO);
        scheme.add(a);
        scheme.add(gate);

        sim.activateVertex(a);
        sim.activateVertex(gate.outputVertex);
        sim.processVertexActivations();

        save();
        scheme.clear();
        load();

        assertTrue(sim.isActive(scheme.getVertices().get(0)));
        assertFalse(sim.isActive(scheme.getElements().get(0).vertices.get(0)));
        assertTrue(sim.isActive(scheme.getElements().get(0).vertices.get(1)));
    }
}